package com.example.weatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.weatherapp.R
import com.example.weatherapp.adapter.CityListAdapter
import com.example.weatherapp.data.ResponseState
import com.example.weatherapp.databinding.FragmentCityListBinding
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.view_model.CityListViewModel
import com.example.weatherapp.view_model.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CityListFragment : Fragment(), CityListAdapter.OnCityItemClickListener {
    @Inject
    lateinit var adapter: CityListAdapter
    private val viewModel: CityListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        viewModel.weatherLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                ResponseState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setupAdapter(state.data.list)
                }
                is ResponseState.Error -> {
                    showErrorSnackbar()
                }
            }
        }
    }

    private fun setupAdapter(data: List<WeatherData>) {
        adapter.setWeatherDataset(data)
        adapter.setListener(this)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, 1))
        binding.recyclerView.adapter = adapter
    }

    override fun onClick(data: WeatherData) {
        sharedViewModel.setWeatherData(data)
        navController.navigate(R.id.action_cityListFragment_to_mapsFragment)
    }

    private fun showErrorSnackbar() {
        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_LONG)
            .setAction("RETRY") { viewModel.getWeatherData() }
            .show()
    }

}