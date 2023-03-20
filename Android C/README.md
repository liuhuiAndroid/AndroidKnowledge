#### VSCode

```shell
env | grep PATH
```

1. 安装VSCode插件

2. 配置C/C++环境

   /Library/Developer/CommandLineTools/SDKs/MacOSX11.1.sdk/usr/include

3. 调试C/C++步骤

   1. 创建编译任务
      1. 编译成可执行文件
         1. Terminal 终端
         2. Configure Tasks 配置任务
         3. C/C++: clang 生成活动文件
         4. Terminal 终端
         5. Run Task 运行任务
      2. 点击Run and Debug
         1. Show all automatic debug configurations 显示所有自动调试配置
         2. Add Configurations
         3. 选择C/C++: (lldb) 启动
         4. 修改 "program": "${workspaceFolder}/${fileBasenameNoExtension}",
         5. C/C++:clang 生产和调试活动文件

#### 开发环境

1. CentOS 7.x

   ```shell
   # 124.222.24.122	Seckill44-
   ssh root@124.222.24.122
   # 参考：使用VScode连接远程服务器进行开发：https://zhuanlan.zhihu.com/p/141205262
   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
   642404044@qq.com
   ```

2. MySQL 5.7

3. /project

   1. public 开发框架
   2. tools 通用功能模块
   3. idc 数据中心项目模块
   4. pthread linux线程demo程序

#### 开发永不停机的服务程序

- 用守护进程监听服务程序的运行状态

- 如果服务程序故障，调度进程将重启服务程序

- 保证系统7x24小时不间断运行




#### Makefile

1. C++编译太慢了，所以需要尽可能提升性能。
   Makefile解决编译依赖按需编译加上多线程编译可以提升编译性能。
2. 书籍：Makefile参考手册.pdf
3. 书籍：跟我一起写Makefile_陈皓.doc

#### 代理

参考：https://juejin.cn/post/7198446470394134586

```shell
ssh root@124.222.24.122
wget https://github.com/Dreamacro/clash/releases/download/v1.2.0/clash-linux-amd64-v1.2.0.gz
sftp root@124.222.24.122
# Seckill44-
put /Users/sec/Downloads/clash-linux-amd64-v1.13.0 /opt/clash/
chmod +x clash-linux-amd64-v1.13.0
mv clash-linux-amd64-v1.13.0 clash
cd /opt/clash/
./clash -f ~/.config/clash/config.yaml
systemctl start clash
systemctl status clash
systemctl enable clash
systemctl disable clash

./clash -d ~/.config/clash
----------------------------------------------------------
vi /etc/profile
export https_proxy=http://127.0.0.1:7890 http_proxy=http://127.0.0.1:7890 all_proxy=socks5://127.0.0.1:7891
source /etc/profile
ping www.github.com
```



#### 开发基于ftp协议的文件传输子系统

熟悉 ftp 命令

ftp 增量下载文件



#### 开发基于tcp协议的文件传输子系统

TCP粘包和分包原理

