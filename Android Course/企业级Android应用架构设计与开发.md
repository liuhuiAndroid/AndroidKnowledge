####  3-3 功能模块划分 

1. 宿主
2. 业务组件：首页业务、登录业务、搜索业务、音乐播放业务
3. 基础业务组件：lib_common_ui
4. 功能组件：lib_network、lib_image_loader、...

####  3-4 工程创建 

1. compileSdkVersion 28
2. gradle  5.4.1
3. com.android.tools.build:gradle:3.2.0

####  4-2 maven仓库

1. 中央仓库：mavenCenter、jCenter
2. 私服：局域网
   1. 优势一：节省自己的外网带宽
   2. 优势二：加速构建过程
   3. 优势三：部署第三方构件
   4. 优势四：提高稳定行，增强控制
   5. 优势五：降低中央仓库的负荷

####  4-3 maven私服搭建

1. nexus，下载地址： https://www.sonatype.com

2. 安装启动

   ```shell
   cd nexus-3.16.2/bin
   ./nexus start
   nexus.exe /run (windows)
   ```

3. 访问：localhost:8081 进入nexus首页，通过此平台创建maven仓库

4. 登录：账号admin 密码admin123

5. 创建 maven2(hosted) 私服 imooc-releases 和 imooc-snapshots 仓库

####  4-4 maven私服接入 

1. 为grade构建添加仓库

   ```
       repositories {
           jcenter()
           google()
           mavenCentral()
           maven {
               url 'http://localhost:8081/repository/imooc-releases/'
               credentials {
                   username 'admin'
                   password 'admin123'
               }
           }
           maven {
               url 'http://localhost:8081/repository/imooc-snapshots/'
               credentials {
                   username 'admin'
                   password 'admin123'
               }
           }
       }
   ```

2. 为Kotlin工程添加仓库同上

3. 为库工程配置上传代码

   ````
   apply plugin: 'maven'
   
   //上传maven配置
   uploadArchives {
       repositories {
           mavenDeployer {
               repository(url: NEXUS_REPOSITORY_URL) {
                   authentication(userName: NEXUS_USERNAME, password: NEXUS_PASSWORD)
               }
               pom.project {
                   name pomName
                   version pomVersionName
                   description pomDescription
                   artifactId pomVersionName
                   groupId POM_GROUPID
                   packaging POM_PACKAGING
               }
           }
       }
   }
   ````

####  4-5 工程结构优化

使用 imooc.gradle 优化 gradle 文件

####  6-3 okhttp封装思路讲解 

TODO 需要熟悉okhttp源码 

1. 封装Request
2. 封装OkhttpClient
3. 封装Response

####  15-2 arouter原理分析 

arouter 实现工程解耦

1.  arouter接入
2. 项目路由改造 
3. 项目服务化改造 

####  16-3 实战工程迁移androidx 