package com.example.hetwapenvanroosendaal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hetwapenvanroosendaal.R
import com.google.firebase.firestore.DocumentSnapshot

class BeerAdapter(private val beers: List<DocumentSnapshot>) :
  RecyclerView.Adapter<BeerAdapter.ViewHolder>() {

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.beer_image)
    val textView: TextView = itemView.findViewById(R.id.beer_name)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_beer, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val document = beers[position]
    // Set the image resource for the ImageView here
    holder.textView.text = document.data?.get("name").toString()
  }

  override fun getItemCount() = beers.size

}
