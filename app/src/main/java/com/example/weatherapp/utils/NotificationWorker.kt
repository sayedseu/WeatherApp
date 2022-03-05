package com.example.weatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.data.ApiServices
import com.example.weatherapp.extension.convertKelvinToCelsius
import com.example.weatherapp.extension.makeReadableFormat
import com.example.weatherapp.model.WeatherData
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val apiServices: ApiServices
) : Worker(appContext, workerParams) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location: Location? ->
            location?.let {
                fetchWeatherDataAndSendNotification(it.latitude.toString(), it.longitude.toString())
            }
        }
        return Result.success()
    }

    private fun fetchWeatherDataAndSendNotification(lat: String, lon: String) {
        val parameters: MutableMap<String, String> = mutableMapOf(
            "lat" to lat,
            "lon" to lon,
            "appid" to "e384f9ac095b2109c751d95296f8ea76",
        )
        apiServices.getWeatherDataByCoord(parameters).enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                response.body()?.let {
                    notificationHelper.sendNotification(
                        "Current Temperature: ${
                            it.main.temp.convertKelvinToCelsius().makeReadableFormat()
                        }"
                    )
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {}
        })
    }
}