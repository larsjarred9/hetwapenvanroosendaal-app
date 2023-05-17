package com.example.hetwapenvanroosendaal.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hetwapenvanroosendaal.R
import com.example.hetwapenvanroosendaal.databinding.FragmentRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import java.net.URLEncoder

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        //Set the header texts of the page
        _binding!!.homeHeader.pageTitle.text = requireContext().getString(R.string.register)
        _binding!!.homeHeader.pageDescription.text = requireContext().getString(R.string.register_description)


        // on click listener for the register button
        _binding!!.loginBtn.setOnClickListener {

            Log.e(VolleyLog.TAG, "Button is clicked")

            val url = "https://hetwapen.projects.adainforma.tk/api/v1/register"
            val firstName = binding.firstNameField.text.toString()
            val lastName = binding.lastNameField.text.toString()
            val email = binding.emailField.text.toString()
            val password = binding.passwordFielkd.text.toString()

            val requestQueue = Volley.newRequestQueue(this.requireContext())

            val stringRequest = object : StringRequest(
                Method.POST, url,
                { response ->
                    Log.e(VolleyLog.TAG, response.toString())
                    // firestore db instance
                    val db = FirebaseFirestore.getInstance()

                    // create a new user in the firestore db
                    val user = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email
                    )

                    // get the user id from the response
                    val userId = JSONObject(response).getJSONObject("data").getJSONObject("token").getInt("tokenable_id")

                    // add the user to the firestore db with the user id as the document id
                    db.collection("users").document(userId.toString())
                        .set(user)
                        .addOnSuccessListener {

                            // redirect user to the login page after successful registration
                            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
                        }
                        .addOnFailureListener { e -> Log.w("RegisterFragment", "Error creating user values", e) }
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
                    params["first_name"] = firstName
                    params["last_name"] = lastName
                    params["email"] = email
                    params["password"] = password
                    params["password_confirmation"] = password

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