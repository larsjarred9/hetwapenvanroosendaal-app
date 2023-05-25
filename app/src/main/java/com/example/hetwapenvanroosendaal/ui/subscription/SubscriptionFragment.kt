package com.example.hetwapenvanroosendaal.ui.subscription

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentSubscriptionBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class SubscriptionFragment : Fragment() {

    private var _binding: FragmentSubscriptionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)

        //Set the header texts of the page
        _binding!!.homeHeader.pageTitle.text = requireContext().getString(R.string.subscription)
        _binding!!.homeHeader.pageDescription.text = requireContext().getString(R.string.subscription_description)

        //Array of imageViews
        val imageViews = arrayOf(
            _binding!!.imgMonth,
            _binding!!.imgQuarter,
            _binding!!.imgYear
        )

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

        //Init selected image
        var selectedImage: String? = null

        // Set OnClickListener for each ImageView
        for (imageView in imageViews) {
            imageView.setOnClickListener {
                // Remove border from all images
                for (iv in imageViews) {
                    iv.setBackgroundResource(0) // Remove border/background resource
                }

                // Set border for the clicked image
                val borderDrawable = GradientDrawable()
                borderDrawable.setStroke(
                    dpToPx(5),
                    resources.getColor(R.color.red, null)
                )

                //Set border for image
                imageView.background = borderDrawable

                //Save selected image id
                selectedImage = resources.getResourceEntryName(imageView.id)
            }
        }

        //Add onclick listener to subscribe button
        _binding!!.subBtn.setOnClickListener {
            if (selectedImage != null) {
                //Save price of clicked image
                val price : Int = when (selectedImage) {
                    "imgMonth" -> {
                        77
                    }
                    "imgQuarter" -> {
                        235
                    }
                    else -> {
                        920
                    }
                }

                //Add price to bundle
                val b = Bundle()
                b.putInt("price", price)

                //Switch to payment fragment and pass the bundle
                findNavController().navigate(R.id.action_subFragment_to_payFragment, b)
            }
        }

        return binding.root
    }

    //Function to convert dp to pixels
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
