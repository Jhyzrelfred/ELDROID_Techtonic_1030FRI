package com.example.techtonic.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.techtonic.R
import com.bumptech.glide.Glide

class ReportDetailActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        val hazardType = intent.getStringExtra("hazardType")
        val description = intent.getStringExtra("description")
        val location = intent.getStringExtra("location")
        val imageUrl = intent.getStringExtra("imageUrl")



        findViewById<TextView>(R.id.detail_title).text = hazardType
        findViewById<TextView>(R.id.detail_location).text = location
        findViewById<TextView>(R.id.detail_description).text = description

        val imageView = findViewById<ImageView>(R.id.detail_image)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.pothole_image)
            .into(imageView)
    }
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK) 
        finish()
    }
}
