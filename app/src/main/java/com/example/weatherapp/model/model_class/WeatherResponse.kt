package com.example.weatherapp.model.model_class

data class WeatherResponse(
    val cod: String,
    val count: Int,
    val list: List<WeatherData>,
    val message: String
)