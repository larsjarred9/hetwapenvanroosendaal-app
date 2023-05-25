package com.example.hetwapenvanroosendaal.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.util.Random

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

        // on click listener for the registerLink
        _binding!!.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
        }

        // on click listener for the register button
        _binding!!.registerBtn.setOnClickListener {

            val url = "https://hetwapen.projects.adainforma.tk/api/v1/register"

            val firstName = binding.firstNameField.text.toString()
            val lastName = binding.lastNameField.text.toString()
            val email = binding.emailField.text.toString()
            val password = binding.passwordFielkd.text.toString()

            //Validation status
            var isFormValid = true

            //Check empty
            if (firstName.isEmpty()) {
                //Set field error
                binding.firstNameField.error = "Please enter your first name"
                //Update validation status
                isFormValid = false
            }

            //Check empty
            if (lastName.isEmpty()) {
                //Set field error
                binding.lastNameField.error = "Please enter your last name"
                //Update validation status
                isFormValid = false
            }

            //Check empty and valid email
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //Set field error
                binding.emailField.error = "Please enter a valid email address"
                //Update validation status
                isFormValid = false
            }

            //Regex password pattern
            val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}\$".toRegex()
            //Check empty and check if password matches regular expression
            if (password.isEmpty() || !password.matches(passwordPattern)) {
                //Set field error
                binding.passwordFielkd.error = "Password must be at least 8 characters and include at least one letter, one number, and one special character"
                //Update validation status
                isFormValid = false
            }

            //If form is not successfully validated
            if (!isFormValid) {
                //Show error and stop code
                Toast.makeText(this.requireContext(), "Please correct the errors above", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val barcode = generateRandomNumber()

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
                        "email" to email,
                        "barcode" to barcode
                    )

                    // get the user id from the response
                    val userId = JSONObject(response).getJSONObject("data").getJSONObject("token").getInt("tokenable_id")

                    // add the user to the firestore db with the user id as the document id
                    db.collection("users").document(userId.toString())
                        .set(user)
                        .addOnSuccessListener {
                            //Success message
                            Toast.makeText(this.requireContext(), "Your account has been successfully created", Toast.LENGTH_SHORT).show()
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

    private fun generateRandomNumber(): String {
        val timestamp = System.currentTimeMillis().toString()
        val random = Random()
        val stringBuilder = StringBuilder()

        stringBuilder.append(timestamp)

        while (stringBuilder.length < 12) {
            stringBuilder.append(random.nextInt(10))
        }

        return stringBuilder.toString().substring(0, 12)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}