

1. 图片的URI和路径：content://media

2. URI转换成路径

3. Bitmap

   ```kotlin
   bitmap.getAllocationByteCount() // 推荐，获取bitmap的大小
   bitmap.compress(CompressFormat format, int quality, OutputStream stream) 
   // quality 减小压缩率，Bitmap大小不变，图片的质量变差，文件的大小变小，图片的宽高不变。进行色度抽样的操作，变得是颜色，是有损压缩。
   ```

4. 图片压缩的两种方法

   1. 质量压缩：减小压缩率

   2. 尺寸压缩

      邻近采样压缩：通过改变 BitmapFactory.Options 的 inSampleSize 来改变图片宽高，inSampleSzie 为2的倍数。直接选择两个相邻的颜色像素其中的一个像素作为生成像素，另一个像素直接抛弃，Bitmap变小，文件变小

   3. 色彩模式压缩

      通过设置 BitmapFactory.Options.inPreferredConfig 改变不同的色彩模式，使得每个像素大小改变，从而图片大小改变

   4. Matrix压缩图片

      Matrix进行缩放处理之后的图片不是像采样率压缩一样纯粹的一种颜色，而是两种颜色的混合。这也叫做双线性采样，它使用的是双线性內插值算法，这个算法不像邻近点插值算法一样，直接粗暴的选择一个像素，而是参考了源像素相应位置周围 2x2 个点的值,根据相对位置取对应的权重，经过计算之后得到目标图像。
   
5. 保存图片到本地并在相册中显示

   ```java
   // 系统相册目录：
   Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator
   // 发系统广播通知手机有图片更新
   Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
   Uri uri = Uri.fromFile(file);
   intent.setData(uri);
   context.sendBroadcast(intent);
   ```

   
