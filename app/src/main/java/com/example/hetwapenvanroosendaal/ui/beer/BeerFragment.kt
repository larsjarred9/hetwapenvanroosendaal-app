package com.example.hetwapenvanroosendaal.ui.beer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentBeerBinding

class BeerFragment : Fragment() {

    private var _binding: FragmentBeerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerBinding.inflate(inflater, container, false)

        //Set the header texts of the page
        _binding!!.header.pageTitle.text = requireContext().getString(R.string.beer)
        _binding!!.header.pageDescription.text = requireContext().getString(R.string.beer_description)

        //Get webview by ID
        val webView = binding.root.findViewById<WebView>(R.id.beerWebView)

        //Override function when link is clicked withing webview
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                //Get url
                val url = request.url?.toString()
                try {
                    //Open browser with the url
                    url?.let {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.e("WebView", "Error opening URL: $url", e)
                }
                return true // Indicate that the URL loading is handled externally
            }
        }

        // Enable JavaScript
        webView.settings.javaScriptEnabled = true

        // Load the website
        webView.loadUrl("https://bouwmarkt.speetjens.net/bier.html")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}