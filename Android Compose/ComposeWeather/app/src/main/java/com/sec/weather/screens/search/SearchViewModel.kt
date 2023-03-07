package com.sec.weather.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sec.weather.model.CityLookup
import com.sec.weather.data.DataOrException
import com.sec.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val _data = MutableLiveData<DataOrException<CityLookup, Exception>>()

    val data: LiveData<DataOrException<CityLookup, Exception>> = _data

    fun getCityLookup(location: String) {
        viewModelScope.launch {
            val response = repository.getCityLookup(location = location)
            _data.postValue(response)
        }
    }

}