# 七、容器编排Docker Swarm
### 创建一个三节点的swarm集群
3 nodes swarm cluster setup
- Vagrant + Virtualbox
```
提供了Vagrantfile，直接运行vagrant up创建。 
vagrant status # 查看三台主机的状态
vagrant ssh swarm-manager # 进入manager节点
docker swarm --help
docker swarm init --help
ip a
docker swarm init --advertise-addr=192.168.205.10 # 先需要在manager中执行，查看输出是否是初始化完成
docker version # 查看docker是否启动成功
service docker start # 启动docker
docker swarm join --token #在上面的输出上找这行代码，拷贝，退出到swarm-wocker1节点运行，输出This node joined a swarm as a worker表示加入成功
vagrant ssh swarm-manager # 进入manager节点
docker node --help
docker node ls # 查看swarm节点
vagrant ssh swarm-worker2
docker swarm join --token ...
完成。
```
- Docker Machine + Virtualbox
```
docker-machine create swarm-manager
docker-machine create swarm-worker1
docker-machine create swarm-worker2
其余步骤同上
```
- Play with docker http://labs.play-with-docker.com/
```
打开上面这个网站，login and start
ADD NEW INSTANCE
其余步骤同上
```
### Service的创建维护和水平扩展
```
docker service
docker service create --help
docker service create --name demo busybox sh -c "while true;do sleep 3600;done"
docker service ls
docker service ps demo
docker ps
docker service scale # 扩展
docker service scale demo=5
docker service ls
docker service ps demo
docker ps # 在worker1和worker2上看一看
docker rm -f `worker2上的demo的container id` # 删除
docker service ls # manager看一看，发现不仅可以扩展，还可以修补
docker service rm demo
docker service ps demo # 看不到了
```
### 在swarm集群里通过service部署wordpress
```
docker network create -d overlay demo
docker network ls
docker service create --name mysql --env MYSQL_ROOT_PASSWORD=root --env MYSQL_DATABASE=wordpress --network demo --mount type=volume,source=mysql-data,destination=/var/lib/mysql mysql
docker service ls
docker service ps mysql
docker ps

docker service create --name wordpress -p 80:80 --env WORDPRESS_DB_PASSWORD=root --env WORDPRESS_DB_HOST=mysql --network demo wordpress
docker service ps wordpress
docker ps

ip a
访问192.168.205.12（wordpress所在的container的ip地址）,可以访问wordpress。安装看数据库能否连上。
访问manager的地址、发现也可以访问。

docker network ls # 发现有demo的网络了。# swarm同步的
```
### 集群服务间通信之Routing Mesh
Routing Mesh的两种体现
- Internal —— Container和Container之间的访问通过overlay网络（通过VIP虚拟IP）
- Ingress —— 如果服务有绑定接口，则此服务可以通过任意swarm节点的相应接口访问。
```
docker network create -d overlay demo
docker service create --name whoami -p 8000:8000 --network demo -d jwilder/whoami
docker service ls
docker service ps whoami
docker ps
curl 127.0.0.1:8000
docker service create --name client -d --network demo busybox sh -c "while true;do sleep 3600;done"
docker service ls
docker service ps client
docker exec -it f9f8 sh（f9f8是client容器的container id）
ping whoami（可以ping通）
docker service scale whoami=2
docker service ps whoami
ping whoami（可以ping通，whoami的ip地址没变，到底ping的谁？）
这个ip地址其实是vip，虚拟ip
nslookup www.baidu.com # 查询dns
docker exec -it f9f8 sh（f9f8是client容器的container id）
nslookup whoami
docker exec -it 5b79 ip a（5b79 是whoami容器的container id）
docker exec -it d9b ip a（d9b 是另一个whoami容器的container id）
nslookup tasks.whoami（这才能看到whoami的ip地址）
wget whoami:8000
more index.html
rm -rf index.html
wget whoami:8000
more index.html
```
### Routing Mesh之Ingress负载均衡
Ingress Network
- 外部访问的负载均衡
- 服务端口被暴露到各个swarm节点
- 内部通过IPVS进行负载均衡
```
docker service scale whoami=2
docker service ps whoami
curl 127.0.0.1:8000
curl 127.0.0.1:8000
在没有whoami节点的机器上：curl 127.0.0.1:8000，依然可以访问
sudo iptables -nL -t nat
ip a # 看到docker_gwbridge，相同网段
brctl show
docker network ls
docker network inspect docker_gwbridge # 可以看到两个container
sudo ls /var/run/docker/netns
sudo nsenter --net=/var/run/docker/netns/ingress_sbox
ip a
iptables -nL -t mangle
绕晕了
```
### Docker Stack部署Wordpress
```
chapter7/labs/wordpress/docker-compose.yml
docker stack deploy wordpress --compose-file=docker-compose.yml
docker stack ls
docker stack ps wordpress
docker stack services wordpress
docker stack rm wordpress
浏览器查看结果
```

