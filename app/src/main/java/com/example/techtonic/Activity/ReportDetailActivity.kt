//package com.example.techtonic.Activity
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.os.Bundle
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.activity.OnBackPressedCallback
//import androidx.appcompat.app.AppCompatActivity
//import com.example.techtonic.R
//import com.bumptech.glide.Glide
//
//class ReportDetailActivity : AppCompatActivity() {
//    private lateinit var backButton: ImageButton
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_report_detail)
//
//        val hazardType = intent.getStringExtra("hazardType")
//        val description = intent.getStringExtra("description")
//        val location = intent.getStringExtra("location")
//        val imageUrl = intent.getStringExtra("imageUrl")
//        backButton = findViewById(R.id.back_button)
//
//
//
//        findViewById<TextView>(R.id.detail_title).text = hazardType
//        findViewById<TextView>(R.id.detail_location).text = location
//        findViewById<TextView>(R.id.detail_description).text = description
//
//        val imageView = findViewById<ImageView>(R.id.detail_image)
//        Glide.with(this)
//            .load(imageUrl)
//            .placeholder(R.drawable.pothole_image)
//            .into(imageView)
//
//
//        backButton.setOnClickListener {
//            onBackPressed() // or finish()
//        }
//    }
//    override fun onBackPressed() {
//        setResult(Activity.RESULT_OK) // Return to the calling activity
//        finish()
//    }
//}


package com.example.techtonic.Activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.techtonic.R
import com.bumptech.glide.Glide

class ReportDetailActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        // Initialize views
        backButton = findViewById(R.id.backButton)
        val titleTextView = findViewById<TextView>(R.id.detail_title)
        val locationTextView = findViewById<TextView>(R.id.detail_location)
        val descriptionTextView = findViewById<TextView>(R.id.detail_description)
        val imageView = findViewById<ImageView>(R.id.detail_image)

        // Get intent data
        val hazardType = intent.getStringExtra("hazardType") ?: "N/A"
        val description = intent.getStringExtra("description") ?: "No description available"
        val location = intent.getStringExtra("location") ?: "Location unknown"
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set data to views
        titleTextView.text = hazardType
        locationTextView.text = location
        descriptionTextView.text = description

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.pothole_image)
            .into(imageView)

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close activity and go back
        }
    }
}
