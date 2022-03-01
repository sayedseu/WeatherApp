package com.example.weatherapp.model.data

import com.example.weatherapp.model.model_class.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiServices: ApiServices) : WeatherRepo {

    override suspend fun getWeatherData(options: Map<String, String>): WeatherResponse {
       return  withContext(Dispatchers.IO){
            apiServices.getWeatherData(options)
       }
    }
}