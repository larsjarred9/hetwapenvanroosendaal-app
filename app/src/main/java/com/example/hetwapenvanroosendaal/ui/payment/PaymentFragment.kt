package com.example.hetwapenvanroosendaal.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)

        //Set the header texts of the page
        _binding!!.homeHeader.pageTitle.text = requireContext().getString(R.string.payment)
        _binding!!.homeHeader.pageDescription.text = requireContext().getString(R.string.paymentDesc)

        //Get image view by id
        val ivPayment = _binding!!.imgPayment

        //Set the correct image based on price
        when (arguments?.getInt("price")) {
            77 -> {
                ivPayment.setImageResource(R.drawable.img_month)
            }
            235 -> {
                ivPayment.setImageResource(R.drawable.img_quarter)
            }
            else -> {
                ivPayment.setImageResource(R.drawable.img_year)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
