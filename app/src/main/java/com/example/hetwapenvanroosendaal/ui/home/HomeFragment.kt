package com.example.hetwapenvanroosendaal.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val db = Firebase.firestore

        val gridLayout = binding.myGridLayout

        // Set the number of columns of the grid layout to 2
        gridLayout.columnCount = 2

        db.collection("beers")
            .limit(4)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Inflate a new instance of the beer_item.xml layout
                    val beerItemView = layoutInflater.inflate(R.layout.item_beer, gridLayout, false)

                    // Set the beer name TextView to the name of the beer from the document
                    val beerNameTextView = beerItemView.findViewById<TextView>(R.id.beer_name)
                    beerNameTextView.text = document.data["name"].toString()

                    val beerImageView = beerItemView.findViewById<ImageView>(R.id.beer_image)
                    val imageResourceName = document.data["image"].toString()
                    val imageResourceId = resources.getIdentifier(imageResourceName, "drawable", requireContext().packageName)
                    beerImageView.setImageResource(imageResourceId)

                    // Set the width of the beer item view to take up half the width of the grid layout
                    val layoutParams = GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f))
                    layoutParams.width = 0
                    beerItemView.layoutParams = layoutParams

                    // Add the beer item view to the grid layout
                    gridLayout.addView(beerItemView)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}