### 作业解答之部署投票应用
```
chapter7/labs/example-vote-app/docker-compose.yml
docker stack deploy example --compose-file=docker-compose.yml
docker stack ls
docker stack services example
浏览器查看结果
docker service scale example_vote=3
docker stack services example

visualizer对swarm可视化
docker stack rm example
```

### Docker Secret管理和使用
什么是Secret？
- 用户名密码
- SSH Key
- TLS认证
- 任何不想让别人看到的数据 

Secret Management
- 存在Swarm Manager节点Raft database里
- Secret可以assign给一个service，这个service就能看到这secret
- 在container内部Secret看起来像文件，但是实际是在内存中
```
vi password      admin123
docker secret create my-pw password
rm -rf password
docker secret ls

echo "admin" | docker secret create my-pw2 -
docker secret ls
docker secret rm my-pw2

docker service create --name client --secret my-pw busybox sh -c "while true;do sleep 3600;done"
docker service ls
docker service ps client
docker ps
docker exec -it ccee sh 
cd /run/secrets/
ls
cat my-pw
exit

docker service create --name db --secret my-pw - e MYSQL_ROOT_PASSWORD_FILE=/run/secrets/my-pw mysql
docker service ls
docker service ps db
docker ps
docker exec -it 6dc sh
ls /run/secrets/
cat /run/secrets/my-pw mysql
mysql -u root -p # 验证密码
```

### Docker Secret在Stack中的使用
```
chapter7/labs/secret-example/docker-compose.yml
docker stack deploy wordpress -c=docker-compose.yml
docker stack services wordpress
```

### Service更新
```
docker network create -d overlay demo
docker network ls 
docker service create --name web --publish 8080:5000 --network demo xiaopeng163/python-flask-demo:1.0
docker service ps web
docker service scale web=2
docker service ps web
curl 127.0.0.1:8080 # 检查服务
sh -c "while true; do curl 127.0.0.1:8080&&sleep 1;done"
docker service update --help
docker service update --image xiaopeng163/python-flask-demo:2.0 web # 版本更新
docker service ps web

docker service update --publish-rm 8080:5000 --publish-add 8088:5000 web # 端口更新
docker stack deploy 会直接更新不演示。
```

# 八、DevOps初体验——Docker Cloud和Docker企业版
### Docker Cloud简介
CaaS —— Container-as-a-Service
什么是Docker Cloud
- 提供容器的管理，编排，部署的托管服务。
主要模块：
- 关联云服务商
- 添加节点作为Docker Host
- 创建服务Service
- 创建Stack
- Image管理
两种模式
- Standard模式。一个Node就是一个Docker Host
- Swarm模式，多个Node组成的Swarm Cluster

