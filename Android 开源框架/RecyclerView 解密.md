#### RecyclerView最好的伙伴：AsyncListDiffer

用于优化 RecyclerView 刷新效率，AsyncListDiffer 比 DiffUtil 更新

#### 自定义 LayoutManager

重写 smoothScrollToPosition 方法配合 SmoothScroller 可以控制滚动位置和滚动速度

#### SortedList 使用

如果列表需要排序的话，可以使用这个集合来代替，比较方便和高效。

```kotlin
private val dataList =
SortedList<String>(String::class.java, object : SortedListAdapterCallback<String>(this) {
    // 比较两个Item的内容是否一致，如不一致则会调用adapter的notifyItemChanged()
    override fun areItemsTheSame(item1: String?, item2: String?): Boolean = false

    // 用于排序，大于0升序，小于0降序，等于0不变
    override fun compare(o1: String?, o2: String?): Int = 0

    // 两个Item是不是同一个，可以用他们的id，或者其他的字段进行比较是否一样
    override fun areContentsTheSame(oldItem: String?, newItem: String?): Boolean = true
})
```

批量更新和删除

```kotlin
// 调用beginBatchedUpdates()之后，所有的对SortedList操作都会等到endBatchedUpdates()之后一起生效。
dataList.beginBatchedUpdates();  // 开始批量更新
dataList.addAll(items);          // 更新一批数据
dataList.remove(item);           // 删除一批数据
dataList.endBatchedUpdates();    // 结束更新
```

#### 预取 Prefetch

 [RecyclerView 数据预取](https://juejin.im/entry/58a3f4f62f301e0069908d8f)

```kotlin
LayoutManager.setItemPrefetchEnabled()
LayoutManager.setInitialPrefetchItemCount()
```

#### 缓存

- Scrap 保存被移除掉但可能马上要使用的缓存，优先考虑

  ```java
  // 存储的是刚从屏幕上分离出来的，但是又即将添加到屏幕上去的 ViewHolder
  final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();
  // 存储的是数据被更新的 ViewHolder
  ArrayList<ViewHolder> mChangedScrap = null;
  ```

- Cache

  缓存默认大小为2，可以通过 **setViewCacheSize** 改变容量大小

  存储预取的 ViewHolder，同时在回收 ViewHolder 时，也会可能存储一部分的 ViewHolder

  ```java
  final ArrayList<ViewHolder> mCachedViews = new ArrayList<ViewHolder>();
  ```

- ViewCacheExtension 

  自定义缓存，通常用不到

  ```java
  private ViewCacheExtension mViewCacheExtension;
  ```

- RecyclerViewPool 

  根据 ViewType 缓存无效的 ViewHoler，数组大小为5

  mCachedViews 存不下的会被保存到 mRecyclerPool 中，mRecyclerPool 保存满之后只能无情抛弃掉，它也有一个默认的容量大小

  ```java
  RecycledViewPool mRecyclerPool;
  ```

#### 优化

- 关闭默认动画提升效率：

  ```java
   /**
    * 打开默认局部刷新动画
    */
   public void openDefaultAnimator() {
       this.getItemAnimator().setAddDuration(120);
       this.getItemAnimator().setChangeDuration(250);
       this.getItemAnimator().setMoveDuration(250);
       this.getItemAnimator().setRemoveDuration(120);
       ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(true);
   }
  
   /**
    * 关闭默认局部刷新动画
    */
   public void closeDefaultAnimator() {
       this.getItemAnimator().setAddDuration(0);
       this.getItemAnimator().setChangeDuration(0);
       this.getItemAnimator().setMoveDuration(0);
       this.getItemAnimator().setRemoveDuration(0);
       ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(false);
   }
  ```

- 使用 ConstraintLayout 替换复杂的布局

- 使用 DiffUtil 进行差异化比较

- Item 高度是固定，使用 RecyclerView.setHasFixedSize(true) 避免重复测量

- 多个 RecycledView 的 Adapter 一样，可以共用 RecyclerViewPool 

- 加大缓存，用空间换时间来提高滚动的流畅性

  ```kotlin
  recyclerView.setItemViewCacheSize(20)
  // 开启
  recyclerView.setDrawingCacheEnabled(true)
  recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
  ```

- 共用监听器

- 数据缓存，提高二次加载速度

- onCreateViewHolder 中 setOnClickListener

- 不到万不得已不要全局刷新，使用局部刷新

  - 如果使用了全局刷新，Adapter 重写 getItemId 方法，并且设置 setHasStableIds(true)

#### 其他

- RecyclerView 滑动时图片加载的优化

  在滑动时停止加载图片，在滑动停止时开始加载图片，这里用了Glide.pause 和 Glide.resume

#### 参考

- [RecyclerView一些你可能需要知道的优化技术](https://www.jianshu.com/p/1d2213f303fc)
- [RecyclerView 性能优化](https://juejin.im/post/5baedbf05188255c596714ab)
- [RecyclerView Scrolling Performance](https://stackoverflow.com/questions/27188536/recyclerview-scrolling-performance)