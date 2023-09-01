#### 第2章 MySQL版本类问题

- 如何选择使用何种版本的MySQL
  - MySQL 常见的发行版
    - MySQL 官方版本：社区版、企业版
    - Percona MySQL：兼容官方版本，优于社区版
    - MariaDB：以 MySQL5.5 基础
  - 各个发行版之间的区别及优缺点
- MySQL,Percona,MariaDB之间的区别和优缺点
- 在线升级MySQL数据库
  - 在对 MySQL 进行升级前要考虑什么？
    - 升级可以给业务带来的益处
      - 是否可以解决业务上某一方面的痛点
      - 是否可以解决运维上某一方面的痛点
    - 升级可能对业务带来的影响
      - 对原业务程序的支持是否有影响
      - 对原业务程序的性能是否有影响
    - 数据库升级方案的制定
      - 评估受影响的业务系统
      - 升级后的数据库环境检查
      - 升级后的业务检查
    - 升级失败的回滚方案
      - 升级失败回滚的步骤
      - 回滚后的数据库环境检查
      - 回滚后的业务检查
  - MySQL 升级步骤
    - 对升级数据库进行备份
    - 升级Slave服务器版本
    - 手动进行主从切换
    - 升级Master服务器版本
    - 升级完成后进行业务检查
- MySQL8.0版本的新特性
  - 服务器功能
    - 所有元数据使用 innodb 引擎存储，无 frm 文件
    - 系统表采用 innodb 存储并采用独立表空间
    - 支持定义资源管理组
    - 支持不可见索引和降序索引，支持直方图优化
    - 支持窗口函数
    - 支持在线修改全局参数持久化
  - 用户及安全
    - 默认使用 caching_sha2_password 认证插件
    - 新增支持定义角色
    - 新增密码历史记录功能，限制重复使用密码
  - innodb存储引擎
    - innodb DDL 语句支持原子操作
    - 支持在线修改 UNDO 表空间
    - 新增管理视图用于监控 innodb 表状态
    - 新增 innodb_dedicated_server 配置项

#### 第3章 用户管理类问题

- 对用户授权指定的权限

  - 用户名@可访问控制列表
    - % 代表可以从所有外部主机访问
    - 192.168.1.% 表示可以从 192.168.1 网段访问
    - localhost:DB 服务器本地访问
  - 使用 CREATE USER 命令建立用户
  - 常用的用户权限：
    - Admin
      - Create User：建立新的用户的权限
      - Grant option：为其他用户授权的权限
      - Super：管理服务器的权限
    - DDL
      - Create：新建数据库、表的权限
      - Alter：修改表结构的权限
      - Drop：删除数据库和表的权限
      - Index：建立和删除索引的权限
    - DML
      - Select：查询表中数据的权限
      - Insert：向表中插入数据的权限
      - Update：更新表中数据的权限
      - Delete：删除表中数据的权限
      - Execute：执行存储过程的权限
  - 如何为用户授权
    - 遵循最小权限要求
    - 使用 Grant 命令对用户授权

  ```mysql
  # 定义 MySQL 数据库账号
  create user 
  # 为用户授权
  grant select,insert,update,delete on db.tb to user@ip;
  revoke delete on db.tb from user@ip;
  ```

- MySQL如何保证用户账号安全

  - 数据库用户管理流程规范
    - 最小权限原则
    - 密码强度策略
    - 密码过期原则
    - 限制历史密码重用原则
  - 密码管理策略

  ```shell
  # 定义 MySQL 数据库账号
  create user test@'localhost' identified by '123456' password history 1;
  select * from mysql.user where user='test';
  # 设置密码过期
  alter user test@'localhost' password expire;
  # 重新设置密码
  alter user user() identified by '12345678';
  ```

- 在不同实例间迁移数据库用户

  - 数据库版本一致 -> 备份恢复

  - 数据库版本不一致 -> 导出授权语句

    ```sql
    pt-show-grants u=root,p=123456,h=localhost
    ```

#### 第4章 服务器配置类问题

