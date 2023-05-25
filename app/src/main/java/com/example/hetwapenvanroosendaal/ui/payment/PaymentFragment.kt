package com.example.hetwapenvanroosendaal.ui.payment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentPaymentBinding
import com.google.firebase.firestore.FirebaseFirestore

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

        var price: Int
        var month: Int

        //Set the correct image based on price
        when (arguments?.getInt("price")) {
            77 -> {
                ivPayment.setImageResource(R.drawable.img_month)
                month = 1
                price = 77
            }
            235 -> {
                ivPayment.setImageResource(R.drawable.img_quarter)
                month = 3
                price = 235
            }
            else -> {
                ivPayment.setImageResource(R.drawable.img_year)
                month = 12
                price = 920
            }
        }
        // Initialize shared preferences
        val sharedPref = this.requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // get user id from shared preferences
        val userid = (sharedPref.getString("user", "0"))

        ivPayment.setOnClickListener {

            var db = FirebaseFirestore.getInstance()

            // get current date (yy-mm-dd)
            val date = java.time.LocalDate.now().toString()

            val hashMap = hashMapOf<String, Any>(
                "startDate" to date,
                "months" to month,
                "price" to price,
                "used" to 0
            )

            // create a new document in the users/subscription collection
            db.collection("users").document(userid.toString()).collection("subscription")
                .add(hashMap)
                .addOnSuccessListener {
                    // redirect user to the savingscard page after successful registration
                    findNavController().navigate(R.id.action_PaymentFragment_to_CardFragment)
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
