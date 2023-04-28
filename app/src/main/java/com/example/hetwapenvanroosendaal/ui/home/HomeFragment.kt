package com.example.hetwapenvanroosendaal.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val db = Firebase.firestore

        val gridLayout = binding.myGridLayout

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

                    // Set the beer image ImageView to a placeholder image
                    val beerImageView = beerItemView.findViewById<ImageView>(R.id.beer_image)
                    beerImageView.setImageResource(R.drawable.logo)

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