官方文档：https://source.android.google.cn/setup/start

清华源：https://mirrors.tuna.tsinghua.edu.cn/help/AOSP/

参考：https://www.jianshu.com/p/a7b90c3b5c76

1. 安装 Repo

   ```shell
   brew install repo
   ```

2. 使用每月更新的初始化包

   ```shell
   wget -c https://mirrors.tuna.tsinghua.edu.cn/aosp-monthly/aosp-latest.tar # 下载初始化包
   tar xf aosp-latest.tar
   cd AOSP   # 解压得到的 AOSP 工程目录
   # 这时 ls 的话什么也看不到，因为只有一个隐藏的 .repo 目录
   repo init -u https://aosp.tuna.tsinghua.edu.cn/platform/manifest -b android-12.0.0_r28
   # 罗升阳 android-2.3_r1
   repo sync # 正常同步一遍即可得到完整目录
   # 或 repo sync -l 仅checkout代码
   ```
   
3. 编译失败～