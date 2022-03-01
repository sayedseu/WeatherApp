package com.example.weatherapp.di

import com.example.weatherapp.model.data.WeatherRepo
import com.example.weatherapp.model.data.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun bindWeatherRepo(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepo
}