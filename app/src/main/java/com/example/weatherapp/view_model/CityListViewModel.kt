package com.example.weatherapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.ResponseState
import com.example.weatherapp.data.WeatherRepo
import com.example.weatherapp.model.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(private val weatherRepo: WeatherRepo) : ViewModel() {

    private val weatherData = MutableLiveData<ResponseState<WeatherResponse>>()

    fun weatherLiveData(): LiveData<ResponseState<WeatherResponse>> = weatherData

    fun getWeatherData() {
        viewModelScope.launch {
            weatherData.value = ResponseState.Loading
            try {
                val options: MutableMap<String, String> = mutableMapOf(
                    "lat" to "23.68",
                    "lon" to "90.35",
                    "cnt" to "50",
                    "appid" to "e384f9ac095b2109c751d95296f8ea76",
                )
                weatherData.value = ResponseState.Success(weatherRepo.getWeatherData(options))
            } catch (exception: Exception) {
                weatherData.value = ResponseState.Error(exception)
            }
        }
    }
}