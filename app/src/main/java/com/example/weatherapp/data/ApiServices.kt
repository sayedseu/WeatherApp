package com.example.weatherapp.data

import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiServices {
    @GET("data/2.5/find")
    suspend fun getWeatherData(@QueryMap parameters: Map<String, String>): WeatherResponse

    @GET("data/2.5/weather")
    fun getWeatherDataByCoord(@QueryMap parameters: Map<String, String>): Call<WeatherData>
}