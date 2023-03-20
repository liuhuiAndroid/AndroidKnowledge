#include <sys/stat.h>
#include <asm/fcntl.h>
#include <fcntl.h>
#include <android/log.h>
#include "MMKV.h"

// 4K整数倍，getpagesize 获取一页大小，4096
int32_t DEFAULT_MMAP_SIZE = getpagesize();

void MMKV::initializeMMKV(const char *path) {
    // 创建文件
    g_rootDir = path;
    // 创建文件夹
    mkdir(g_rootDir.c_str(), 0777);
}

MMKV *MMKV::defaultMMKV() {
    // 实例化 MMKV，传入文件名
    MMKV *kv = new MMKV(DEFAULT_MMAP_ID);
    return kv;
}

MMKV::MMKV(const char *mmapID) {
    // 全路径
    m_path = g_rootDir + "/" + mmapID;
    // 产生映射
    loadFromFile();
}

void MMKV::loadFromFile() {
    // 打开文件
    m_fd = open(m_path.c_str(), O_RDWR | O_CREAT, S_IRWXU);
    // 获取文件具体的大小
    struct stat st = {0};
    // 通过 fstat 获取文件发小
    if (fstat(m_fd, &st) != -1) {
        // 可以获取，大小可能非4k，需要纠正，否则没办法映射
        m_size = st.st_size;
    }
    // 小于4k 或者 不是 4k 的整数倍
    if (m_size < DEFAULT_MMAP_SIZE || (m_size % DEFAULT_MMAP_SIZE != 0)) {
        int32_t oldSize = m_size;
        // 新的4k整数倍
        m_size = ((m_size / DEFAULT_MMAP_SIZE) + 1) * DEFAULT_MMAP_SIZE;
        // 修改文件大小
        if (ftruncate(m_fd, m_size) != 0) {
            m_size = st.st_size;
        }
        //如果文件大小被增加了，让增加这些大小的内容变成空，否则内容是随机的
        zeroFillFile(m_fd, oldSize, m_size - oldSize);
    }
    // 此时 m_fd 这个文件大小是 4k 整数倍
    // mmap 函数：将物理内存和磁盘进行映射
    // addr = 0，随机分配内存区域
    // m_size：映射区的长度，必须是一页，比如 4096
    // __flags：MAP_SHARED，进程共享
    // __fd：文件描述符，一般是由 open 函数返回，也可以设置为-1匿名映射，需要 __flags 设置为 MAP_ANON
    // __offset：偏移量
    m_ptr = static_cast<int8_t *>(mmap(0, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, m_fd, 0));
    // 先从 m_ptr 获取到原先数据缓存到内存中
    // 先读取4个字节获取总长度
    // memcpy 也可以将写入数据拷贝到 m_ptr，例如 memcpy(&m_ptr, data.data(), data.size());
    memcpy(&m_actualSize, m_ptr, 4);
    __android_log_print(ANDROID_LOG_VERBOSE, "sec", "m_actualSize=%d ", m_actualSize);
    // 数据长度大于0
    if (m_actualSize > 0) {
        // 读后面的数据：key长度 key内容 value长度 value内容
        // 实例化 inputBuffer 对象
        // 匿名构造函数
        ProtoBuf inputBuffer(m_ptr + 4, m_actualSize);
        // 清空 hashmap
        m_dic.clear();
        // 将文件内容解析为 map
        while (!inputBuffer.isAtEnd()) {
            // 开始解析，读第一个 key
            std::string key = inputBuffer.readString();
            __android_log_print(ANDROID_LOG_VERBOSE, "sec", "key=%s", key.c_str());
            if (key.length() > 0) {
                // 先读 value 的长度，再读取 value
                ProtoBuf *value = inputBuffer.readData();
                // 数据有效则保存，否则删除key，因为我们是append的
                if (value && value->length() > 0) {
                    // 放入 hashmap，emplace = put
                    // hashmap 保证 key 的唯一性
                    m_dic.emplace(key, value);
                }
            }
        }
    }
    // 为了后续动态扩容，需要保存一份原始数据
    m_output = new ProtoBuf(m_ptr + 4 + m_actualSize, m_size - 4 - m_actualSize);
}

