package com.healthy.holder8.utils

import com.healthy.holder8.R
import java.util.ArrayList
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView

/**
 * 折线图表，可以实现多个折线图的功能
 */
class LineCharts {
    private val values: List<PointValue>? = null
    private val lines: List<Line>? = null
    private var lineChartData: LineChartData? = null
    private var linesList: MutableList<Line>? = null
    private var pointValueList: MutableList<PointValue>? = null
    private var points: List<PointValue>? = null
    private var position:Float = 0f
    private var axisY: Axis? = null
    private var axisX: Axis? = null
    var count:Int = 1
    private var list:MutableList<AxisValue> = mutableListOf(AxisValue(0f).setLabel(DateUtil.nowTime))

    /**
     * 当前显示区域
     */

    private var max = 150f//最大高度,判断max与top的值，实时刷新图表y轴




    fun makeCharts(lineChartView: LineChartView, current: Float) {



        //实时添加新的点
        val value1 = PointValue(position, current)
        axisX!!.values = list
        pointValueList!!.add(value1)
        val x = value1.x
//根据新的点的集合画出新的线
        val line = Line(pointValueList)
        //颜色
        line.color = R.color.colorPrimary
        line.shape = ValueShape.DIAMOND
        line.isCubic = true//曲线是否平滑
        line.setHasPoints(false)//设置折线是否含点
        line.setHasLabels(true)
        linesList!!.clear()
        linesList!!.add(line)
        lineChartData = initData(linesList)
        lineChartView.lineChartData = lineChartData
        //根据点的横坐实时变换坐标的视图范围
        val port: Viewport
        if (x > 500) {
            port = initViewPort(x - 450, x+50, current)
        } else {
            port = initViewPort(0f, 550f, current)
        }
        lineChartView.currentViewport = port//当前窗口

        val maxPort = initMaxViewPort(x, current)//更新最大窗口设置
        lineChartView.maximumViewport = maxPort//最大窗口
        position++
        count++
        if (count==100) {
            list.add(AxisValue(position).setLabel(DateUtil.nowTime))
            if (list.count() > 5) {
                list.removeAt(0)
            }
            count = 0
        }
    }

    /**
     * 初始化视图
     */
    fun initView(lineChartView: LineChartView) {
        pointValueList = ArrayList()
        linesList = ArrayList()
        //初始化坐标轴
        axisY = Axis()
        axisX = Axis()
//        axisX!!.maxLabelChars = 10 如果缩小绘图间距可以把这个打开。 不然不要动
        lineChartData = initData(null)
        lineChartView.lineChartData = lineChartData
        val port = initViewPort(0f, 100f, 150f)
        lineChartView.setCurrentViewportWithAnimation(port)
        lineChartView.isInteractive = false
        lineChartView.isScrollEnabled = true
        lineChartView.isValueTouchEnabled = true
        lineChartView.isFocusableInTouchMode = true
        lineChartView.isViewportCalculationEnabled = false
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
        lineChartView.startDataAnimation()
        points = ArrayList()
    }


    /**
     * 初始化图表数据
     */

    fun initData(lines: List<Line>?): LineChartData {
        val data = LineChartData(lines)
        data.axisYLeft = axisY
        data.axisXBottom = axisX
        return data
    }

    /**
     * 最大显示区域
     */
    fun initMaxViewPort(right: Float, top: Float): Viewport {
        val port = Viewport()
        if (max > top) {
            port.top = max + 50
            // Log.d("IF",max + "");
        } else {
            max = top
            port.top = max + 50
            // Log.d("ELSE",max + "");
        }
        port.bottom = 0f
        port.left = 0f
        //此处越大，图片绘制越紧密。相应的，x轴时间戳要少生成一些，不然会相互覆盖。
        port.right = right + 500
        return port
    }

    fun initViewPort(left: Float, right: Float, top: Float): Viewport {
        val port = Viewport()
        if (max > top) {
            port.top = max + 10
            // Log.d("IF",max + "");
        } else {
            max = top
            port.top = max + 10
            //Log.d("ELSE",max + "");
        }
        port.bottom = 0f
        port.left = left
        port.right = right
        return port
    }
}