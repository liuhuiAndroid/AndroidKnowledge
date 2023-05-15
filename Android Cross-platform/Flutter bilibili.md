### 第1章

快捷键

```
快速创建 Widget：stf stl 
自动生成构造函数：option + 回车
添加父组件、变成子组件、删除子组件：option + 回车
```

自动补全插件：Flutter Snippets



### 第3章 Flutter网络和数据存储框架搭建





### 第6章 大首页模块开发

##### 6-1 本章目标

##### 6-2 首页接口系分与Dao层实现

JSON 转 Dart：https://www.devio.org/io/tools/json-to-dart/

##### 6-3 封装HiState处理页面状态异常

##### 6-4 轮播图Banner组件封装

##### 6-5 封装可自定义样式的沉浸式导航栏NavigationBar

##### 6-6 基于StaggeredGridView封装首页双Feed列表

##### 6-7 卡片组件封装与优化

##### 6-8 Flutter图片加载与缓存原理剖析

##### 6-9 列表图片缓存与加载优化实战

##### 6-10 上拉加载更多与分页功能实现

##### 6-11 基于Lottie实现全局加载组件封装



### 第7章 Flutter视频播放器组件封装

##### Flutter主流视频播放器

chewie pub 地址：https://pub.dev/packages/chewie

chewie Github 地址：https://github.com/fluttercommunity/chewie

| 播放器            | 类型                         | 说明                                                         | 特点                               |
| ----------------- | ---------------------------- | ------------------------------------------------------------ | ---------------------------------- |
| video_player      | 官方播放器                   | 支持Android、iOS和web的Flutter官方播放器；基于：ExoPlayer(Android)、 AVPlayer(iOS)、video_player_web(web) | 迭代更新及时、兼容性好、使用较复杂 |
| chewie            | 基于video_player封装的播放器 | 基于video_player的播放器插件                                 | 简洁易用、功能强大                 |
| better_player     | 基于video_player封装的播放器 | 基于video_player 与 Chewie的另一款视频播放器                 | 解决了一些特定场景下使用问题       |
| flutter_ijkplayer | 基于ijkplayer等方案的播放器  | 开发维护人员较少、大多是个人项目                             | 兼容性和迭代风险高                 |

##### Flutter主流视频播放器分析

##### Flutter视频播放器组件封装

##### 播放器源码分析与自定义播放器UI

##### 全屏播放与沉浸式播放功能实现

##### 利用应用生命周期变化进行体验优化

##### 封装HiTab组件实现Tab切换功能复用



### 第8章 视频详情模块开发

##### 视频详情头部模块组件封装与布局技巧

##### 基于Animation实现带动画的展开列表组件

##### 详情页接口系分与视频模型复用

##### 视频点赞分享收藏工具栏实现

##### 视频收藏接口系分与功能实现

##### 关联视频列表卡片设计与功能实现



### 第9章 排行榜模块开发

排行榜页面框架搭建

通用底层带分页和刷新的页面框架HiBaseTabState封装

排行榜模块接口系分与Dao层封装

基于HiBaseTabState实现页面快速搭建



### 第10章 个人中心模块开发

##### 个人中心模块接口系分与数据加载

##### 基于NestedScrollView与SliverAppBar实现复杂场景下的嵌套滚动

##### 高斯模糊与视差滚动效果实现

##### 高效的组件HiFlexibleHeader封装

##### 个人中心用户资产模块实现

##### 自定义动态布局实现职场进阶模块

##### 增值服务模块实现
