package com.sec.weather.network

import com.sec.weather.model.AstronomySun
import com.sec.weather.model.CityLookup
import com.sec.weather.model.Weather3d
import com.sec.weather.model.WeatherNow
import com.sec.weather.utils.Constants
import com.sec.weather.utils.TimeUtils
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {

    /**
     * 城市信息查询
     */
    @GET(value = "https://geoapi.qweather.com/v2/city/lookup")
    suspend fun cityLookup(
        @Query("location") location: String,
        @Query("key") key: String = Constants.API_KEY
    ): CityLookup

    /**
     * 实时天气
     */
    @GET(value = "v7/weather/now")
    suspend fun weatherNow(
        @Query("location") location: String,
        @Query("key") key: String = Constants.API_KEY
    ): WeatherNow

    /**
     * 日出日落
     */
    @GET(value = "v7/astronomy/sun")
    suspend fun astronomySun(
        @Query("location") location: String,
        @Query("key") key: String = Constants.API_KEY,
        @Query("date") date: String = TimeUtils.getNow()
    ): AstronomySun

    /**
     * 3天预报
     */
    @GET(value = "v7/weather/3d")
    suspend fun weather3d(
        @Query("location") location: String,
        @Query("key") key: String = Constants.API_KEY
    ): Weather3d
}