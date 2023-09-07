### 第2章 初识Nacos

##### Nacos介绍与发展前景

官网：https://nacos.io/zh-cn/

Nacao能干什么？

- 注册微服务，将服务注册到管控中心
- 发现微服务，将服务暴露给其他服务
- 管理参数，提供配置参数池

##### Eureka与Nacos的江湖地位之争

- Eureka的使用范围逐渐缩小
  - 不在维护
- Nacos
  - Nacos逐渐被国内互联网大厂使用
  - Eureka已经被国内互联网大厂停止使用，均移步Nacos

##### Nacos不同版本特性介绍

##### 在Linux、Mac上安装Nacos

- 下载 Nacos For Linux/Mac
- 安装 Nacos For Linux/Mac

```shell
mkdir nacos-server
cd nacos-server
tar -zxvf nacos-server-2.1.1.tar.gz
cd nacos/bin
./startup.sh -m standalone
# 管控台默认账号密码 nacos nacos localhost:8848/nacos/index.html
```

##### 在Windows上安装Nacos

### 第3章 Nacos基础核心特性揭秘

##### 浅谈Nacos架构

Nacos 地图：特性大图、架构大图、业务大图、生态大图、优势大图、战略大图

##### Nacos特性--服务注册

- 用户发出服务注册请求
- Nacos 接收到该服务注册请求
- Nacos 构造服务元数据

##### Nacos特性--服务发现

- Nacos 云平台自我检测服务列表
- Nacos 内部对外暴露服务列表
- Nacos 启动服务间通信机制
- Nacos 启动服务间健康检查机制
- Nacos 维护服务元数据

##### Nacos特性--发布与获取配置

- 什么是配置发布

  将一个或多个服务所需的配置参数通过一定的技术手段，进行统一发布，使其生效或不生效的过程。

- 什么是配置获取

  通过一定的技术手段，从统一的配置管理中心获取一个或多个服务配置参数，并进行自我校验的过程。

- 步骤

  - 用户发起配置发布请求
  - Nacos 配置监听程序捕获该请求
  - Nacos 对该请求进行解析
  - Nacos 识别配置文件格式、参数
  - Nacos 与具体服务建立通信
  - Nacos 更新具体服务配置参数

##### Nacos Spring关键特性

### 第4章 Nacos与Spring的不解之缘

##### Nacos整合Spring Boot流程梳理

1. 添加依赖

2. 配置 Nacos Server Address

   yml/properties 文件中配置：

   ```properties
   nacos.discovery.server-addr=127.0.0.1:8848
   ```

3. 注入 Nacos 的 NamingService 实例

4. 服务检测

   启动 NacosDiscoveryApplication 调用 curl http://localhost:8080/discovery/get?serviceName=example，此时返回为空

   通过调用 Nacos Open API 向 Nacos server 注册一个名称为 example 服务

   curl -X POST 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=example&ip=127.0.0.1&port=8080'

5. 服务验证

   再次访问 curl http://localhost:8080/discovery/get?serviceName=example

SpringBoot 整合 Nacos 配置管理

1. 添加依赖

   com.alibaba.boot:nacos-config-spring-boot-starter

2. 配置 Nacos Server Address

   ```properties
   nacos.config.server-addr=127.0.0.1:8848
   ```

3. 加载示例配置源

4. 添加示例配置属性

5. 测试配置属性

   启动 NacosConfigApplication

   调用  curl http://localhost:8080/config/get，返回内容是 false

   向 Nacos server 发布配置：dataId 为 example，内容为 useLocalCache=true

   curl -X POST 'http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=useLocalCache=true'

##### Nacos整合Spring Boot流程梳理——代码实操

1. 构建 Nacos 基础项目环境

   创建基于 SpringBoot 框架的 Nacos 项目环境

   注意：在配置时采用的 Nacos 版本为 0.2.X，因为0.2.X版本对应 SpringBoot 2.X 版本

2. 启动 Nacos-Server 端

   在任意平台启动 Nacos-Server

##### Spring Cloud整合Nacos流程梳理

##### Nacos整合Spring Cloud——代码实操

##### 如何从Eureka过渡到Nacos

1. 从大方向入手

   以项目整体为角度，思考项目本身是否可以过渡到 Nacos

   即：项目技术架构是否支持 Nacos

   如果使用 Nacos 会为项目本身带来什么优势

   在更换 Nacos 时，如何保障项目正常运行

2. 从小方向进行演进

   确定更换 Nacos 时到最小粒度节点

   即：确定更换 Nacos 时所花费的人力与时间成本

   确定需要更换 Nacos 的服务范围，通常是项目整体

   确定配置文件是否可以平稳迁移，若需改动配置难易度如何

3. 从运维方向确保平稳过渡

   保障演进为 Nacos 后的过渡工作

   即：考虑 Eureka 是否需要与 Nacos 平行运行一段时间

   监测演进为 Nacos 后的项目整体性能指标是否符合预期

   制定演进为 Nacos 后的项目整体运维方案