// 文件写入0
void MMKV::zeroFillFile(int fd, int32_t startPos, int32_t size) {
    if (lseek(fd, startPos, SEEK_SET) < 0) {
        return;
    }
    static const char zeros[4096] = {0};
    while (size >= sizeof(zeros)) {
        if (write(fd, zeros, sizeof(zeros)) < 0) {
            return;
        }
        size -= sizeof(zeros);
    }
    if (size > 0) {
        if (write(fd, zeros, size) < 0) {
            return;
        }
    }
}

void MMKV::putInt(const std::string &key, int32_t value) {
    // 计算 value 的长度
    int32_t size = ProtoBuf::computeInt32Size(value);
    ProtoBuf *buf = new ProtoBuf(size);
    buf->writeRawInt(value);
    // 暂时写入 hashmap
    m_dic.emplace(key, buf);
    appendDataWithKey(key, buf);
}

void MMKV::appendDataWithKey(std::string key, ProtoBuf *value) {
    // 计算待写入的数据大小，key+value
    int32_t itemSize = ProtoBuf::computeItemSize(key, value);
    if (itemSize > m_output->spaceLeft()) {
        // 计算我需要总大小，以 hashmap 为准
        int32_t needSize = ProtoBuf::computeMapSize(m_dic);
        // 实际数据+总长度4字节
        needSize += 4;
        // 计算每个 item 的平均长度
        int32_t avgItemSize = needSize / std::max<int32_t>(1, m_dic.size());
        // 最小扩容8个字节，数据量多扩容一半
        int32_t futureUsage = avgItemSize * std::max<int32_t>(8, (m_dic.size() + 1) / 2);
        // m_size：已有数据
        if (needSize + futureUsage >= m_size) {
            // 此时需要扩容，判断为了避免脏数据
            // 为了防止将来使用大小不够导致频繁重写，扩充一倍
            int32_t oldSize = m_size;
            do {
                // 扩充一倍
                m_size *= 2;
                // 如果在需要的与将来可能增加的加起来比扩容后还要大，继续扩容
            } while (needSize + futureUsage >= m_size);
            // 重新设定文件大小
            ftruncate(m_fd, m_size);
            // 清空文件
            zeroFillFile(m_fd, oldSize, m_size - oldSize);
            // 解除映射
            munmap(m_ptr, oldSize);
            // 重新映射
            m_ptr = (int8_t *) mmap(m_ptr, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, m_fd, 0);
        }
        // 扩容之后，全量写入数据
        m_actualSize = needSize - 4;
        // 先写入总长度
        memcpy(m_ptr, &m_actualSize, 4);
        __android_log_print(ANDROID_LOG_VERBOSE, "david", "extending  full write");
        // 删除之前的全局缓存
        delete m_output;
        // 创建新的全局缓存
        m_output = new ProtoBuf(m_ptr + 4, m_size - 4);
        // 遍历 hashmap 重新写数据
        auto iterator = m_dic.begin();
        for (; iterator != m_dic.end(); iterator++) {
            auto k = iterator->first;
            auto v = iterator->second;
            m_output->writeString(k);
            m_output->writeData(v);
        }
    } else {
        // 容量足够，直接 append 加入
        // 写入4个字节总长度
        m_actualSize += itemSize;
        memcpy(m_ptr, &m_actualSize, 4);
        // 写入key
        m_output->writeString(key);
        // 写入value
        m_output->writeData(value);
    }
}

int32_t MMKV::getInt(std::string key, int32_t defaultValue) {
    auto iterator = m_dic.find(key);
    if (iterator != m_dic.end()) {
        ProtoBuf *buf = iterator->second;
        int32_t returnValue = buf->readInt();
        // 多次读取，将 position 还原为 0
        buf->restore();
        return returnValue;
    }
    return defaultValue;
}
