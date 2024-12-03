package com.example.hyperdesigntask.home.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.hyperdesigntask.R
import com.example.hyperdesigntask.databinding.FragmentRequestQuotationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestQuotationFragment : Fragment() {
    private var _binding: FragmentRequestQuotationBinding? = null
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
binding.quantity
        binding.nameOfShipment
        binding.description
        binding.comment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun collectContainerData(): List<Map<String, String>> {
        val containers = mutableListOf<Map<String, String>>()

        for (i in 0 until binding.containerList.childCount) {
            val containerView = binding.containerList.getChildAt(i) as ViewGroup

            val number = containerView.findViewById<EditText>(R.id.et_container_number).text.toString()
            val size = containerView.findViewById<EditText>(R.id.et_container_size).text.toString()
            val weight = containerView.findViewById<EditText>(R.id.et_container_weight).text.toString()
            val dimension = containerView.findViewById<EditText>(R.id.et_container_dimension).text.toString()

            containers.add(
                mapOf(
                    "number" to number,
                    "size" to size,
                    "weight" to weight,
                    "dimension" to dimension
                )
            )
        }

        return containers
    }
}
