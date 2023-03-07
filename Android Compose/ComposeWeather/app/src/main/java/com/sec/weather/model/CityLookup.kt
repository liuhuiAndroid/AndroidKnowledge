package com.sec.weather.model

data class CityLookup(
    val code: String,
    val location: List<Location>,
    val refer: Refer
)

data class Location(
    // 地区/城市所属一级行政区域
    val adm1: String,
    // 地区/城市的上级行政区划名称
    val adm2: String,
    // 地区/城市所属国家名称
    val country: String,
    // 该地区的天气预报网页链接，便于嵌入你的网站或应用
    val fxLink: String,
    // 地区/城市ID
    val id: String,
    // 地区/城市是否当前处于夏令时
    val isDst: String,
    // 地区/城市纬度
    val lat: String,
    // 地区/城市经度
    val lon: String,
    // 地区/城市名称
    val name: String,
    // 地区评分
    val rank: String,
    // 地区/城市的属性
    val type: String,
    // 地区/城市所在时区
    val tz: String,
    // 地区/城市目前与UTC时间偏移的小时数，参考详细说明
    val utcOffset: String
)