## 一、创建一个简单 Spring Boot 项目
略

## 二、Spring Boot 项目添加 Docker 支持
在 pom.xml中添加 Docker 镜像名称：
```
    <properties>
        <docker.image.prefix>springboot</docker.image.prefix>
    </properties>
```
plugins 中添加 Docker 构建插件：
```
            <!-- Docker maven plugin -->
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
                    <dockerDirectory>src/main/docker</dockerDirectory>
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                </configuration>
            </plugin>
```
在目录src/main/docker下创建 Dockerfile 文件，Dockerfile 文件用来说明如何来构建镜像。
```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD springboot-webmvc-0.1-SNAPSHOT.war app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

## 三、安装 Docker 环境
在阿里云CentOS服务器上安装docker。略

## 四、使用 Docker 部署 Spring Boot 项目
安装mvn
```
wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
yum -y install apache-maven
```
部署：
```
git clone code ...
mvn clean package
cp target/springboot-webmvc-0.1-SNAPSHOT.war src/main/docker/
mvn package docker:build
docker images
docker run -p 8080:8080 -t springboot/springboot-webmvc
```
