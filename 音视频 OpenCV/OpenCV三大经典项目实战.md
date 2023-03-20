# opencv-study
##### OpenCV搭建环境方法

- 命令安装方式

```shell
brew search python
brew install python@3.9
pip3 install numpy matplotlib opencv_python
# control + d ： 退出
# control + l ： 清屏
# control + command + q ： 锁屏
# https://pypi.org/project 可以直接下载pip3下载的安装包
python3 
> import numpy
> import matplotlib
> import cv2
```

- 源码安装方式

```shell
# 1.安装必要的库和工具
# 安装 python3 和 numpy 库
# 安装 cmake：https://github.com/Kitware/CMake
# 2.下载OpenCV的源码 
# OpenCV：https://github.com/opencv/opencv
# OpenCV-contribue：https://github.com/opencv/opencv_contrib
# 3.编译
# mkdir build
# 运行cmake，生成编译脚本
# 通过编译器进行编译
```

- 使用编译好的OpenCV库

```shell
# 创建C++工程文件
# 配置VS环境
# 编写测试代码进行测试
```

- OpenCV开发工具

```
pycharm
vscode：安装python插件
```

##### 车辆检测

- 窗口的展示

  ```shell
  # 查看OpenCV文档
  grep "namedWindow(" * -Rn ｜ grep "\.h"
  # 创建一个窗口并命名
  namedWindow()
  # 显示窗口
  imshow()
  # 销毁所有窗口
  destoryAllWindows()
  # 修改窗口大小
  resizeWindow()
  ```
  
- 图像/视频的加载

  ```shell
  imread(path, flag)
  # name: 要保存的文件名
  # img: mat类型
  imwrite(name, img)
  
  # 视频采集
  VideoCapure()
  # 返回两个值，第一个为状态值，读到帧为true
  cap.read()
  # 第二个值为视频帧
  cap.release()
  ```

- 基本图形的绘制

- 车辆识别

##### 图片文字识别

##### 图片拼接



#### OpenCV银行卡智能识别

1. 步骤：
   1. 截取到银行卡区域
   2. 截取到银行卡号区域
   3. 对银行卡号区域进行特征值分析提取识别
      1. 轮廓增强（梯度增强）
      2. 进行二值化轮廓过滤
      3. 截取卡号区域
2. OpenCV集成：https://www.jianshu.com/p/fe8dbb9f72ef
