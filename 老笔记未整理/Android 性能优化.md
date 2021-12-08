1. 正确应对系统内存不足，OnLowMemory 和 OnTrimMemory 回调

   ```kotlin
       override fun onTrimMemory(level: Int) {
           super.onTrimMemory(level)
           if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
               Glide.get(this).clearMemory()
           }
           Glide.get(this).onTrimMemory(level)
       }
   
       override fun onLowMemory() {
           super.onLowMemory()
           Glide.get(this).onLowMemory()
       }
   ```

2. AsyncListDiffer — RecyclerView最好的伙伴

3. 