- 使用SQL Mode改变SQL处理行为

  - SQL Mode 作用：
    - 配置 MySQL 处理 SQL 的方式
    - set [session/global/persist] sql_mode= 'xxx'
    - my.cnf    [mysqld] sql_mode=xxx
  - 常用的 SQL Mode
    - ONLY_FULL_GROUP_BY
    - ANSI_QUOTES：禁止用双引号来引用字符串
    - REAL_AS_FLOAT：Real 做为 float 的同义词
    - PIPES_AS_CONCAT：将 || 视为字符串的连接操作符而非或运算符
    - STRICT_TRANS_TABLES / STRICT_ALL_TABLES：严格模式
    - ERROR_FOR_DIVISION_BY_ZERO：不允许0做为除数
    - NO_AUTO_CREATE_USER：在用户不存在时不允许grant语句自动建立用户
    - NO_ZERO_IN_DATE / NO_ZERO_DATA：日期数据不能含0
    - NO_ENGINE_SUBSTITUTION：当指定的存储引擎不可用时报错

  ```sql
  # 查看 SQL Mode
  show variables like 'sql_mode';
  # 设置 SQL Mode
  set session sql_mode= 'NO_ENGINE_SUBSTITUTION,ONLY_FULL_GROUP_BY';
  ```

- 对比配置文件同MySQL运行配置参数

  - 使用 set 命令配置动态参数
  - 使用 pt-config-diff 工具比较配置文件

  ```sql
  set [session | @@session] system_var_name = expr
  set [global | @@global] system_var_name = expr
  set [persist | @@persist] system_var_name = expr
  
  show variables like 'wait_timeout';
  set global wait_timeout=300;
  show global variables like 'wait_timeout';
  set session wait_timeout=300;
  show variables like 'wait_timeout';
  
  show variables like 'max_connect%';
  # 配置存在：mysqld-auto.cnf，不会重启丢失；MySQL8.0；否则得同时修改 mysqld.cnf才不会重启覆盖
  set persist max_connect=1000;
  
  # 比较配置文件
  pt-config-diff u=root,p=xxx,h=xxx /etc/my.cnf
  ```

- 影响MySQL性能的关键参数

  - 服务器配置参数
    - max_connections：设置MySQL允许访问的最大连接数
    - interactive_timeout：设置交互连接的timeout时间
    - wait_time：设置非交互连接的timeout时间
    - max_allowed_packet：控制MySQL可以接收的数据包大小
    - sync_binlog：表示每写多少次缓冲会向磁盘同步一次binlog
    - sort_buffer_size：设置每个会话使用的排序缓冲区的大小
    - join_buffer_size：设置每个会话所使用的连接缓冲的大小
    - read_buffer_size：指定了当对MYISAM进行表扫描时所分配的读缓冲池的大小
    - read_rnd_buffer_size：设置控制索引缓冲区的大小
    - binlog_cache_size：设置每个会话用于缓存未提交的事务缓存大小
  - 存储引擎参数
    - innodb_flush_log_at_trx_commit：1-每次事务提交都会刷新事务日志到磁盘中
    - innodb_buffer_pool_size：设置innodb缓冲池的大小，应为系统可用内存的75%
    - innodb_buffer_pool_instances：innodb缓冲池的实例个数
    - innodb_file_per_table：设置每个表独立使用一个表空间文件

#### 第5章 在日志类问题

MySQL常用的日志类型

1. 错误日志 error_log

   记录 MySQL 在启动、运行或停止时出现的问题

   1. 分析排除 MySQL 运行错误
   2. 记录未授权的访问

   ```mysql
   log_error = $mysql/sql_log/mysql-error.log
   # 错误日志级别
   log_error_verbosity = [1,2,3]
   # 定义如何把错误信息写入到日志中
   log_error_services= [日志服务组件;日志服务组件]
   
   select @@log_error;
   select @@log_error_verbosity;
   select @@log_error_services;
   select @@log_timestamps;
   set persist log_timestamps='SYSTEM';
   show variables like 'log_error_verbosity';
   show variables like 'log_error_services';
   install component 'file://component_log_sink_json'
   # 输出 json 格式日志数据
   set persist log_error_services='log_sink_json';
   ```

