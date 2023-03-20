#ifndef MMKV_PROTOBUF_H
#define MMKV_PROTOBUF_H

#include <malloc.h>
#include <string>
#include <unordered_map>

// 解析协议
class ProtoBuf {
public:
    ProtoBuf(int32_t size);

    ProtoBuf(int8_t *buf, int32_t size, bool isCopy = false);

    ~ProtoBuf();

    static int32_t computeInt32Size(int32_t value);

    static int32_t computeItemSize(std::string key, ProtoBuf *value);

    static int32_t computeMapSize(std::unordered_map<std::string, ProtoBuf *> map);

    void restore();

public:
    std::string readString();

    ProtoBuf *readData();

    int32_t readInt();

public:
    void writeRawInt(int32_t value);

    void writeString(std::string value);

    void writeData(ProtoBuf *data);

public:
    bool isAtEnd() { return m_position == m_size; };

    int32_t length() const { return m_size; }

    // 空闲内存
    int32_t spaceLeft() {
        return m_size - m_position;
    }

    int8_t *getBuf() {
        return m_buf;
    }

private:
    void writeByte(int8_t value);

    int8_t readByte();

private:
    int8_t *m_buf;
    int32_t m_size;
    int32_t m_position;
    bool m_isCopy;
};

#endif //MMKV_PROTOBUF_H
