package com.sec.weather.data

import java.lang.Exception

class DataOrException<T, E : Exception>(
    val data: T? = null,
    val loading: Boolean? = null,
    val exception: E? = null
)