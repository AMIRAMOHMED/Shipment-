package com.example.hyperdesigntask.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyperdesigntask.databinding.FragmentShipmentItemDetailsBinding
import com.example.hyperdesigntask.home.viewmodel.ShipmentItemDetailsViewModel
import com.example.hyperdesigntask.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShipmentItemDetailsFragment : Fragment() {

    private lateinit var binding: FragmentShipmentItemDetailsBinding
    private val viewModel: ShipmentItemDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShipmentItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.let { ShipmentItemDetailsFragmentArgs.fromBundle(it).id }

        id?.let {
            viewModel.getShipmentDetails(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.shipmentDetailsState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { details ->
                            binding.shipmentDetails = details
                            val containerAdapter = ContainerAdapter()
                            binding.containersRecyclerView.adapter = containerAdapter
                            binding.containersRecyclerView.layoutManager =
                                LinearLayoutManager(context)
                            containerAdapter.setContainers(details.Containers)
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar(resource.message ?: "An error occurred")
                    }
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}