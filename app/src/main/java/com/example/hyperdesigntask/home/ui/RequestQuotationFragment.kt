package com.example.hyperdesigntask.home.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hyperdesigntask.R
import com.example.hyperdesigntask.data.model.Container
import com.example.hyperdesigntask.data.model.RequestQuotation
import com.example.hyperdesigntask.databinding.FragmentRequestQuotationBinding
import com.example.hyperdesigntask.home.viewmodel.RequestQuotationViewModel
import com.example.hyperdesigntask.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestQuotationFragment : Fragment() {
    private var _binding: FragmentRequestQuotationBinding? = null
    private  val requestQuotationViewModel: RequestQuotationViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestQuotationBinding.inflate(inflater, container, false)
        binding.addContainerButton.setOnClickListener {
            val containerView = LayoutInflater.from(context).inflate(R.layout.container_item, null)

            binding.containerList.addView(containerView)
        }

        binding.requestButton.setOnClickListener {
            requestQuotationViewModel.requestQuotation(createRequestQuotation())
            observeRequestQuotation()
        }
        return binding.root
    }

    private fun observeRequestQuotation() {
        lifecycleScope.launchWhenStarted {
            requestQuotationViewModel.requestQuotationState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar("Quotation requested successfully")
                        findNavController().navigate(R.id.action_requestQuotationFragment_to_homeFragment)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showSnackbar(state.message)
                    }
                }
            }
        }
    }
    private fun createRequestQuotation(): RequestQuotation {
        val shipmentName = binding.nameOfShipment.text.toString()
        val description = binding.description.text.toString()
        val quantity = binding.quantity.text.toString()
        val comment = binding.comment.text.toString()
        val containers = collectContainerData()

        return RequestQuotation(
            shipmentName = shipmentName,
            description = description,
            quantity = quantity,
            containerNumber = containers,
            comment = comment
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun collectContainerData(): List<Container> {
        val containers = mutableListOf<Container>()

        for (i in 0 until binding.containerList.childCount) {
            val containerView = binding.containerList.getChildAt(i) as ViewGroup

            val number = containerView.findViewById<EditText>(R.id.et_container_number).text.toString()
            val size = containerView.findViewById<EditText>(R.id.et_container_size).text.toString()
            val weight = containerView.findViewById<EditText>(R.id.et_container_weight).text.toString()
            val dimension = containerView.findViewById<EditText>(R.id.et_container_dimension).text.toString()

            containers.add(
                Container(
                    number = number,
                    size = size,
                    weight = weight,
                    dimension = dimension
                )
            )
        }

        return containers
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

}