2. 常规日志 general_log

   记录所有发向 MySQL 的请求

   1. 分析客户端发送到 MySQL 的实际请求

   ```mysql
   general_log = [ON|OFF]
   general_log_file = $mysql/sql_log/general.log
   log_output = [FILE|TABLE|NONE]
   
   use mysql;
   show create table general_log;
   
   CREATE TABLE `general_log` (
     `event_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
     `user_host` mediumtext NOT NULL,
     `thread_id` bigint unsigned NOT NULL,
     `server_id` int unsigned NOT NULL,
     `command_type` varchar(64) NOT NULL,
     `argument` mediumblob NOT NULL
   ) ENGINE=CSV DEFAULT CHARSET=utf8mb3 COMMENT='General log'
   
   select @@general_log;
   select @@general_log_file;
   set persist general_log_file='/home/mysql/sql_log/general.log'
   select @@log_output;
   # 启动general_log
   set global general_log=on;
   tail -f /home/mysql/sql_log/general.log
   set global general_log=off;
   set global log_output='table';
   set global general_log=on;
   select * from general_log;
   truncate table general_log;
   ```

3. 慢查询日志 show_query_log

   记录符合条件的查询

   1. 将执行成功并符合条件的查询记录到日志中
   2. 找到需要优化的SQL

   ```mysql
   slow_query_log = [ON|OFF]
   show_query_log_file = $mysql/sql_log/slow_log.log
   long_query_time = xx秒，六位小数
   log_queries_not_using_indexes = [ON|OFF]
   log_slow_admin_statements = [ON|OFF]
   log_slow_slave_statements = [ON|OFF]
   
   use mysql;
   show variables like 'long_query_time';
   set global long_query_time = 0.001;
   show variables like 'long_query_time'; # 需关闭
   set global long_query_time = 0;
   show variables like 'slow_query_log%';
   set global slow_query_log = on;
   set global slow_query_log_file = '/var/lib/mysql/sql_log/slow_log.log';
   ```

4. 二进制日志 binary_log【重要】

   记录全部有效的数据修改日志

   1. 记录所有对数据库中数据的修改
   2. 基于时间点的备份和恢复
   3. 主从复制

   ```mysql
   # 配置文件中的静态配置，默认不启用
   log_bin [=base_name]
   log_bin = /home/mysql/sql_log/mysql-bin
   grep log_bin /etc/my.cnf
   show variables like 'log_bin%';
   
   binlog_format = [ROW|STATEMENT|MIXED]
   binlog_row_image = [FULL|MINIMAL|NOBLOB]
   show variables like 'binlog_format%';
   show variables like 'binlog_row_image%';
   flush logs;
   mysqlbinlog --no-defaults -vv --base64-output=DECODE-ROWS mysql-bin.000003
   set global binlog_row_image = minimal;
   show variables like 'binlog_row_image%';
   set session binlog_row_image=minimal;
   
   # 查看实际提交的SQL
   binlog_rows_query_log_events = [ON|OFF]
   flush logs;
   show variables like 'binlog_rows_query_log_events%';
   set binlog_rows_query_log_events=on;
   
   log_slave_updates = [ON|OFF]
   sync_binlog = [1|0]
   # 自动清理过期的二进制日志
   expire_logs_days = days
   # 手动清理二进制日志
   PURGE BINARY LOGS TO 'mysql-bin.010'
   PURGE BINARY LOGS BEFORE '2008-01-01 22:22:22';
   ```

5. 中继日志 relay_log

   用于主从复制，临时存储从主库同步的二进制日志

   1. 临时记录从主服务器同步的二进制日志

   ```mysql
   relay_log=filename
   relay_log_purge = [ON|OFF]
   ```

#### 第6章 存储引擎类问题

MySQL常用的存储引擎

1. MYISAM

   MySQL 5.6 之前的默认存储引擎，最常用的非事务型存储引擎

   1. 非事务型存储引擎
   2. 以堆表方式存储
   3. 使用表级锁
   4. 支持 Btree 索引，空间索引，全文索引

   ```mysql
   create table myIsam(id int,c1 varchar(10)) engine=myisam;
   check table myIsam;
   repair table myIsam;
   # 压缩
   myisampack --myisampack
   myisampack -b -f myIsam
   ```

   MyISAM 使用场景

   1. 读操作远远大于写操作的场景
   2. 不需要使用事务的场景

2. CSV

   以CSV格式存储的非事务型存储引擎

   1. 非事务型存储引擎
   2. 数据以CSV格式存储
   3. 所有列都不能为NULL
   4. 不支持索引

   ```mysql
   create table mycsv(id int not null,c1 varchar(20) not null,c2 varchar(20) not null) engine=csv;
   show create table mycsv;
   insert into mycsv values(1,'aa','bb'),(2,'cc','dd');
   select * from mycsv;
   create index idx_id on mycsv(id);
   ```

   CSV 使用场景

   1. 做为数据交换的中间表使用

3. Archive

   只允许查询和新增数据而不允许修改的非事务型存储引擎；用于归档数据

   1. 非事务型存储引擎
   2. 表数据使用zlib压缩
   3. 只支持 Insert 和 Select
   4. 只允许在自增 ID 上建立索引

   ```mysql
   create table myarchive(id int auto_increment,c1 varchar(20),c2 char(20),key(id)) engine=archive;
   show create table myarchive;
   insert into myarchive(c1,c2) values('aa','bb'),('cc','dd');
   select * from myarchive;
   delete from myarchive where id=1;
   ```

   Archive 使用场景

   1. 日志和数据采集类应用
   2. 数据归档存储

4. Memory

   是一种易失性非事务型存储引擎

   1. 非事务型存储引擎
   2. 数据保存在内存中
   3. 所有字段长度固定
   4. 支持 Btree 和 Hash 索引

   Memory 使用场景

   1. 用于缓存字典映射表
   2. 缓存周期性分析数据

5. Innodb

   最常用的事务型存储引擎

   1. 事务型存储引擎支持ACID
   2. 数据按主键聚集存储
   3. 支持行级锁及MVCC
   4. 支持 Btree 和自适应 Hash 索引
   5. 支持全文和空间索引

   Innodb 使用场景

   1. 大多数 OLTP 场景

   Innodb 问题

   1. Innodb无法在线修改表结构的情况

      1. Innodb不支持在线修改表结构的场景

         1. 加全文索引
         2. 加空间索引
         3. 删除主键
         4. 增加自增列
         5. 修改列类型
         6. 改表字符集

      2. 在线 DDL 存在的问题

         1. 有部分语句不支持在线DDL
         2. 长时间的DDL操作会引起严重的主从延迟
         3. 无法对DDL操作进行资源限制

      3. 如何更安全的在线修改表结构 DDL

         1. 使用 pt-online-schema-change [OPTION] DSN

         ```mysql
         pt-online-schema-change --help | more
         alter table stock add column modified_time timestamp
         pt-online-schema-change --alter "add column modified_time timestamp" --execute D=stock,t=stock,u=dba,p=123456
         ```

   2. Innodb如何实现事务处理

      什么是事务？事务实现的方式？

      1. 原子性A

         回滚日志 Undo Log：用于记录数据修改前的状态

      2. 一致性C

         重作日志 Redo Log：用于记录数据修改后的状态

      3. 隔离性I

         锁：用于资源隔离，分为共享锁和排他锁

      4. 持久性D

         重作日志 Redo Log + 回滚日志 Undo Log

   3. MySQL的多版本并发控制(MVCC)，Innodb 读是否会阻塞写

      读写应该相互阻塞吗？读操作不会被写操作阻塞，使用Undo Log

      1. 查询需要对资源加共享锁
      2. 数据修改需要对资源加排他锁

      ```mysql
      begin;
      select * from stock.stock where id=1;
      select * from stock.stock where id=1;
      
      begin;
      update stock.stock set count=10 where id=1;
      update stock.stock set count=30 where id=1;
      ```

6. NDB

   MySQL 集群所使用的内存型事务存储引擎

   1. 事务型存储引擎
   2. 数据存储在内存中
   3. 支持行级锁
   4. 支持高可用集群
   5. 支持Ttree索引

   NDB 使用场景

   1. 需要数据完全同步的高可用场景

- 

#### 第7章 MySQL架构类问题

#### 第8章 备份恢复类问题

逻辑备份和物理备份；全量备份、增量备份和差异备份

常用的备份工具：

1. mysqldump：最常用的逻辑备份工具，支持全量备份及条件备份

   优点：

   - 备份结果为可读的SQL文件，可用于跨版本跨平台恢复数据
   - 备份文件的尺寸小于物理备份，便于长时间存储
   - MySQL发行版自带工具，无需安装第三方软件

   缺点：

   - 只能单线程执行备份恢复任务，备份恢复速度较慢
   - 为完成一致性备份需要对备份表加锁，容易造成阻塞
   - 会对 Innodb Buffer Pool 造成污染

   ```shell
   mysqldump --help
   # 备份 stock 数据库
   mysqldump -uroot -p --databases stock > stock.sql
   # 备份 stock 数据库中 stock 表
   mysqldump -uroot -p stock stock > stock.sql
   # 备份记录备份二进制信息
   mysqldump -uroot -p --master-data=2 --all-databases > all.sql
   # 恢复
   mysql -uroot -p stock < stock.sql
   # 有条件备份
   mysqldump -uroot -p --where "count>20" stock stock > stock_20.sql
   ```

2. mysqlpump：多线程逻辑备份工具，mysqldump的增强版本

   优点：

   - 语法同 mysqldump 高度兼容，学习成本低
   - 支持基于库和表的并行备份，可以提高逻辑备份的性能
   - 支持使用 ZLIB 和 Lz4 算法对备份进行压缩

   缺点：

   - 基于表进行并行备份，对于大表来说性能较差
   - 5.7.11 之前版本不支持一致性并行备份
   - 对会 Innodb Buffer Pool 造成污染

   ```shell
   mysqlpump --help
   mysqlpump --compress-output=zlib --setgtid-purged=off --databases stock > stock.zlib
   ls -lh stock.zlib
   # 解压缩
   zlib_decompress stock.zlib stock.sql
   # 备份数据库账号
   mysqlpump --users --exclude-databases=sys,mysql,percona,stock --set-gtid-purged=off -uroot -p
   ```

3. xtrabackup：Innodb在线物理备份工具，支持多线程和增量备份

   优点：

   1. 支持 Innodb 存储引擎的在线热备份，对 Innodb 缓冲没有影响
   2. 支持并行对数据库的全备和增量备份
   3. 备份和恢复效率比逻辑备份高

   缺点：

   1. 做单表恢复时比较复杂
   2. 完整的数据文件拷贝，故备份文件比逻辑备份大
   3. 对跨平台和数据库版本的备份恢复支持度不如逻辑备份

   ```shell
   # percona 官网下载安装
   rpm -ivh percona-xtrabackup.rpm
   xtrabackup --help
   innobackupex --help
   mkdir -p /home/db_backup
   # 备份
   innobackupex --user=root --password=123456 /home/db_backup
   # 恢复
   innobackupex --apply-log /home/db_backup/xxx
   innobackupex --move-back /home/db_backup/xxx
   chown mysql:mysql -R data/
   chown mysql:mysql -R undo_log/
   /etc/init.d/mysqld start
   ps -ef
   ```

4. 增量备份和恢复

   1. 逻辑备份 + 二进制日志

      ```shell
      # 全备
      mkdir /home/db_backup
      mysqldump -uroot -p --master-data=2 --sigle-transaction --routines --triggers --events --set-gtid-purged=off stock > stock.sql
      # 增量备份
      scp stock.sql root@192.168.1.1:/root
      scp mysql-bin.000001 root@192.168.1.1:/root
      scp mysql-bin.000002 root@192.168.1.1:/root
      # 全备恢复
      mysql -uroot -p stock < stock.sql
      # 二进制恢复
      more stock.sql # 取出二进制日志点 MASTER_LOG_FILE 和 MASTER_LOG_POS
      mysqlbinlog --start-position=337 --database=stock mysql-bin.000002 > stock_diff.sql
      mysql -uroot -p stock < stock_diff.sql
      ```

   2. 使用 xtrabackup 工具

      ```shell
      # 全备
      innobackupex --user=root --password=123456 /home/backups
      # 增量备份
      innobackupex --user=root --password=123456 --incremental /home/db_incremental_backups --incremental-basedir=/home/backups/xxx
      # 增量恢复
      innobackupex --apply-log --redo-only 全备目录
      innobackupex --apply-log --redo-only 全备目录 --incremental-dir=第1...n次增量目录
      innobackupex --apply-log 全备目录
      /etc/init.d/mysqld stop
      cd mysql
      rm -rf data_old/ undo_old/
      mv data data_old
      mv undo_log/ undo_old
      mkdir data undo_log
      cd /home/db_backup
      innobackupex --move-back /home/db_backup/全备目录
      chown mysql:mysql -R data/
      chown mysql:mysql -R undo_log/
      /etc/init.d/mysqld start
      ps -ef
      ```

   如何备份二进制日志？

   1. 使用 cp 命令进行离线备份

   2. 使用 mysqlbinlog 命令在线实时备份

      ```shell
      # 用户需要具有 replication slave 权限
      mysqlbinlog --raw --read-from-remote-server --stop-never --host 备份机IP --port 3306 -u repl -p xxx 起始二进制日志文件名
      ```

#### 第9章 管理及监控类问题

- 性能类指标：

  - QPS：数据库每秒钟处理的请求数量

    ```sql
    # 查询 Com_insert、Com_delete
    show global status like 'Com%';
    show global status like 'Com_update';
    # 所有累加和：QPS=(Queries2-Queries1)/时间间隔
    show global status like 'Queries';
    show global status where variable_name in ('Queries', 'uptime');
    ```

  - TPS：数据库每秒钟处理的事务数量

    ```sql
    show global status where variable_name in ('Com_insert', 'Com_delete', 'Com_update');
    ```

  - 并发数：数据库实例当前并行处理的会话数量

    ```sql
    show global status like 'Threads_running';
    ```

  - 连接数：连接到数据库会话的数量

    ```sql
    # 报警阈值：Threads_connected/max_connections > 0.8
    show global status like 'Threads_connected';
    ```

  - 缓存命中率：Innodb的缓存命中率

    ```sql
    # 公式：(Innodb_buffer_pool_read_requests - Innodb_buffer_pool_reads) / Innodb_buffer_pool_read_requests * 100%
    # Innodb_buffer_pool_read_requests：从缓存池中读取的次数
    # Innodb_buffer_pool_reads：表示从物理磁盘读取的次数
    ```

- 功能类指标：

  - 可用性：数据库是否可正常对外提供服务

    - 周期性连接数据库服务器并执行 select @@version;
    - mysqladmin -uxxx -pxxx -hxxx ping

  - 阻塞：当前是否有阻塞的会话

    ```sql
    # >= mysql5.7
    show create view sys.innodb_lock_waits;
    
    select waiting_pid as '被阻塞的线程', waiting_query as '被阻塞的SQL', blocking_pid as '阻塞线程', blocking_query as '阻塞SQL', wait_age as '阻塞时间', sql_kill_blocking_query as '建议操作' from sys.innodb_lock_waits where (UNIX_TIMESTAMP() - UNIX_TIMESTAMP(wait_started)) > 30
    
    select waiting_pid as 'blocked pid', waiting_query as 'blocked SQL', blocking_pid as 'running pid', blocking_query as 'running SQL', wait_age as 'blocked time', sql_kill_blocking_query as 'info' from sys.innodb_lock_waits where (UNIX_TIMESTAMP() - UNIX_TIMESTAMP(wait_started)) > 30
    
    # \G 可以竖着显示
    ```

  - 死锁：当前事务是否产生了死锁

    ```sql
    show engine innodb status;
    # 使用pt工具，指定监控死锁服务器信息，存储到数据库表中【信息比较全】
    pt-deadlock-logger u=dba,p=xxx,h=127.0.0.1 --create-dest-table --dest u=dba,p=xxx,h=127.0.0.1,D=crn,t=deadlock
    # 设置mysql变量存储死锁日志【分析较麻烦】
    set global innodb_print_all_deadlocks=on;
    set persist innodb_print_all_deadlocks=on;
    ```

  - 慢查询：实时慢查询监控

    - 通过慢查询日志监控

    - 通过 information_schema.`PROCESSLIST` 表实时监控

      ```sql
      select * from information_schema.processlist where time > 60 and command <> 'Sleep';
      ```

  - 主从延迟：数据库主从延迟时间

    - show slave status 监控 Seconds_Behind_Master 值

    - 监控主从延迟

      ```sql
      # 使用 pt-heartbeat 工具：主库插入数据
      pt-heartbeat --user=xxx --password=xxx -h master --create-table --database xxx -update --daemonize --interval=1
      # 周期性从库读取数据，进行时间比较
      pt-heartbeat --user=xxx --password=xxx -h slave --database crn --monitor --daemonize --log /tmp/slave_lag.log
      ```

  - 主从状态：数据库主从复制链路是否正常

    - show slave status 监控 Slave_IO_Running & Slave_SQL_Running 值是否为 YES；如果异常在 Last_Errno 和 Last_Error 查看故障信息

#### 第10章 异常处理类问题

- 解决MySQL服务器IO负载过大问题

  - 磁盘IO超负荷

    - iostat -dmx 1	：查看IO情况

    - lsof	：查看具体进程所打开的文件

    - MYSQL 输出大量日志

    - MYSQL 正在进行大批量写

    - 慢查询产生了大量的磁盘临时表

      ```sql
      show global status like '%tmp%'
      ```

  - 存在大量阻塞线程

    - show processlist
    - 阻塞监控

  - 存在大量并发慢查询

    - show processlist
    - 慢查日志

  - 存在其他占用CPU的服务

    - ps
    - top

  - 服务器硬件资源问题

    - 硬件监控

- 解决MySQL主从数据不一致故障

  - 前提

    - 主从数据库延迟为0
    - IO_THREAD 和 SQL_THREAD 状态为 YES
    - 相同查询在主从服务器中查询结果不同

  - 原因

    - 对从服务器进行了写操作

      - 设置 read_only = ON

      - 设置 super_read_only = ON

      - 使用 row 格式的复制

      - 使用 pt_table_sync 修复数据

        ```sql
        pt-table-sync --execute --charset=utf8 --database=xxx --table=xxx --sync-to-master h=xxx从库信息,u=xxx,p=xxx
        ```

    - 使用 sql_slave_skip_counter 或注入空事务的方式造成错误

    - 使用了 statement 格式的复制

- 解决从服务器连接不到主服务器的故障

  ```shell
  show slave status \G
  ```

  - 主从服务器间网络是否畅通

    ```shell
    ping xxx
    telnet 192.168.1.1 3306
    ```

  - 是否存在防火墙，过滤了数据库端口

  - 复制链路配置的用户名和密码是否正确，是否有相应权限

    ```shell
    show grants for current_user;
    grant replication slave on *.* to xxx@'192.168.1.%';
    stop slave;
    start slave;
    ```

- 解决主从复制中的主键冲突故障

  - 人为操作过 slave 数据
  - 出现过宕机
  - 处理：跳过故障数据，检查主从数据一致性，OR【直接删除从库主键冲突数据】

- 解决主从复制异常

  - 数据行不存在

    - 跳过故障数据
    - 使用 pt-table-sync 修复数据

  - 解决主从复制中的RelayLog错误故障

    - relay_log 损坏：slave 服务器宕机原因

      - 找到已经正确同步的日志点

        ```shell
        show slave status \G
        ```

        Relay_Master_Log_File & Exec_Master_Log_Pos

      - 使用 reset slave 删除 relay_log

      - 在正确同步日志点后重新同步日志

- MySQL数据库优化概论

  - 服务器硬件：CPU、磁盘、内存
  - 操作系统配置
    - 配置内核参数：/etc/sysctl.conf
    - 修改系统限制：/etc/security/limits.conf
  - MySQL 配置
  - 查询优化
    - SQL 改写
    - 索引优化
  - schema 优化
    - 范式化设计
    - 反范式化设计
    - 分库分表

### 其他问题

1. docker 的 mysql/mongodb 没有密码如何访问
2. javaweb 数据库配置信息通常存在哪
3. mysql 跨版本使用
4. mysqldump 备份还原可能发生什么问题
