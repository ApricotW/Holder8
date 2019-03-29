package com.healthy.holder8.utils

import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

//关键字object用来声明单例对象，就像Java中开发者自己定义的Utils工具类。
//其内部的属性等同于Java中的static静态属性，外部可直接获取属性值。
object DateUtil {

    //声明一个当前日期时间的属性，
    val nowDateTime: String
    //外部访问DateUtil.nowDateTime时，会自动调用nowDateTime附属的get方法得到它的值
    get() {
    val sdf = getDateInstance(SHORT)
    return sdf.format(Date())
    }

    //只返回日期字符串
    val nowDate: String
    get() {
    val sdf = getDateInstance(MEDIUM)
    return sdf.format(Date())
    }

    //只返回时间字符串
    val nowTime: String
    get() {
    val sdf = getTimeInstance(MEDIUM)
    return sdf.format(Date())
    }

    //返回详细的时间字符串，精确到毫秒
    val nowTimeDetail: String
    get() {
    val sdf = getTimeInstance(LONG)
    return sdf.format(Date())
    }

    //返回开发者指定格式的日期时间字符串
    fun getFormatTime(format: String=""): String {
    val ft: String = format
    val sdf = if (!ft.isEmpty()) SimpleDateFormat(ft)
    else SimpleDateFormat("yyyyMMddHHmmss")
    return sdf.format(Date())
    }
}
