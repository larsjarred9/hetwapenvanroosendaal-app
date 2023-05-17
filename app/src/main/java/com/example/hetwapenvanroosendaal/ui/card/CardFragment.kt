package com.example.hetwapenvanroosendaal.ui.card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentCardBinding
import com.example.hetwapenvanroosendaal.databinding.FragmentLoginBinding
import org.json.JSONObject
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
        if (userid != null && userid != "0") {
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}