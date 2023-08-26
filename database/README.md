##### 数据库

【1】mysql 必知必会：https://blog.csdn.net/EPFL_Panda/article/details/100579485

【2】296-MySQL面试指南 中高级开发者的晋升加薪利器

【3】49-MySQL提升课程 全面讲解MySQL架构设计

【4】515-高并发 高性能 高可用 MySQL 实战：https://www.aliyundrive.com/s/J2bQgfNzsdu

DBA数据库工程师

332-详解企业主流数据库MySQL8.0

615-6大数据库，挖掘7种Java业务下的分布式存储更优解：https://www.aliyundrive.com/s/b9HmdgpPwGN

151-一站式学习Redis， 从入门到高可用分布式实践

467-高级Redis进阶课 解决Redis实际问题+掌握Redis6.x特性

324-玩转MongoDB4.0(最新版) 从入门到实践

##### docker 安装

按照官网步骤安装docker：[安装步骤]( https://docs.docker.com/install/linux/docker-ce/centos/ )

##### docker 安装mysql

```shell
# 启动docker
systemctl start docker
# 简单命令
docker pull xxx
# 交互式运行
docker run -it centos
# 重启container
docker restart `container id`
# 查看container ，默认只显示运行的
docker container ls
# 查看所有的container
docker container ls -a
docker container ls -a = docker ps -a
docker container rm `container id` = docker rm `container id`
# 执行container
docker run xxx/hello-world
# 删除container
docker container rm `container id` = docker rm `container id`
# 查看所有的container，只显示container id
docker container ls -aq
# 删除所有的container
docker rm $(docker container ls -aq)
# 查看所有的container，根据提供的条件过滤输出
docker container ls -f "status=exited" -q
# 删除所有已退出的container
docker rm $(docker container ls -f "status=exited" -q)
# 查看本地已经有的image
sudo docker image ls
# 查看最近30分钟的日志
docker logs --since 30m CONTAINER_ID
# 查看某时间之后的日志
docker logs -t --since="2019-08-02T13:23:37" CONTAINER_ID
# 删除容器
docker rm CONTAINER_ID
# Redis安装
docker run --name some-redis -d -p 6379:6379 redis redis-server --appendonly yes --requirepass "qwer1234"
# Nginx安装
docker run --name appointment-nginx -p 80:80 -d -v /root/dist:/usr/share/nginx/html nginx
# mysql 安装
docker pull mysql:8.1.0
docker run --name mysql8 -e MYSQL_ROOT_PASSWORD=mySt8#pW.sec4 -p 3306:3306 -d mysql:8.1.0 --character-set-server=utf8mb4

sudo docker run -p 3306:3306 --name test-mysql \
   -v /usr/local/docker/mysql/conf:/etc/mysql \
   -v /usr/local/docker/mysql/logs:/var/log/mysql \
   -v /usr/local/docker/mysql/data:/var/lib/mysql \
   -v /data/mysql/master/mysql-files:/var/lib/mysql-files \
   -e MYSQL_ROOT_PASSWORD=mySt8#pW.sec4 \
   --restart=unless-stopped \
   -d mysql:8.1.0 \
   --character-set-server=utf8mb4
  
# 字符集：utf8mb4	排序规则：utf8mb4_general_ci/utf8mb4_unicode_ci
# show variables like '%character%';
# show variables like '%collation%';
sudo docker run -p 3306:3306 --name test-mysql \
   -e MYSQL_ROOT_PASSWORD=mySt8#pW.sec4 \
   --restart=unless-stopped \
   -d mysql:8.1.0 \
   --character-set-server=utf8mb4
# 进入MySQL容器
docker exec -it jarvan-mysql /bin/bash
# 登录MySQL
mysql -u root -p
# Host 'ip地址' is not allowed to connect to this MySQL server
use mysql
select Host from user where User='root';
update user set Host='%' where User='root';
flush privileges;
# Authentication plugin 'caching_sha2_password' cannot be loaded
ALTER USER 'root' IDENTIFIED WITH mysql_native_password BY 'qwer1234';
```

-v：主机和容器的目录映射关系，前为主机目录，后为容器目录 

参考：[Docker 搭建 MySQL，MySQL 主从同步搭建及踩坑](https://blog.csdn.net/qq_37143673/article/details/94723044)

##### 解决mysql8+使用navicat连接乱码问题

   ```shell
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'mySt8#pW' PASSWORD EXPIRE NEVER; #修改加密规则 
   ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'mySt8#pW'; #更新一下用户的密码 
   FLUSH PRIVILEGES; #刷新权限 
   # --------------------------------------------------
   select user,plugin from user where user = 'root';
   delete from user where user = 'root' and plugin = 'caching_sha2_password'
   update user set host = '%' where user = 'root';
   FLUSH PRIVILEGES;
   ```

#### 备注

1. 远程连接工具： MobaXterm
2. Docker Desktop 设置页面可配置 Docker 镜像

#### mysql 基础命令

```shell
docker exec -it mysql /bin/bash
vim /etc/my.cnf
skip-grant-tables
systemctl start mysqld.service
update mysql.user set authentication_string=password('123456') where user='root';
flush privileges;
mysql -uroot -p123456
set global validate_password_policy=LOW;
set global validate_password_length=4;
set password=password('123456');
```







