package com.example.techtonic.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtonic.Class.SignupClass
import com.example.techtonic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {


    private lateinit var fullnameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var Login: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        fullnameEditText = findViewById(R.id.fullnameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        passwordEditText = findViewById(R.id.passEditText)
        confirmPasswordEditText = findViewById(R.id.conpassEditText)
        Login = findViewById(R.id.login)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            registerUser()
        }

        Login.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val fullname = fullnameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (fullname.isEmpty()) {
            fullnameEditText.error = "Fullname is required"
            fullnameEditText.requestFocus()
            return
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Enter a valid email"
            emailEditText.requestFocus()
            return
        }

        if (phone.isEmpty() || phone.length != 11) {
            phoneEditText.error = "Enter a valid phone number"
            phoneEditText.requestFocus()
            return
        }

        if (password.isEmpty() || password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters"
            passwordEditText.requestFocus()
            return
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            confirmPasswordEditText.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveUserData(email, fullname, phone)
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                    if (verifyTask.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Registration successful. Please check your email for verification.",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(Intent(this, SignIn::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            "Error in sending verification email: ${verifyTask.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Registration failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun saveUserData(email: String, fullName: String, phoneNumber: String) {
        val userId = auth.currentUser?.uid
        Log.d("Firebase", "Saving data for user ID: $userId")

        if (userId == null) {
            Log.w("Firebase", "User ID is null. Cannot save data.")
            return
        }

        val userRef = database.child("users").child(userId)

        val userData = SignupClass(email, fullName, phoneNumber)

        Log.d("Firebase", "User data: $userData")

        userRef.setValue(userData)
            .addOnSuccessListener {
                Log.d("Firebase", "User data saved successfully")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error saving user data", e)
            }
    }

}



