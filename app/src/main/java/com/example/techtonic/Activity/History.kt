package com.example.techtonic.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techtonic.Class.Reports
import com.example.techtonic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class History : AppCompatActivity() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private val historyMap = mutableMapOf<String, MutableList<Reports>>() // "March 2025" -> [Reports]
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        expandableListView = findViewById(R.id.expandable_list_view)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed() // or finish() to close the activity
        }

        database = FirebaseDatabase.getInstance().getReference("hazardReports").child(userId)
        fetchCompletedReports()
    }

    private fun fetchCompletedReports() {
        database.orderByChild("status").equalTo("Completed")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyMap.clear()
                    for (data in snapshot.children) {
                        val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Unknown"
                        val description = data.child("description").getValue(String::class.java) ?: ""
                        val location = data.child("location").getValue(String::class.java) ?: ""
                        val timestamp = data.child("timestamp").getValue(String::class.java) ?: ""
                        val date = formatDate(timestamp)

                        val report = Reports(hazardType, location, "Completed", R.drawable.pothole_image, description, "")
                        historyMap.getOrPut(date) { mutableListOf() }.add(report)
                    }
                    setupExpandableListView()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HistoryActivity", "Failed to load completed reports", error.toException())
                    Toast.makeText(this@History, "Failed to load history", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun formatDate(timestamp: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = sdf.parse(timestamp)
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(date!!)
        } catch (e: Exception) {
            "Unknown Date"
        }
    }

    private fun setupExpandableListView() {
        val expandableListAdapter = HistoryAdapter(this, historyMap)
        expandableListView.setAdapter(expandableListAdapter)
    }
}
