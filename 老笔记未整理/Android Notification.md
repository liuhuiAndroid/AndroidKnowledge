- 建造者模式

- 显示本地通知

  ```kotlin
      fun setLocalNotification(context: Context) {
          val notificationManager =
              context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
          val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(), 0)
          var notification: Notification
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              val channelId = "channel_vr" // 渠道id
              val channelName = "channel_name" // 渠道名称
              // 构建 NotificationChannel 实例，Android O 新特性！！！
              // 需要指定 Channel 的 Id、Name 和通知的重要程度
              // IMPORTANCE_HIGH 紧急级别
              var mChannel =
                  NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MAX)
              // 是否绕过请勿打扰模式
              mChannel.setBypassDnd(true)
              // 设置通知出现时的闪光灯
              mChannel.enableLights(true)
              // 设置通知闪关灯的灯光颜色
              mChannel.lightColor = Color.RED
              // 锁屏显示通知
              mChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
              // 设置通知出现时的震动
              mChannel.enableVibration(true)
              // 设置通知震动模式
              mChannel.vibrationPattern = longArrayOf(1000, 500, 2000)
              // 在 notificationManager 中创建通知渠道
              notificationManager.createNotificationChannel(mChannel)
              // Android O 引入了通知渠道，以提供统一的系统来帮助用户管理通知
              notification = NotificationCompat.Builder(context, channelId)
                  .setAutoCancel(true)
                  .setContentTitle("VR带看")
                  .setContentText("您有新的VR带看消息")
                  .setSmallIcon(R.mipmap.ic_launcher)
                  .setLargeIcon(
                      BitmapFactory.decodeResource(
                          context.resources,
                          R.mipmap.ic_launcher
                      )
                  )
                  .setContentIntent(pendingIntent)
                  // 设置时间
                  .setWhen(System.currentTimeMillis())
                  // 添加默认声音提醒、默认震动提醒和默认呼吸灯提醒
                  .setDefaults(Notification.DEFAULT_ALL)
                  .setAutoCancel(true)
                  .build()
          } else {
              // Android O（Android 8.0）之前的用法
              notification = NotificationCompat.Builder(context)
                  .setAutoCancel(true)
                  // 指定通知的标题
                  .setContentTitle("VR带看")
                  // 设置通知的内容
                  .setContentText("您有新的VR带看消息")
                  // 设置通知的小图标
                  .setSmallIcon(R.mipmap.ic_launcher)
                  // 设置通知的大图标
                  .setLargeIcon(
                      BitmapFactory.decodeResource(
                          context.resources,
                          R.mipmap.ic_launcher
                      )
                  )
                  .setContentIntent(pendingIntent)
                  // 设置时间
                  .setWhen(System.currentTimeMillis())
                  // 添加默认声音提醒、默认震动提醒和默认呼吸灯提醒
                  .setDefaults(Notification.DEFAULT_ALL)
                  // 实现点击跳转后关闭通知
                  .setAutoCancel(true)
                  .build()
          }
          // 调用 NotificationManager 的 notify() 方法将通知显示出来
          // 第一个参数是通知的id，第二个参数是 notification 对象
          notificationManager.notify(1, notification)
      }
  ```

- 判断锁屏：KeyguardManager

  ```kotlin
  val mKeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
  // 当前是否处于锁屏状态
  val flag = mKeyguardManager.isKeyguardLocked
  ```

- 判断屏幕状态，唤醒屏幕

  ```kotlin
  val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
  // 如果isLighting为true，设备处于交互状态，不需要亮起屏幕
  // 如果isLighting为false，亮起屏幕10秒用于提醒
  val isLighting = powerManager.isInteractive
  if (!isLighting) {
      val mWakeLock = powerManager.newWakeLock(
          PowerManager.SCREEN_DIM_WAKE_LOCK
          or PowerManager.ACQUIRE_CAUSES_WAKEUP,
          "VRTakeSeeDemo:LockTag"
      )
      // 亮起屏幕10秒
      mWakeLock.acquire(10 * 1000L)
  }
  ```