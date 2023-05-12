package com.sec.tiktok

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircleImageView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context!!, attrs, defStyleAttr) {
    private var mSize = 0
    private var mPaint: Paint? = null

    init {
        mPaint = Paint()
        mPaint!!.isDither = true
        mPaint!!.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        mSize = width.coerceAtMost(height) //取宽高的最小值
        setMeasuredDimension(mSize, mSize) //设置CircleImageView为等宽高
    }

    override fun onDraw(canvas: Canvas) { //获取sourceBitmap，即通过xml或者java设置进来的图片
        val drawable = drawable ?: return
        val sourceBitmap = (drawable as BitmapDrawable).bitmap
        if (sourceBitmap != null) { //对图片进行缩放，以适应控件的大小
            val bitmap = resizeBitmap(sourceBitmap, width, height)
            drawCircleBitmapByShader(canvas, bitmap) //利用BitmapShader实现
        }
    }

    private fun resizeBitmap(sourceBitmap: Bitmap, dstWidth: Int, dstHeight: Int): Bitmap {
        val width = sourceBitmap.width
        val height = sourceBitmap.height
        val widthScale = dstWidth.toFloat() / width
        val heightScale = dstHeight.toFloat() / height

        //取最大缩放比
        val scale = widthScale.coerceAtLeast(heightScale)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, true)
    }

    private fun drawCircleBitmapByShader(canvas: Canvas, bitmap: Bitmap) {
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint!!.shader = shader
        canvas.drawCircle(
            (mSize / 2).toFloat(), (mSize / 2).toFloat(), (mSize / 2).toFloat(), mPaint!!
        )
    }
}