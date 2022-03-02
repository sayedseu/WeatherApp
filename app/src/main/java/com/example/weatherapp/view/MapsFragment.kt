package com.example.weatherapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMapsBinding
import com.example.weatherapp.extension.capitalizeFirstCharacterOfEachWord
import com.example.weatherapp.extension.convertKelvinToCelsius
import com.example.weatherapp.extension.makeReadableFormat
import com.example.weatherapp.view_model.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentMapsBinding
    private var location: LatLng? = null
    private var locationName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        sharedViewModel.weatherData.observe(viewLifecycleOwner) {
            it?.let {
                locationName = it.name
                location = LatLng(it.coord.lat, it.coord.lon)
                binding.weatherInfo.temperatureTV.text =
                    it.main.temp.convertKelvinToCelsius().makeReadableFormat()
                binding.weatherInfo.cityName.text = it.name
                binding.weatherInfo.weatherCondition.text =
                    it.weather[0].description.capitalizeFirstCharacterOfEachWord()
                binding.weatherInfo.humidity.text = "Humidity: ${it.main.humidity}"
                binding.weatherInfo.windSpeed.text = "Wind Speed: ${it.wind.speed}"
                binding.weatherInfo.maxTemp.text =
                    "Max Temp: ${it.main.temp_max.convertKelvinToCelsius().makeReadableFormat()}"
                binding.weatherInfo.minTemp.text =
                    "Min Temp: ${it.main.temp_min.convertKelvinToCelsius().makeReadableFormat()}"
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val latLng = location ?: LatLng(-34.0, 151.0)
        val markerText = locationName ?: "Sydney"
        googleMap.addMarker(MarkerOptions().position(latLng).title(markerText))
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                ), 10.0f
            )
        )
    }
}