package com.example.hetwapenvanroosendaal.ui.card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.components.EAN13Generator
import com.example.hetwapenvanroosendaal.databinding.FragmentCardBinding
import java.net.URLEncoder

class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)

        //Set the header texts of the page
        _binding!!.homeHeader.pageTitle.text = requireContext().getString(R.string.card)
        _binding!!.homeHeader.pageDescription.text = requireContext().getString(R.string.card_description)

        // Initialize shared preferences
        val sharedPref = this.requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // get user id from shared preferences
        val userid = (sharedPref.getString("user", "0"))

        // check if user id is set and not empty
        if (userid == null && userid == "0") {
            // @TODO: If not, navigate to login page
        }

        // Define barcode image view & text
        val barcode = binding.barcode
        val barcodeId = binding.barcodeId

        // Generate image
        val code = "123456789101"
        val ean13CodeDrawable = EAN13Generator.generateEAN13Code(this, code)

        // Ser the image
        barcode.setImageDrawable(ean13CodeDrawable)
        barcodeId.text = code

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}