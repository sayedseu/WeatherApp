package com.example.weatherapp.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCityListBinding
import com.example.weatherapp.view_model.CityListViewModel

class CityListFragment : Fragment() {

    private lateinit var viewModel: CityListViewModel
    private lateinit var binding : FragmentCityListBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)
        viewModel = ViewModelProvider(this).get(CityListViewModel::class.java)

        binding.textView.setOnClickListener {
            navController.navigate(R.id.action_cityListFragment_to_mapViewFragment)
        }
    }

}