package com.healthy.holder8.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.healthy.holder8.R
import com.healthy.holder8.ui.MainActivity
import java.util.*

/**
 * 实时心跳曲线绘制。会根据心率变速。
 */
class HeartBeatLine : View {
    //画笔
    lateinit protected var mPaint: Paint

    lateinit public var mPath: Paint

    //网格颜色
    protected var mGridColor = R.color.colorAccent

    //小网格颜色
    protected var mSGridColor = R.color.colorPrimary
    //背景颜色
    protected var mBackgroundColor = Color.TRANSPARENT
    //自身的大小
    protected var mWidth: Int = 80
    protected var mHeight: Int = 100

    //网格宽度
    protected var mGridWidth = 25
    //小网格的宽度
    protected var mSGridWidth = 5


    internal var context: Context

    internal var data = ArrayList<Int>()

    internal var xx: Int = 0

    internal var xlist = ArrayList<HashMap<Int, Int>>()

    constructor(context: Context) : super(context) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.context = context
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        this.context = context
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
        xing()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initBackground(canvas)
    }

    private fun initBackground(canvas: Canvas) {
        mPaint = Paint()
        mPath = Paint()

//        canvas.drawColor(mBackgroundColor)
        //画小网格

//        //竖线个数
//        val vSNum = mWidth / mSGridWidth
//
//        //横线个数
//        val hSNum = mHeight / mSGridWidth
//        mPaint.color = mSGridColor
//        mPaint.strokeWidth = 2f
//        //画竖线
//        for (i in 0 until vSNum + 1) {
//            canvas.drawLine((i * mSGridWidth).toFloat(), 0f, (i * mSGridWidth).toFloat(), mHeight.toFloat(), mPaint)
//        }
//        //画横线
//        for (i in 0 until hSNum + 1) {
//
//            canvas.drawLine(0f, (i * mSGridWidth).toFloat(), mWidth.toFloat(), (i * mSGridWidth).toFloat(), mPaint)
//        }
//
//        //竖线个数
//        val vNum = mWidth / mGridWidth
//        //横线个数
//        val hNum = mHeight / mGridWidth
//        mPaint.color = mGridColor
//        mPaint.strokeWidth = 2f
//        //画竖线
//        for (i in 0 until vNum + 1) {
//            canvas.drawLine((i * mGridWidth).toFloat(), 0f, (i * mGridWidth).toFloat(), mHeight.toFloat(), mPaint)
//        }
//        //画横线
//        for (i in 0 until hNum + 1) {
//            canvas.drawLine(0f, (i * mGridWidth).toFloat(), mWidth.toFloat(), (i * mGridWidth).toFloat(), mPaint)
//        }


        //搏动折线的颜色
        mPath.color = resources.getColor(MainActivity().linecolor)
        //搏动折线的宽度

        mPath.strokeWidth = 6.0f
        val scale = context.resources.displayMetrics.density

        if (data.size > 0) {
            //    画搏动折线
            for (k in 1 until data.size) {
                if (data[k] == xx) {
                    val b = k.toFloat() * 5f * scale
                    var x = 0f
                    for (i in 1 until xlist.size) {
                        val map1 = xlist[i - 1]
                        val x1 = map1[0]!!
                        val y1 = map1[1]!!
                        val map2 = xlist[i]
                        val x2 = map2[0]!!
                        val y2 = map2[1]!!
                        if (i == 1) {
                            x = x1 - b
                        }
                        canvas.drawLine(x1 - x, y1.toFloat(), x2 - x, y2.toFloat(), mPath)
                    }
                } else {
                    canvas.drawLine(
                        (k - 1).toFloat() * 5f * scale,
                        data[k - 1] * scale,
                        k.toFloat() * 5f * scale,
                        data[k] * scale,
                        mPath
                    )
                }
            }
        }

        if (data.size > 80) {
            data.removeAt(0)//移除左边图形  让图形移动起来
        }
    }

    fun setData(i: Int) {
        data.add(getMobileHeight(context) + i)
        postInvalidate()
    }

    fun getMobileHeight(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        xx = (mHeight.toFloat() / scale / 2f).toInt()
        return (mHeight.toFloat() / scale / 2f).toInt()
    }

    private fun xing() {
        //        Paint paint = new Paint();// 创建画笔
        //        paint.setStyle(Paint.Style.STROKE);
        //        paint.setStrokeWidth(2);
        //        paint.setColor(Color.BLUE);
        ////        Canvas canvas = null;
        //        Path path = new Path();
        xlist.clear()
        val scale = context.resources.displayMetrics.density * 2//调整心 大小
        var i = 0 + 180
        while (i < 361 + 180) {
            val sit = i.toDouble() * 2.0 * Math.PI / 360.0
            val x = scale.toDouble() * 16.0 * Math.pow(Math.sin(sit), 3.0)
            val y = scale * (13 * Math.cos(sit) - 5 * Math.cos(2 * sit) - 2 * Math.cos(3 * sit) - Math.cos(4 * sit))
            val map = HashMap<Int,Int>()
            map.put(0, (mWidth / 2 + x).toInt())
            map.put(1, ((mHeight / 2).toDouble() - y - (scale * 15).toDouble()).toInt())//调整心 高度未知
            xlist.add(map)
            i = i + 10
            //            if (i == 180) {
            //                path.moveTo(mWidth / 2 + (float) (x), mHeight / 2 - (float) (y));
            //            }
            //            path.lineTo(mWidth / 2 + (float) (x), mHeight / 2 - (float) (y));
            //
            //            if (canvas != null)
            //                canvas.drawPath(path, paint);

        }
    }
}
