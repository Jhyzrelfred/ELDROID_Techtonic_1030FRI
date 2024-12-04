package com.example.techtonic.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtonic.R
import com.google.firebase.auth.FirebaseAuth

class Verification : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        auth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()

        val user = auth.currentUser
        user?.reload()?.addOnCompleteListener {
            if (user.isEmailVerified) {
                Toast.makeText(this, "Email is verified!", Toast.LENGTH_SHORT).show()
                    } else {
                Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

