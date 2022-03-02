package com.example.weatherapp.data

import com.example.weatherapp.model.WeatherResponse

interface WeatherRepo {
    suspend fun getWeatherData(options: Map<String, String>): WeatherResponse
}