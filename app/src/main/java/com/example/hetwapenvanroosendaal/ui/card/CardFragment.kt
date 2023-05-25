package com.example.hetwapenvanroosendaal.ui.card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.components.EAN13Generator
import com.example.hetwapenvanroosendaal.databinding.FragmentCardBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder
import java.time.LocalDate

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
            findNavController().navigate(R.id.action_CardFragment_to_LoginFragment)
        }

        // Define period variable
        var period: String? = null
        var month: Int? = null
        var usage = 0

        // firestore instance
        var db = FirebaseFirestore.getInstance()

        // Firestore check if users collection has a underlaying subscription collection
        db.collection("users").document(userid.toString()).collection("subscription")
            .get()
            .addOnSuccessListener { documents ->
                // foreach document in the collection print
                for (document in documents) {

                    // get the month & startdate from the document
                    val startDate = document.data["startDate"].toString()
                    val months = document.data["months"].toString().toInt()
                    val used = document.data["used"].toString().toInt()

                    // Parsing the startDate string into a LocalDate object
                    val localDate = LocalDate.parse(startDate)

                    // Adding the specified number of months to the startDate
                    val futureDate = localDate.plusMonths(months.toLong())

                    // Checking if the date is before or equal to the current date
                    if (futureDate.isBefore(LocalDate.now()) || futureDate.isEqual(LocalDate.now())) {
                        // do nothing
                    }
                    else {
                        // Set the period variable to the futureDate
                        period = futureDate.toString()
                        month = months
                        usage = used
                        break
                    }
                }

                // check if period is not empty
                if (period != null && period!!.isNotEmpty()) {
                    // set period to active period text
                    var periodTxt = binding.periodTxt
                    periodTxt.text = "Je abonnement is geldig tot " + period + "."

                    // Get the amount of beers the user has left
                    var bier = (16 * month!!) // 16 beers per month
                    bier = (bier - usage) // subtract the amount of beers the user has used


                    // Define subscription fragment elements
                    var subscriptionInfo = binding.subscriptionInfo
                    subscriptionInfo.text = "Je hebt nog recht op "+ bier +" biertjes tijdens deze abonnements periode."
                }
                else {
                    // @TODO: Send user to the subscription page to renew their subscription
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }



        // Define barcode fragment elements
        var barcode = binding.barcode
        var barcodeId = binding.barcodeId


        // get user data from firestore
        db.collection("users").document(userid.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.data
                    val code = user?.get("barcode").toString()
                    val ean13CodeDrawable = EAN13Generator.generateEAN13Code(this, code)
                    barcode.setImageDrawable(ean13CodeDrawable)
                    barcodeId.text = code
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }


        // on click listener for the logout button
        _binding!!.logoutBtn.setOnClickListener {
            // clear shared preferences
            sharedPref.edit().clear().apply()

            // navigate to login page
            findNavController().navigate(R.id.action_CardFragment_to_LoginFragment)
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}