package com.example.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.weatherapp.adapter.CityListAdapter
import com.example.weatherapp.databinding.FragmentCityListBinding
import com.example.weatherapp.model.data.ResponseState
import com.example.weatherapp.model.model_class.WeatherData
import com.example.weatherapp.view_model.CityListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CityListFragment : Fragment(), CityListAdapter.OnCityItemClickListener {
    @Inject
    lateinit var adapter: CityListAdapter
    private val viewModel: CityListViewModel by viewModels()
    private lateinit var binding: FragmentCityListBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)

        observerWeatherLiveData()
        viewModel.getWeatherData()
    }

    private fun observerWeatherLiveData() {
        viewModel.weatherLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                ResponseState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setupAdapter(it.data.list)
                    showErrorSnackbar()
                }
                is ResponseState.Error -> {
                    showErrorSnackbar()
                }
            }
        })
    }

    private fun setupAdapter(data: List<WeatherData>) {
        val dividerItemDecoration = DividerItemDecoration(context, 1)
        adapter.setWeatherDataset(data)
        adapter.setListener(this)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.adapter = adapter
    }

    override fun onClick(data: WeatherData) {
        Log.d("hasan", "onClick: $data")
    }

    private fun showErrorSnackbar() {
        val snack = Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_LONG)
        snack.setAction("RETRY", View.OnClickListener { viewModel.getWeatherData() })
        snack.show()
    }

}