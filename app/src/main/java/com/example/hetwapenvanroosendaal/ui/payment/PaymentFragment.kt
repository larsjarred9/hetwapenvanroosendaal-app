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
import java.time.LocalDate

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


        // Initialize shared preferences
        val sharedPref = this.requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // get user id from shared preferences
        val userid = (sharedPref.getString("user", "0"))

        // Initalize db
        var db = FirebaseFirestore.getInstance()

        // Check if user has a valid subscription
        db.collection("users").document(userid.toString()).collection("subscription")
            .get()
            .addOnSuccessListener { documents ->
                // foreach document in the collection print
                for (document in documents) {

                    // get the month & startdate from the document
                    val startDate = document.data["startDate"].toString()
                    val months = document.data["months"].toString().toInt()

                    // Parsing the startDate string into a LocalDate object
                    val localDate = LocalDate.parse(startDate)

                    // Adding the specified number of months to the startDate
                    val futureDate = localDate.plusMonths(months.toLong())

                    // Checking if the date is before or equal to the current date
                    if (futureDate.isBefore(LocalDate.now()) || futureDate.isEqual(LocalDate.now())) {
                        // do nothing
                    }
                    else {
                        // Send user to card page
                        findNavController().navigate(R.id.action_PaymentFragment_to_CardFragment)
                        break
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }

        // Create variables for price and month
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

        ivPayment.setOnClickListener {

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
