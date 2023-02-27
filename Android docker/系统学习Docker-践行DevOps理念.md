###一
######到底什么是Docker？
Docker是容器技术的一种实现
######Docker能干什么？
简化配置
 整合服务器
代码流水线管理
调适能力
提高开发效率
多租户
隔离应用
快速部署
######Docker Kubernetes
######DevOps = 文化 + 过程 + 工具
######虚拟化技术和容器技术的比较
######容器解决了什么问题？
- 解决了开发和运维之间的矛盾
- 在开发和运维之间搭建了一个桥梁，是事先devops的最佳解决方案
######什么是容器？
- 对软件和其依赖的标准化打包
- 应用之间相互隔离
- 共享同一个OS Kernel
- 可以运行在很多主流操作系统上
######容器和虚拟机的区别
- 容器是APP层面的隔离
- 虚拟化是物理资源层面的隔离
 ######演示——安装WordPress
TODO
- 需要一个docker-compose.yml文件
- more docker-compose.yml
- docker-compose build
- docker-compose up

###二
######在Windows系统上安装Docker
######Vagrant基本使用
创建虚拟机的方法：
安装VirtualBox软件，通过Vagrant创建linux虚拟机
```
mkdir Vagerant创建centos7目录
cd Vagerant
mkdir centos7
cd centos7
vagrant init centos/7 创建vagrantfile
vagrant up
vagrant ssh
sudo yum update

vagrant status
vagrant halt 停掉机器
vagrant destroy 删除机器
```
######CentOS上安装docker
安装方法见官网
```
Uninstall old versions
Install required packages
Use the following command to set up the stable repository
INSTALL DOCKER CE
Start Docker : sudo systemctl start docker
sudo docker version
Verify that docker is installed correctly by running the hello-world image.
```
######通过Docker Machine安装docker
```
会创建一台安装好docker的虚拟机
docker-machine create demo
docker-machine ls
docker-machine ssh demo
```
######Docker Machine在阿里云上的使用
[docker-machine-driver-aliyunecs](https://github.com/AliyunContainerService/docker-machine-driver-aliyunecs)
######Play with Docker 
###三
######Docker的架构和底层技术
Docker Platform
- Docker提供了一个开发，打包，运行app的平台
- 把app和底层infrastructure（物理设备）隔离开来

Docker Engine
- 后台进程（dockerd）
- REST API Server
- CLI接口（docker）
```
sudo docker version
ps -ef | grep docker
```
Docker Architecture
Docker 底层技术支持
- Namespaces:做隔离pid,net,ipc,mnt,uts
- Control groups:做资源限制
- Union file systems:Container和image的分层

chapter3下面有一个Vagrantfile可以帮助我们快速搭建docker环境，
chapter3源码在docker-k8s-devops下面
我们可以在自己的机器里面安装git然后clone源代码
```
sudo yum install -y git vim gcc glibc-static telnet
```
######Docker Image概述
什么是Image
- 文件和meta data的集合（root filesystem）
- 分层的，并且每一层都可以添加改变删除文件，成为一个新的image
- 不同的image可以共享相同的layer
- Image本身是read-only的
```
查看本地已经有的image
sudo docker image ls
```
Image的获取第一种方式
- Build from Dockerfile
```
more Dockerfile
docker build -t lh/redis:latest . 
```
Image的获取第二种方式
- Pull from Registry
Docker Hub是Docker为我们提供的一个公开的免费的Registry
```
sudo docker pull ubuntu:14.04
```
######如何制作Base Image
```
去除sudo的方法：
sudo groupadd docker
sudo gpasswd -a vagrant docker
sudo service docker restart
exit 
vagrant ssh
```
通过Dockerfile构建Base Image
```
docker pull hello-world
docker run hello-world
```
通过C语言编写一个helloworld，把helloworld编译执行成为一个二进制可执行文件
```
mkdir hello-world
cd hello-world
vim hello.c

#include<stdio.h>
int main(){
  printf("hello docker\n");
}

gcc -static hello.c -o hello
./hello
vim Dockerfile

FROM scratch
ADD hello /
CMD ["/hello"]

docker build -t lh/hello-world .
docker image ls = docker images
docker history `image-id`
执行container
docker run lh/hello-world
docker image rm `image-id` = docker rmi `image-id` 
```
######初识Container
什么是Container
- 通过Image创建（copy）
- 在Image layer 之上建立一个container layer（可读写）
- 类比面向对象：类和实例
- Image负责app的存储和分发，Container负责运行app
```
docker run lh/hello-world
docker container ls
docker container ls -a
docker pull centos
交互式运行
docker run -it centos
touch test.txt
yum install vim 
exit
docker container ls -a = docker ps -a
docker container rm `container id` = docker rm `container id`

删除所有的container
docker container ls -aq
docker rm $(docker container ls -aq)
docker container ls -f "status=exited" -q
docker rm $(docker container ls -f "status=exited" -q)
```
######构建自己的Docker镜像
```
docker container commit = docker commit
docker image build = docker build
```
```
通过docker commit，基于一个已经存在的container创建一个docker image，不提倡
docker image ls 
docker run -it centos
yum install vim -y
exit
docker container ls -a
docker commit centos `NAMES` 'NEW_NAME etc:lh/centos-vim' ?
docker image ls
docker history `centos imageid`
docker history `centos-vim imageid`
```
```
通过Dockerfile build一个image,建议使用这种方式
删除之前的image
docker image ls
docker image rm `image id`
mkdir docker-centos-vim
cd docker-centos-vim
vim Dockerfile 

FROM centos
RUN yum install -y vim

docker build -t lh/centos-vim-new .
docker image ls
```
######Dockerfile语法梳理及最佳实践
FROM关键字制定base image
```
FROM scratch # 制作base image
FROM centos # 使用base image
FROM ubuntu:14.04
```
FROM：尽量使用官方的image作为base image，原因是为了安全

LABEL
```
LABEL maintainer="lh@gmail.com" # 作者
LABEL version="1.0" # 版本
LABEL description="This is description" # 描述
```
LABEL：Metadata不可少

RUN
```
RUN yum update && yum install -y vim \
python-dev # 反斜线换行
RUN apt-get update  ... # 注意清理cache
RUN /bin/bash -c 'source $HOME/.bashrc;echo $HOME'
```
RUN：为了美观，复杂的RUN请用反斜线换行！避免无用分层，合并多条命令成一行！

WORKDIR 
设定当前工作目录
```
WORKDIR /root
--------------------------
WORKDIR /test # 如果没有会自动创建test目录
WORKDIR demo
RUN pwd # 输出结果应该是/test/demo
```
WORKDIR：用WORKDIR，不要用RUN cd！尽量使用绝对目录！

ADD and COPY
```
ADD hello /
------------------------
ADD test.tar.gz / # 添加到根目录并解压
-------------------------
WORKDIR /root
ADD hello test/ # /root/test/hello
-------------------------
WORKDIR /root
COPY hello test/
```
ADD or COPY 大部分情况，COPY优于ADD！ADD除了COPY还有额外功能（解压）！添加远程文件/目录请使用curl或者wget！

ENV
```
ENV MYSQL_VERSION 5.6 # 设置常量
RUN apt-get install -y mysql-server = "${MYSQL_VERSION}" \
  && rm -rf /var/lib/apt/lists/* #引用常量
```
ENV：尽量使用ENV增加可维护性！

VOLUME and EXPOSE
（存储和网络）后面再说

CMD and ENTRYPOINT
后面再说
######[Dockerfile reference 详细的描述了Dockerfile的语法](https://docs.docker.com/engine/reference/builder/)

######RUN vs CMD vs ENTRYPOINT
RUN：执行命令并创建新的Image Layer
CMD：设置容器启动后默认执行的命令和参数
ENTRYPOINT：设置容器启动时运行的命令

Shell和Exec格式
- Shell格式
```
RUN apt-get install -y vim
CMD echo "hello docker"
ENTRYPOINT echo "hello docker"
```
- Exec格式
```
RUN ["apt-get","install","-y","vim"]
CMD ["/bin/echo"."hello docker"]
ENTRYPOINT ["/bin/echo","hello docker"]
```
- Dockerfile1
```
FROM centos
ENV name Docker
ENTRYPOINT echo "hello $name"
```
- Dockerfile2
```
FROM centos
ENV name Docker
ENTRYPOINT ["/bin/bash","-c","echo hello $name"]
```
```
vim Dockerfile
write Dockerfile1/Dockerfile2 content
more Dockerfile
docker build -t lh/centos-entrypoint-shell .
docker image ls
docker run lh/centos-entrypoint-shell
两次都输出hello docker
```

CMD
- 容器启动时默认执行的命令
- 如果docker run指定了其他的命令，CMD命令被忽略
- 如果定义了多个CMD，只有最后一个会执行
```
FROM centos
ENV name Docker
CMD echo "hello $name"

docker run [image]输出hello docker
docker run -it [image] /bin/bash 输出空
```

ENTRYPOINT
- 让容器以应用程序或者服务的形式运行
- 不会被忽略，一定会执行
- 最佳实践：写一个shell脚本作为entrypoint
```
COPY docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]

EXPOSE 27017
CMD ["mongod"]
```
```
FROM centos
ENV name Docker
CMD echo "hello $name"
```
```
vim Dockerfile
docker build -t lh/centos-cmd-shell .
docker image ls
docker run lh/centos-cmd-shell
docker run -it lh/centos-cmd-shell /bin/bash
docker run lh/centos-entrypoint-shell
docker run -it lh/centos-entrypoint-shell /bin/bash
```

######镜像的发布
把本地自己创建的docker image push到docker hub
```
vim Dockerfile

FROM scratch
ADD hello /
CMD ["/hello"]

注意tag一定是我们的docker hub id
docker login 输入用户名密码
docker image push lh/hello-world:latest
docker image rm lh/hello-world
docker pull lh/hello-world
docker image ls
```
```
分享Dockerfile
Create Automated Build
```
```
docker registry image 可以搭建私有的docker hub
启动私有的registry
docker run -d -p 5000:5000 --restart always --name registry registry:2
docker ps 查看registry状态
另一台机器telnet 10.10.44.44:5000看是否能够访问
如何push image
docker image rm `image id`
docker build -t 10.10.44.44:5000/hello-world . 
docker image ls 
sudo ls /etc/docker
sudo more /etc/docker/daemon.json
vim  /etc/docker/daemon.json
写入{"insecure-registries":["10.75.44.222:5000"]}
sudo vim /lib/systemd/system/docker.service
添加EnvironmentFile=-/etx/docker/daemon.json
sudo service docker restart
docker push 10.10.44.44:5000/hello-world

通过docker registry api查看是否push成功
10.75.44.222:5000/v2/_catalog可以查看

docker image ls 
docker image rm `image id`
docker pull 10.10.44.44:5000/hello-world
docker image ls 
```
######Dockerfile实战
任务：把python app打包成docker image,然后把这个image运行起来——常驻内存的程序
```
more app.py  # 代码见github
sudo yum install python-setuptools
sudo easy_install pip # 安装pip
sudo pip install flask
python app.py

mkdir flask-hello-world
cd flask-hello-world
vim app.py

from flask import Flask
app = Flask(__name__)
@app.route('/')
def hello():
    return "hello docker"
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)

vim Dockerfile

FROM python:2.7
LABEL maintainer="Peng Xiao<xiaoquwl@gmail.com>"
RUN pip install flask
COPY app.py /app/
WORKDIR /app
EXPOSE 5000
CMD ["python", "app.py"]

docker build -t lh/flask-hello-world . 
docker run -it `image id` /bin/bash  # 进入中间状态的image
docker image ls
docker run lh/flask-hello-world
docker run -d lh/flask-hello-world # 后台执行
docker ps
```
######容器的操作
```
进入运行中的容器
docker exec -it `container id` /bin/bash
ps -ef | grep python
exit
docker exec -it `container id` python
print "hello docker"
exit()
打印运行中的容器的ip地址
docker exec -it `container id` ip a
docker container stop `container id`
docker ps -a
docker rm $(docker ps -aq)
docker run -d --name=demo lh/flask-hello-world
docker ps
docker stop demo
docker start demo

显示container详细信息
docker inspect `container id`
显示container运行产生的输出
docker logs `container id`
```
######Dockerfile实战2
安装stress，使用stress
```
docker run -it ubuntu
apt-get update && apt-get install -y stress
which stress
stress --help
stress --vm 1
stress --vm 1 --verbose
stress --vm 1 --vm-bytes 500000M --verbose
exit
top
```
把stress打包成为一个docker image ——命令行工具
```
ubuntu-stress源码文件夹中，源码见github
vim Dockerfile

FROM ubuntu
RUN apt-get update && apt-get install -y stress
ENTRYPOINT ["/usr/bin/stress"]
CMD []

docker build -t lh/ubuntu-stress . 
docker run -it lh/ubuntu-stress
指定参数
docker run -it lh/ubuntu-stress --vm 1 --verbose
```
###四
######Docker network
单机 Bridge Network、 Host Network、 None Network
多机 Overlay Network
```
chapter4 Vagrantfile 定义了两台centos的机器
cd chapter4
vagrant up  # 可以创建两台docker host
vagrant status
vagrant ssh docker-node1 # 进入第一台主机里
docker version
ip a
ping 192.168.205.11 # 两台机器之间是可以ping通的
所有源码在labs内

我们也可以通过docker-machine create两台centos
```
######基础网络概念
基于数据包的通信方式
路由的概念
IP地址和路由
公有AP和私有IP
- Public IP：互联网上的唯一标识，可以访问internet
- Private IP：不可在互联网上使用，仅供机构内部使用
网络地址转换NAT
Ping和telnet
- Ping（ICMP）：验证IP的可达性
- telnet：验证服务的可用性
抓包工具wireshark

######Linux网络命名空间
Linux Network Namespace
```
docker run -d --name test1 busybox /bin/sh -c "while true;do sleep 3600; done"
docker ps
交互式进入到容器内部
docker exec -it `container id` /bin/sh
ip a 
exit
ip a
docker run -d --name test2 busybox /bin/sh -c "while true;do sleep 3600; done"
docker ps
docker exec `container id` ip a
进入到第二个容器发现是可以ping通第一个容器的

如何创建和删除linux的network namespace
sudo ip netns list # 查看本机network namespace
sudo ip netns delete test1 # 删除本机network namespace
sudo ip netns add test1 # 创建本机network namespace
sudo ip netns exec test1 ip a # 在test1这个network namespace中执行ip a这个命令
sudo ip netns exec test1 ip link
sudo ip netns exec test1 ip link set dev lo up # 让lo这个端口up起来
sudo ip netns exec test1 ip link

需求，自己配置Linux Network Namespace为两台机器创建eth，使两台机器联通
宿主机执行：sudo ip link add veth-test1 type veth peer name veth-test2
ip link # 可以看到多了一对eth接口
sudo ip netns exec test1 ip link
sudo ip link set veth-test1 netns test1
sudo ip netns exec test1 ip link
ip link
sudo ip link set veth-test2 netns test2
sudo ip netns exec test1 ip link
ip link
# 分别给两个端口配置ip地址
sudo ip netns exec test1 ip addr add 192.168.1.1/24 dev veth-test1
sudo ip netns exec test2 ip addr add 192.168.1.2/24 dev veth-test2
sudo ip netns exec test1 ip link
sudo ip netns exec test2 ip link
# 启动两个端口
sudo ip netns exec test1 ip link set dev veth-test1 up
sudo ip netns exec test2 ip link set dev veth-test2 up
sudo ip netns exec test1 ip link
sudo ip netns exec test2 ip link
sudo ip netns exec test1 ip a
sudo ip netns exec test2 ip a
# 演示两台机器相互ping通
sudo ip netns exec test1 ip a
sudo ip netns exec test1 ping 192.168.1.2
sudo ip netns exec test2 ping 192.168.1.1
```
######Docker Bridge详解
Bridge Network
```
# 容器之间是如何互相访问？
sudo docker exec -it test1 ip a
sudo docker exec -it test2 ip a
sudo docker exec -it test1 /bin/sh
ping 172.17.0.2 # 在test1里面ping test2
ping www.baidu.com # 在container里面ping internet
sudo docker stop test2 # 删除test2，只保留test1看网络情况
sudo docker rm test2
sudo docker network ls # 可以看到bridge网络
sudo docker ps
sudo docker network inspect `bridge network id` # 可以看到containers里面有test1,说明test1这个container连接到我们bridge这个网络上
ip a # 一对veth连接docker和container，其中一个veth在test1上
sudo docker ps # 
sudo docker exec test1 ip a # 可以看到一个eth0和外面的veth是一对，都连接到docker0上面

# 验证个eth0和veth都连接到docker0上面
sudo yum install -y bridge-utils
brctl show # 可以看到linux上只有一个bridge叫做docker0,docker0上有一个接口见interfaces
ip a # 可以看到这个接口，证明这个接口是连到了linux bridge上了
docker run -d --name test2 busybox /bin/sh -c "while true;do sleep 3600; done"
sudo docker network inspect `bridge network id` # containers 多了一个container
ip a # 多了一个veth
brctl show # 可以看到docker0连了两个接口了
相当于一个局域网

#当个容器是怎么访问internet？
容器内的数据包经过docker0通过NAT网络地址转换成eth0的地址，作为linux主机的数据包访问外网
```
![image.png](https://upload-images.jianshu.io/upload_images/1956963-ab9f435d3313f29c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

######容器之间的link
什么是link?
container通过name相互访问
```
sudo docker ps # 可以看到上次的两个busybox test1 test2
sudo docker stop test2 # 删除test2
sudo docker rm test2
sudo docker ps # 现在只有一个容器叫做test1
sudo docker run -d --name test2 --link test1 busybox /bin/sh -c "while true;do sleep 3600; done"
# 注意多了--link test1
sudo docker exec -it test2 /bin/sh # 进入到test2内
ping 172.17.0.3 # 可以ping通test1
ping test1 # 也是可以ping通
# 如果test1上有mysql 在test2就可以通过test1:3306访问了

sudo docker exec -it test1 /bin/sh
ping 172.17.0.2 # 可以
ping test2 # 不行
# link是有方向的

sudo docker stop test2
sudo docker rm test2
sudo docker run -d --name test2 busybox /bin/sh -c "while true;do sleep 3600; done"
# 回到最原始的状态
sudo docker network ls
# 创建容器时可以指定network 不适用bridge

# 新建network bridge,让container连接到我们自己的network bridge
sudo docker network create -d bridge my-bridge
sudo docker network ls
brctl show
sudo docker run -d --name test3 --network my-bridge busybox /bin/sh -c "while true;do sleep 3600; done"
#  --network my-bridge
sudo docker ps
brctl show
sudo docker network inspect `my-bridge network id` # 可以看到test3

#如何使test1、test2连接到my-bridge
sudo docker network connect my-bridge test2
sudo docker network inspect `my-bridge network id` # 可以看到test2
sudo docker network inspect `default-bridge network id`  # 也看到test2
sudo docker exec -it test3 /bin/sh
ping `test2 ipaddress` # 可以
ping test2 # 可以，原因是test2、test3都连接到我们新建的bridge上而不是默认的
sudo docker exec -it test2 /bin/sh
ip a
ping test3 # 可以
ping test1 # 不可以
# 把test1连接到my-bridge
sudo docker network connect my-bridge test1
sudo docker exec -it test2 /bin/sh
ping test1 # test2 ping test1 # 可以
```
 
######容器的端口映射
```
sudo docker run --name web -d nginx
sudo docker ps
# 无法访问nginx
sudo docker exec -it web /bin.bash # 退出把
sudo docker network inspect bridge # 查看bridge ip地址172.17.0.2
ping 172.17.0.2 # 在linux ping可以，因为docker0
telnet 172.17.0.2 80 # 可以
curl http://172.17.0.2 # 可以拉到nginx的html页面
# 通过端口映射实现在外面也可以访问 想映射到本机的80端口
curl 127.0.0.1
sudo docker stop web # 停掉nginx
sudo docker rm web
sudo docker run --name web -d -p 80:80 nginx # 容器的80端口映射到本地的80端口
sudo docker ps
curl 127.0.0.1 # 成功
curl 公网ip

# 在vagrant的Vagrantfile中设置forwarded_port 
config.vm.network "forwarded_port", guest:80, host: opts[port] 具体见github代码
ping 本机ip可以访问nginx
```
![image.png](https://upload-images.jianshu.io/upload_images/1956963-07bba2d4e416fded.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

######容器网络之host和none
none network
```
sudo docker run -d --name test1 --network none busybox /bin/sh -c "while true;do sleep 3600; done"
sudo docker ps
sudo docker network inspect none # test1没有mac地址、没有ip地址
sudo docker exec -it test1 ip a # 除了本地回环口，没有其他的接口

docker stop test1
docker rm test1
sudo docker run -d --name test1 --network host busybox /bin/sh -c "while true;do sleep 3600; done"
docker network inspect host
docker exec -it test1 ip a # 和linux主机共享一套network namespace,可能会有端口冲突
# 可以模拟两个nginx都绑定到80端口试一试
```
######多容器复杂应用的部署
```
源码见chapter4 flask-redis
cd flask-redis
# 把flask作为一个container部署，redis单独作为一个container部署
# 首先创建一个redis container
docker run -d --name redis redis # redis供内部访问，没必要把6379暴露出来
docker ps
docker build -t seckill44/flask-redis . 
docker image ls
docker run -d --link redis --name flask-redis -e REDIS_HOST=redis seckill44/flask-redis  # -d 是后台执行  -e 是设置环境变量
docker exec -it flask-redis /bin/bash
env # 环境变量
ping redis
curl 127.0.0.1:5000
exit
docker ps
curl 127.0.0.1:5000 # 不可以
docker stop flask-redis
docker rm flask-redis
docker run -d -p 5000:5000 --link redis --name flask-redis -e REDIS_HOST=redis seckill44/flask-redis
curl 127.0.0.1:5000 # 可以
```
![image.png](https://upload-images.jianshu.io/upload_images/1956963-948f86c58b135437.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/1956963-d4aee38effcd46fd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

######多机器通信
![image.png](https://upload-images.jianshu.io/upload_images/1956963-89ce672d09f9f55c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
VXLAN 方式传输
```
# 搭建实验环境
通过character4 vagrant 创建两台docker host
vagrant ssh docker-node1
ip a # 地址是192.168.205.10
vagrant ssh docker-node1
ip a # 地址是192.168.205.11 # 相互之间可以ping通
```
######docker overlay网络和etcd实现多机器通信
```
# 实验在两台docker host 中不同的container进行通信
# 分布式存储etcd
# 首先搭建etcd cluster
# 源码multi-host-network.md有实验步骤
# node1中：
cd etcd-v3.0.12-linux-amd64
lls 
nohup ./etcd --name docker-node1 --initial-advertise-peer-urls http://192.168.205.10:2380 ........
# 同样在node2中执行

./etcdctl cluster-health
./etcdctl cluster-health
具体见文档。。。
github docker labs 有个 overlay-network讲解
```
![image.png](https://upload-images.jianshu.io/upload_images/1956963-f574f6b3a8ffd736.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 五
######Docker的持久化存储和数据共享
Container Layer
Container 是在Image之上创建的，它是运行层
Container 可以写数据 Image 只读
Container 只能临时保存数据
Data Volume
Docker持久化数据的方案
- 基于本地文件系统的Volume。可以在执行Docker create或Docker run时，通过-v参数将主机的目录作为容器的数据卷。这部分功能便是基于本地文件系统的volume管理
- 基于plugin的Volume，支持第三方的存储方案，比如NAS,AWS

Volume的类型
- 受管理的data Volume，由docker后台自动创建。
- 绑定挂载的Volume，具体挂载位置可以由用户指定。

######实验环境介绍
使用chapter5的Vagrantfile，也可以使用chapter4 主机
```
vagrant plugin list
# vagrant-scp 可以从本地来拷贝文件到docker内
vagrant plugin install vagrant-scp
vagrant ssh docker-node1
ls 
cd labs
exit
vagrant scp ../chapter5/labs/ docker-node1:/home/vagrant/labs/
vagrant ssh docker-node1 # 可以看到文件
```

######数据持久化：Data Volume
```
docker run -d --name mysql1 -e MYSQL_ALLOW_EMPTY_PASSWORD mysql
docker ps
docker logs mysql1
docker rm mysql1
docker volume ls
docker volume rm `volume name`
docker run -d --name mysql1 -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql
docker ps
docker volume ls
docker volume inspect `volume name`
docker run -d --name mysql2 -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql
docker volume ls
docker volume inspect `volume name`
docker stop mysql1 mysql2
docker rm mysql1 mysql2
docker volume ls
# volume 别名
docker volume rm `volume name`
docker volume rm `volume name`
docker run -d -v mysql:/var/lib/mysql --name mysql1 -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql
docker volume ls
# 验证mysql volume 是否生效
docker exec -it mysql1 /bin/bash
mysql -u root
show databases;
create database docker;
exit
docker ps
docker rm -f mysql1 # 强制删除
docker volume ls
docker run -d -v mysql:/var/lib/mysql --name mysql2 -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql
docker exec -it mysql2 /bin/bash
mysql -u root
show databases;
```
```
VOLUME ["/var/lib/mysql"]
docker run -v mysql:/var/lib/mysql
```
######数据持久化：Bind Mounting
```
docker run -v /home/aaa:/root/aaa

cd labs/docker-nginx/
more Dockerfile
docker build -t seckill44/my-nginx .
docker image ls
docker run -d -p 80:80 --name web seckill44/my-nginx
docker ps
curl 127.0.0.1

docker ps 
docker rm -f web
docker run -d -v $(pwd):/usr/share/nginx/html -p 80:80 --name web seckill44/my-nginx
docker exec -it web /bin/bash
cd /usr/share/nginx/html 
touch test.txt
exit
ls
vim test.txt
docker exec -it web /bin/bash
cd /usr/share/nginx/html 
more test.txt
```

######开发者利器 - docker bind mount
```
# 项目源码见: chapter5/labs/flask-skeleton
docker build -t seckill44/flask-skeleton .
docker image ls
chmod +x scripts/dev.sh
docker run -d -p 80:5000 --name flask seckill44/flask-skeleton # -it????
docker ps
在本地127.0.0.1可以看到flask web application

docker rm -f flask 
docker run -d -p 80:5000 -v $(pwd):/skeleton --name flask seckill44/flask-skeleton
docker ps
在本地直接修改skeleton/client/templates/main/home.html ，页面四秒回自动刷新
```

### 六
###### 部署wordpress
```
docker pull wordpress
docker pull mysql
docker ps 
docker run -d --name mysql -v mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=wordpress mysql
# docker run -d -v mysql:/var/lib/mysql --name mysql1 -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql
# docker exec -it mysql1 /bin/bash
docker ps
docker run -d -e WORDPRESS_DB_HOST=mysql:3306 -e WORDPRESS_DB_PASSWORD=root --link mysql -p 8080:80 wordpress
docker ps
浏览器输入127.0.0.1:8080
```

###### docker compose是什么
多容器的APP太恶心
- 要从Dockerfile build image 或者Dockerhub拉取image
- 要创建多个container
- 要管理这些container

Docker Compose “批处理”
Docker Compose
- Docker Compose 是一个工具
- 这个工具可以通过一个yml文件定义多容器的docker应用
- 通过一条命令就可以根据yml文件的定义去创建或管理这多个容器

docker-compose.yml
三大概念 Service Networks Volumes
 
Service 
- 一个serrvice代表一个container，这个container可以从dockerhub的image来创建，或者从本地的Dockerfile build出来的image来创建
- Service的启动类似docker run，我们可以给其指定network和volume，所以可以给service指定network和volume的引用
![image.png](https://upload-images.jianshu.io/upload_images/1956963-3ab817a924b25684.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/1956963-b9ad6e4109a3ae51.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/1956963-2a3805593dbfa6f9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

chapter6/labs/wordpress/docker-compose.yml 是一个完整案例

###### docker compose安装和基本使用
docker官网install compose
```
sudo curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

docker-compose
# 使用chapter6/labs/wordpress/docker-compose.yml 为例
docker-compose up --help
docker-compose -f docker-compose.yml up 
docker-compose up
docker ps
docker-compose ps
docker-compose stop  # 停止
docker-compose start 
docker-compose ps
docker-compose down
docker image ls
docker-compose up -d # -d 后台执行
docker-compose images
docker-compose exec mysql bash
exit
docker-compose exec wordpress bash
exit
docker network ls
docker-compose down

# 使用chapter6/labs/flask-redis/docker-compose.yml 为例
docker-compose up
# 浏览器打开127.0.0.1:8080 查看这个服务
docker-compose down # 删除
```

###### 水平扩展和负载均衡
```
# 使用chapter6/labs/flask-redis/docker-compose.yml 为例
docker-compose up -d
docker-compose ps
docker-compose up --help
docker-compose up --scale web=3 -d
docker-compose down 
# 端口冲突，删除掉docker-compose.yml 中的 ports
docker-compose ps
docker-compose up --scale web=3 -d
docker-compose ps 
docker-compose up --scale web=5 -d
docker-compose ps 
# 进入chapter6/labs/lb-scale
# 有空可以了解haproxy原理
docker-compose down 
docker-compose up -d
docker-compose ps 
curl 127.0.0.1:8080 # hostname 是 web1的hostname
docker-compose up --scale web=3 -d
docker-compose ps 
curl 127.0.0.1:8080
curl 127.0.0.1:8080
curl 127.0.0.1:8080 
for i in `seq 10`; do curl 127.0.0.1:8080; done
```
![image.png](https://upload-images.jianshu.io/upload_images/1956963-60c97d81b1a53f00.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###### 部署一个复杂的投票应用
```
# 源码在chapter6/labs/example-voting-app
docker-compose up
# 访问localhost:5000可以投票
# 访问localhost:5001可以查看结果
docker-compose build # 可以先编译 然后up比较快
```
![image.png](https://upload-images.jianshu.io/upload_images/1956963-2ad9e74ac857c94b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

-------------------------------------------------------
### 需要记住的命令
```
docker image ls = docker images
docker image rm `image-id` = docker rmi `image-id` 
docker container ls -a = docker ps -a
docker container rm `container id` = docker rm `container id`
docker rm $(docker container ls -aq) # 删除所有的container
docker container commit = docker commit
docker logs `container id`
docker image build = docker build
docker build -t lh/centos-vim-new . #通过Dockerfile build一个image
docker cp dist 242://usr/share/nginx/html # 复制dist文件到container的目录中
docker network ls
docker network inspect `network id`
docker network connect my-bridge test2
docker volume ls
docker volume inspect `volume name`
docker rm -f mysql
docker run -d -v $(pwd):/usr/share/nginx/html -p 80:80 --name web seckill44/my-nginx
docker-compose up
docker-compose build
```
```
Dockerfile语法
// 查看本地已经有的image
docker image ls
// 通过Dockerfile Build 一个image
docker image build = docker build
docker build -t lh/hello-world .
// 执行container
docker run lh/hello-world
// 删除image
docker image rm `image-id` = docker rmi `image-id` 
// 查看container ，默认只显示运行的
docker container ls
// 查看所有的container
docker container ls -a = docker ps -a
// 交互式运行
docker run -it centos
// 删除container
docker container rm `container id` = docker rm `container id`
// 查看所有的container，只显示container id
docker container ls -aq
// 删除所有的container
docker rm $(docker container ls -aq)
// 查看所有的container，根据提供的条件过滤输出
docker container ls -f "status=exited" -q
// 删除所有已退出的container
docker rm $(docker container ls -f "status=exited" -q)
```
