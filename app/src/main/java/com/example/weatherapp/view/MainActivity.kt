package com.example.weatherapp.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.LocationHelper
import com.example.weatherapp.utils.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var locationHelper: LocationHelper
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!locationHelper.isLocationPermissionGranted()) {
            locationHelper.requestLocationPermission(this)
        } else {
            sendWorkRequest()
        }
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfig)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mapsFragment) {
                binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationHelper.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendWorkRequest()
            }
        }
    }

    private fun sendWorkRequest() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val notificationWorkRequest =
            PeriodicWorkRequest.Builder(NotificationWorker::class.java, 24, TimeUnit.HOURS)
                .addTag("TAG_SEND_NOTIFICATION")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "NOTIFICATION_WORK",
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationWorkRequest
            )
    }

}