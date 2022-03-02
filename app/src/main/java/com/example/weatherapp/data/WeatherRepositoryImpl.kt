package com.example.weatherapp.data

import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    WeatherRepo {

    override suspend fun getWeatherData(options: Map<String, String>): WeatherResponse {
        return withContext(Dispatchers.IO) {
            apiServices.getWeatherData(options)
        }
    }
}