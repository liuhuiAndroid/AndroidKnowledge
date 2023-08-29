#### 第2章 MySQL版本类问题

#### 第3章 用户管理类问题

#### 第4章 服务器配置类问题

#### 第5章 在日志类问题

#### 第6章 存储引擎类问题

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
