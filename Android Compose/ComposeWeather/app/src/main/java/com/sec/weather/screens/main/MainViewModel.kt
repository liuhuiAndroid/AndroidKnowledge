package com.sec.weather.screens.main

import androidx.lifecycle.ViewModel
import com.sec.weather.data.*
import com.sec.weather.model.AstronomySun
import com.sec.weather.model.Weather3d
import com.sec.weather.model.WeatherNow
import com.sec.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    suspend fun getWeatherNow(location: String): DataOrException<WeatherNow, Exception> {
        return repository.getWeatherNow(location = location)
    }

    suspend fun getAstronomySun(location: String): DataOrException<AstronomySun, Exception> {
        return repository.getAstronomySun(location = location)
    }

    suspend fun getWeather3d(location: String): DataOrException<Weather3d, Exception> {
        return repository.getWeather3d(location = location)
    }
}