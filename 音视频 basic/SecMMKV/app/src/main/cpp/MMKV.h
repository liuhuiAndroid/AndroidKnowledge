#ifndef MANIUMMKV_MMKV_H
#define MANIUMMKV_MMKV_H
#include <malloc.h>
#include <string>
#include <unordered_map>
#include <sys/stat.h>
#include <sys/mman.h>
#include <unistd.h>
#include <fcntl.h>
#include "ProtoBuf.h"
static std::string g_rootDir;

#define DEFAULT_MMAP_ID "mmkv.default"

class MMKV {
public:
    MMKV(const char *mmapID);
    static void initializeMMKV(const char *path);
    static MMKV *defaultMMKV();
    void putInt(const std::string& key, int32_t value);
    int32_t getInt(std::string key, int32_t defaultValue);
private:
    void loadFromFile();
    void zeroFillFile(int fd, int32_t startPos, int32_t size);
    void appendDataWithKey(std::string key, ProtoBuf* value);
public:
    // 文件路径
    std::string m_path;
    // 文件句柄
    int m_fd;
    // 文件可写长度
    int32_t m_size;
    // 内存中数据
    int8_t *m_ptr;
    // 记录原始数据
    ProtoBuf *m_output;
    // 已经使用的长度
    int32_t m_actualSize = 0;
    // hashmap
    std::unordered_map<std::string, ProtoBuf*> m_dic;
};

#endif //MANIUMMKV_MMKV_H
