package com.example.weatherapp.model.data

import com.example.weatherapp.model.model_class.WeatherResponse

interface WeatherRepo {
    suspend fun getWeatherData(options : Map<String, String>) : WeatherResponse
}