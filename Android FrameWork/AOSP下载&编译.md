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

-----------------------

------------------------

[如何在 macOS 上安装虚拟机软件 VMware Fusion Player (个人版免费)](https://blog.csdn.net/wuyujin1997/article/details/128776714)

参考[VMware Fusion在M1上安装Ubuntu虚拟机](https://www.bilibili.com/video/av421832011)

Ubuntu arm版本镜像：https://ubuntu.com/download/server/arm

```shell
# 可以考虑换源
sudo apt-get update
sudo apt install ubuntu-desktop
reboot
# Ubuntu 安装 VMware Tools
sudo apt install open-vm-tools
sudo apt install open-vm-tools-desktop
```

```shell
# 这一段是准备工作，无所谓，Ubuntu最新版已经是python3
# 安装git: 
sudo apt install git
# 安装依赖工具：
sudo apt install git-core libssl-dev libffi-dev gnupg flex bison gperf build-essential zip curl zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 lib32ncurses5-dev x11proto-core-dev libx11-dev libz-dev ccache libgl1-mesa-dev libxml2-utils xsltproc unzip
# 下载Python3：
cd Downloads
wget https://www.python.org/ftp/python/3.7.1/Python-3.7.1.tgz
# 解压Python3：
tar xvf Python-3.7.1.tgz
# 编译与安装Python3:
./configure
sudo make install
# 配置update-alternatives：
sudo update-alternatives --install  /usr/bin/python python /usr/bin/python2.7 2
sudo update-alternatives --install  /usr/bin/python python python3的安装地址(/usr/local/bin/python3.7)  3(权重号)
# 选择Python版本：
sudo update-alternatives --config python
```

AOSP官方地址：https://source.android.google.cn/setup/build/downloading
中科大镜像：https://mirrors.ustc.edu.cn/help/aosp.html
清华镜像：https://mirrors.tuna.tsinghua.edu.cn/help/AOSP/

教程使用中科大初始同步方法 2安装：

```shell
mkdir ~/bin
cd ~/bin
PATH=~/bin:$PATH
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
mkdir ~/WORKING_DIRECTORY
cd ~/WORKING_DIRECTORY
repo init -u git://mirrors.ustc.edu.cn/aosp/platform/manifest
# 可以编译 Android 8
repo init -u git://mirrors.ustc.edu.cn/aosp/platform/manifest -b android-2.3.1_r1
# 使用清华源
repo init -u https://mirrors.tuna.tsinghua.edu.cn/git/AOSP/platform/manifest -b android-2.3.1_r1
repo init -u https://android.googlesource.com/platform/manifest -b android-2.3.1_r1
repo sync
```

#### 错误一

如果提示无法连接到 gerrit.googlesource.com，可以编辑 ~/bin/repo，把 REPO_URL 一行替换成下面的： REPO_URL = 'https://gerrit-googlesource.proxy.ustclug.org/git-repo'

#### 错误二

如果提示无法进行SSL证书校验，执行：

错误：error [SSL: CERTIFICATE_VERIFY_FAILED] certificate verify failed

```shell
git config --global http.sslverify false
git config --global https.sslverify false
```

#### 错误三

如果出现git身份不存在：

```shell
git config --global user.email "im.lh@hotmail.com"
git config --global user.name sec
```

#### 错误四

ubuntu /usr/bin/env: python : no such file or directory

```shell
whereis python3
sudo ln -s /usr/bin/python3 /usr/bin/python
```

-----------

------------

### 自定义ROM刷机实战

[wget 下载官方驱动](https://developers.google.cn/android/drivers)，解压，chmod +x ，运行 shell 脚本

```shell
make -j 4
adb devices
# 进入Bootloader模式：（解BL锁）
# https://source.android.google.cn/setup/build/running 查看如何解锁引导加载程序
# 可以淘宝购买已解锁的设备，或者去小米官网看如何OEM解锁
adb reboot bootloader
# 查看连接设备：
fastboot devices 
# 正式刷机
fastboot flashall -w
```

```shell
# 编译 Android 12 源码先设置交换空间再正常编译
# 配置环境
source build/envsetup.sh
# 选择要编译的选项：
lunch
# 执行编译
make -j 4
# 交换空间
swapon -s
# 停止交换空间
sudo swapoff /swapfile
sudo rm /swapfile
sudo fallocate -l 20G /swapfile
ls -l /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
free -m
```

