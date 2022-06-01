package com.example.refugeehelper.requests.ui.housing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.refugeehelper.databinding.FragmentHousingRequestsBinding

class HousingRequestsFragment : Fragment() {

    private var _binding: FragmentHousingRequestsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val housingRequestsViewModel = ViewModelProvider(this).get(HousingRequestsViewModel::class.java)

        _binding = FragmentHousingRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.housingRequestsTitle
        housingRequestsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}