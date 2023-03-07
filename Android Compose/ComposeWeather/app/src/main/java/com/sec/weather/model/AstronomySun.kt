package com.sec.weather.model

data class AstronomySun(
    val code: String,
    val fxLink: String,
    val refer: Refer,
    // 日出时间
    val sunrise: String,
    // 日落时间
    val sunset: String,
    val updateTime: String
)