[Android App包瘦身优化实践 - 美团技术团队](https://tech.meituan.com/2017/04/07/android-shrink-overall-solution.html)

[安装包立减1M–微信Android资源混淆打包工具](http://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=208135658&idx=1&sn=ac9bd6b4927e9e82f9fa14e396183a8f#rd)

体积精简：

- 业务优化
  - 业务拆分
    - 业务更细粒度拆分，组件化，按需接入
  - 功能裁剪：
    - 简化现有业务流程，低频功能删除
- 技术优化
  - 资源压缩：
    - 使用工具检查冗余资源并删除；
    - 只保留特定屏幕密度的图片资源；
    - 开启 shrinkResources，移除未使用资源
    - 图片使用 webp 格式
  - 编码算法升级：
    - 图片编码算法升级；
    - 语言编码算法升级；
  - 代码压缩：
    - 公共代码抽取复用；
    - 无用代码清理； 
    - 枚举替换成 @IntDef
    - 开启 minifyEnabled，app 混淆时会删除未使用的代码
    - 开启混淆工具 R8 的 Full Mode，进一步压缩代码
  - 编译优化
    - 符号表裁剪
    - 多个动态库合并为一个
    - 使用redex工具优化压缩dex文件
  - SDK库压缩
    - 自研及第三方SDK体积压缩优化
