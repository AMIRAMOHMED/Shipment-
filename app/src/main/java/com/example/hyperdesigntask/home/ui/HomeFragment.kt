package com.example.hyperdesigntask.home.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyperdesigntask.R
import com.example.hyperdesigntask.data.model.Shipment
import com.example.hyperdesigntask.databinding.FragmentHomeBinding
import com.example.hyperdesigntask.home.viewmodel.HomeViewModel
import com.example.hyperdesigntask.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() ,OnShipmentClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val  HomeViewModel: HomeViewModel by  viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShipmentAdapter(this)
        binding.shipmentList.adapter = adapter
        binding.shipmentList.layoutManager=LinearLayoutManager(context)
        HomeViewModel.fetchShippments("1")

        lifecycleScope.launch {
            HomeViewModel.shippments.collect {
                resources ->
                when (resources) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE

                        resources.data.let {
                            adapter.setShipmentItems(it.shippments.data)
                        }

                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar(resources.message)
                    }
                }
            }

        }

        binding.fabRequest.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_requestQuotationFragment)
        }
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onShipmentClick(shipment: Shipment) {
        val action = HomeFragmentDirections.actionHomeFragmentToShipmentItemDetailsFragment(shipment.id.toString())
        findNavController().navigate(action)
    }
}
