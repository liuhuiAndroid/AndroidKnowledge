package com.sec.weather.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun formatDate(utcTime: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+08:00")
        val date = df.parse(utcTime)
        df.applyPattern("yyyy-MM-dd")
        return df.format(date)
    }

    fun formatDateHHmm(utcTime: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+08:00")
        val date = df.parse(utcTime)
        df.applyPattern("HH:mm")
        return df.format(date)
    }

    fun getNow(): String {
        return SimpleDateFormat("yyyyMMdd").format(Date())
    }

}