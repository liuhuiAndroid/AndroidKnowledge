package com.sec.weather.model

/**
 * 实时天气
 */
data class WeatherNow(
    // API状态码
    val code: String,
    // 当前数据的响应式页面
    val fxLink: String,
    val now: Now,
    val refer: Refer,
    // 当前API的最近更新时间
    val updateTime: String
)

data class Now(
    // 云量，百分比数值。可能为空
    val cloud: String?,
    // 露点温度。可能为空
    val dew: String?,
    // 体感温度，默认单位：摄氏度
    val feelsLike: String,
    // 相对湿度，百分比数值
    val humidity: String,
    // 天气状况和图标的代码，图标可通过天气状况和图标下载
    val icon: String,
    // 数据观测时间
    val obsTime: String,
    // 当前小时累计降水量，默认单位：毫米
    val precip: String,
    // 大气压强，默认单位：百帕
    val pressure: String,
    // 温度，默认单位：摄氏度
    val temp: String,
    // 天气状况的文字描述，包括阴晴雨雪等天气状态的描述
    val text: String,
    // 能见度，默认单位：公里
    val vis: String,
    // 风向360角度
    val wind360: String,
    // 风向
    val windDir: String,
    // 风力等级
    val windScale: String,
    // 风速，公里/小时
    val windSpeed: String
)