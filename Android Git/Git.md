[如何从本地把项目上传到github&&如何把github项目通过clone复制下来，详细教程](http://www.cnblogs.com/chengxs/p/6297659.html)

#### 初始化仓库

```
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/liuhuiAndroid/CustomView.git
git push -u origin master
```

#### 切换到tag

```
git checkout v3.8.0
git checkout -b branch tag // 创建一个基于指定tag的分支

git checkout -b code-analysis v3.8.0 //例子
git push origin code-analysis:code-analysis //提交本地分支到远程分支，没有则创建
```

#### 删除本地分支 

```
git branch -D br 
```
#### 切换分支 

```
git checkout master
```
#### 查看远程分支地址

```
 git remote -v
```
#### 忽略文件

![image.png](http://upload-images.jianshu.io/upload_images/1956963-b361501f27233c50.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 设置大小写敏感

```
git config core.ignorecase false  
```

```
在本地新建一个分支： git branch index-swiper
切换到你的新分支: git checkout index-swiper
将新分支发布在github上： git push origin index-swiper
在本地删除一个分支： git branch -d index-swiper
在github远程端删除一个分支： git push origin :index-swiper(分支名前的冒号代表删除)

合并index-swiper分支到主分支：
git checkout master
git merge origin/index-swiper
git push
```

#### 修改commit
git commit --amend

#### 常见错误
windows 使用git 需要在环境变量配置：/git安装目录/bin/，否则idea中git无法使用

#### 提交回滚

想fork一个github上的项目，然后回滚到某个简单的版本然后提交到我的远程分支。

git reset --hard c146cb0
git commit -m "revert to version 2.3.0"
git push -u origin master -f    强制提交