![image.png](https://upload-images.jianshu.io/upload_images/1956963-568b60e8eac9615c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Docker Cloud之自动build Docker image
```
Docker Cloud 进入SETTINGS 中的Cloud Settings。
选择Source providers中的Github，点击Connect provider，授权访问代码仓库。
Docker Cloud 用户头像选择Create Organization，填入名字和信息，创建一个新的Organization。
关闭Swarm mode
点击Cloud registry的Create repository，填入信息，点击create。成功。
点击Builds选择Configure Automated Builds，填入Github项目信息，点击Save and Build。下面会出现build的信息。
```
### Docker Cloud之持续集成和持续部署
开发到部署完整例子演示
### Docker企业版的在线免费体验
### Docker企业版本地安装之UCP
### Docker企业版本地安装之DTR
### Docker企业版UCP的基本使用演示
### 体验阿里云的容器服务
### 在阿里云上安装Docker企业版
### Docker企业版DTR的基本使用演示

# 九、容器编排Kubernetes
### Kubenetes简介
![image.png](https://upload-images.jianshu.io/upload_images/1956963-b5dba7839eb83c29.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Minikube快速搭建K8S单节点环境
```
安装minikube
curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 && chmod +x minikube && sudo cp minikube /usr/local/bin/ && rm minikube
安装kubectl
curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.11.0/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl

minikube version # 查看是否安装成功
kubectl version # 查看是否安装成功

minikube start # 创建一个单节点的集群

kubectl config view # 查看config
kubectl config get-contexts # 查看minikube的contexts
kubectl cluster-info # 查看Kubernetes cluster情况
minikube ssh # 可以进入到virtual创建的虚拟机内
docker version
```

### k8s最小调度单位Pod
```
chapter9/labs/pod-basic/pod-nginx.yml
kubectl version
kubectl create -f pod-nginx.yml # 创建resource
kubectl delete -f pod-nginx.yml # 删除pot
kubectl get pods # 查看pod
kubectl get pods -o wide # 显示pod详细信息
minikube ssh # 进入虚拟机
docker ps
docker exec -it `k8s id` sh # 进入容器内
docker network ls
docker network inspect bridge
kubectl get pods -o wide # 显示pod详细信息，比较ip
kubectl exec -it nginx sh # 这样也可以进入，如果是多台机器，默认进入第一个
kubectl exec -h
kubectl describe pod nginx # 显示详细信息

minikube ssh
ping 172.17.0.4
curl 172.17.0.4 # 获取到了nginx index.html
ip a
ping 192.168.99.100
kubectl port-forward nginx 8080:80 #方式一： 端口映射，访问测试
kubectl delete -f pod_nginx.yml # 删除
```

### ReplicaSet和ReplicationController
创建pod
```
chapter9/labs/replicas-set/rs_nginx.yml
pcat rs_nginx.yml
cp rs_nginx.yml rc_nginx.yml
vi rc_nginx.yml
修改 kind:ReplicaSet -> kind:ReplicationController
kubectl create -f rc_nginx.yml # 提示成功
kubectl get rc
kubectl get pods
kubectl delete pods nginx-6r92b
kubectl get pods
kubectl scale rc nginx --replicas=2
kubectl get pods
kubectl get rc
kubectl scale rc nginx --replicas=4
kubectl get pods
kubectl get rc
kubectl get pods -o wide # 查看pod详情
kubectl delete -f rc_nginx.yml # 删除
kubectl get pods -o wide
kubectl get rc
```
ReplicaSet is the next-generatiopn Replication Controller.
```
vim rs_nginx.yml
修改 apiVersion:v1 -> apiVersion:apps/v1
kubectl create -f rs_nginx.yml 
kubectl get rs
kubectl get pods
kubectl scale rs nginx --replicas=2
```
抛弃上一节直接创建pod的方式创建app，使用rc或者rs

### Deployment
A Deployment controller provides declarative updates for Pods and ReplicaSets.
```
chapter9/deployment/deployment_nginx.yml
kubectl create -f deployment_nginx.yml
kubectl get deployment
kubectl get rs
kubectl get pods
kubectl get pods -o wide

如歌升级Deployment
kubectl set image deployment nginx-deployment nginx=nginx:1.13
kubectl get deployment
kubectl get deployment -o wide
kubectl get rs
kubectl get pods
kubectl rollout history deployment nginx-deployment # 查看deployment历史
kubectl rollout undo deployment nginx-deployment # 回退到之前版本
kubectl get deployment -o wide
kubectl rollout history deployment nginx-deployment
kubectl set image deployment nginx-deployment nginx=nginx:1.13

如何暴露端口到minikube
kubectl get node
kubeclt get node -o wide
kubectl expose deployment nginx-deployment --type=NodePort
kubectl get svc
curl 192.168.99.100:30541（minikube地址） # 可以访问我们的服务
kubectl delete services nginx-deployment
```

### 使用Tectonic在本地搭建多节点K8S集群
```
Core OS公司的Tectonic-Kubernetes
Tectonic Sandbox
chapter9/tectonic-sandbox-1.7.5-tectonic.1
vagrant up
kubectl config get-contexts
kubectl config use-context minikube
kubectl config get-contexts
kubectl get node
kubectl config use-context tectonic
kubectl get node
```

### k8s基础网络Cluster Network
### Service简介和演示
### NodePort类型Service以及Label的简单实用
### 准备工作——使用kops在亚马逊AWS上搭建k8s集群
### 使用kops在亚马逊AWS上搭建k8s集群
### LoadBlancer类型Service以及AWS的DNS服务配置
### 在亚马逊k8s集群上部署wordpress

# 十、容器的的运维和监控
### 容器的基本监控
### k8s集群运行资源监控——Heapster+Grafana+InfluxDB
### 根据资源占用自动横向伸缩
### k8s集群Log的采集和展示——ELK+Fluentd
### k8s集群监控方案Prometheus

# 十一、Docker+DevOps实战——过程和工具
### 本章简介
### 搭建GitLab服务器
### 搭建GitLab CI服务器和Pipeline演示
### 基于真实Python项目的CI演示
### 简单Java项目的CI演示
### 使用Python项目演示的CICD流程
### CI实现版本自动发布
### 本章总结和如何继续学习

# 十二、总结
