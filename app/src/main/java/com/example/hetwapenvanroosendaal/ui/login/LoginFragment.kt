package com.example.hetwapenvanroosendaal.ui.login

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
import com.example.hetwapenvanroosendaal.databinding.FragmentLoginBinding
import org.json.JSONObject
import java.net.URLEncoder

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        //Set the header texts of the page
        _binding!!.homeHeader.pageTitle.text = requireContext().getString(R.string.login)
        _binding!!.homeHeader.pageDescription.text = requireContext().getString(R.string.login_description)

        // Initialize shared preferences
        val sharedPref = this.requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // get user id from shared preferences
        val userid = (sharedPref.getString("user", "0"))

        // check if user id is set and not empty
        if (userid != null && userid != "0") {

            findNavController().navigate(R.id.action_LoginFragment_to_CardFragment)
        }

        // on click listener for the registerLink
        _binding!!.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        // on click listener for the login button
        _binding!!.loginBtn.setOnClickListener {

            val url = "https://hetwapen.projects.adainforma.tk/api/v1/login"
            val email = binding.emailField.text.toString()
            val password = binding.passwordFielkd.text.toString()

            val requestQueue = Volley.newRequestQueue(this.requireContext())

            val stringRequest = object : StringRequest(Method.POST, url,
                { response ->
                    // Get the user id from a successful login
                    val jsonResponse = JSONObject(response)
                    val userId = jsonResponse.getJSONObject("data").getJSONObject("user").getInt("id")

                    // Set the user id in shared preferences
                    val editor = sharedPref.edit()
                    editor.remove("userId") // Make sure to remove the old user id if it exists
                    editor.putString("user", userId.toString()) // Add the user id
                    editor.apply()

                    // Redirect to the savings fragment
                    findNavController().navigate(R.id.action_LoginFragment_to_CardFragment)
                },
                { error ->
                    println(error)
                }
            ) {
                override fun getBodyContentType(): String {
                    return "application/x-www-form-urlencoded; charset=UTF-8"
                }

                override fun getBody(): ByteArray {
                    val params = HashMap<String, String>()
                    params["email"] = email
                    params["password"] = password

                    val postData = StringBuilder()
                    for ((key, value) in params) {
                        if (postData.isNotEmpty()) {
                            postData.append("&")
                        }
                        postData.append(URLEncoder.encode(key, "UTF-8"))
                        postData.append("=")
                        postData.append(URLEncoder.encode(value, "UTF-8"))
                    }

                    return postData.toString().toByteArray(Charsets.UTF_8)
                }
            }

            requestQueue.add(stringRequest)

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}