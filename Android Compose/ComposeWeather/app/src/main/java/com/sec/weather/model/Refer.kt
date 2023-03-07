package com.sec.weather.model

data class Refer(
    // 数据许可或版权声明，可能为空
    val license: List<String>?,
    // 原始数据来源，或数据源说明，可能为空
    val sources: List<String>?
)