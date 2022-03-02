package com.example.weatherapp.model

data class WeatherResponse(
    val cod: String,
    val count: Int,
    val list: List<WeatherData>,
    val message: String
)