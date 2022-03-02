package com.example.weatherapp.data

import com.example.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiServices {
    @GET("data/2.5/find")
    suspend fun getWeatherData(@QueryMap options: Map<String, String>): WeatherResponse
}