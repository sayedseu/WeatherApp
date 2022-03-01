package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.CityItemBinding
import com.example.weatherapp.extension.capitalizeFirstCharacterOfEachWord
import com.example.weatherapp.extension.convertKelvinToCelsius
import com.example.weatherapp.extension.makeReadableFormat
import com.example.weatherapp.model.model_class.WeatherData
import javax.inject.Inject

class CityListAdapter @Inject constructor() :
    RecyclerView.Adapter<CityListAdapter.CityViewHolder>() {

    private var weatherDataset: List<WeatherData> = mutableListOf()
    private var listener: OnCityItemClickListener? = null

    fun setWeatherDataset(data: List<WeatherData>) {
        weatherDataset = data
    }

    fun setListener(listener: OnCityItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            CityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val weatherData = weatherDataset[position]
        holder.binding.cityNameTV.text = weatherData.name
        holder.binding.weatherConditionTV.text =
            weatherData.weather[0].description.capitalizeFirstCharacterOfEachWord()
        holder.binding.temperatureTV.text =
            weatherData.main.temp.convertKelvinToCelsius().makeReadableFormat()
        holder.binding.itemView.setOnClickListener {
            listener?.onClick(weatherData)
        }
    }

    override fun getItemCount(): Int = weatherDataset.size

    class CityViewHolder(itemBinding: CityItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val binding: CityItemBinding = itemBinding
    }

    interface OnCityItemClickListener {
        fun onClick(data: WeatherData)
    }
}