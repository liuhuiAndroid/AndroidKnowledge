SharePerferences
1. xml dom解析时间长，使用 protocol buffer 或者 哥伦布编码优化
2. FileOutPutStream 优化：共享内存 MMU
FileOutPutStream.write: IoBridge.write

1. 文件大小动态扩容
2. 重复的暂时不删，缓存写入的时候重置

protocol 有开源库