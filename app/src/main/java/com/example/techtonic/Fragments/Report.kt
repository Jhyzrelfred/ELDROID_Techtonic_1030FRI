//package com.example.techtonic.Fragments
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ListView
//import android.widget.SearchView
//import android.widget.Toast
//import androidx.activity.OnBackPressedCallback
//import androidx.appcompat.app.AlertDialog
//import com.example.techtonic.Class.Reports
//import com.example.techtonic.R
//import com.example.techtonic.ReportAdapter
//import com.google.firebase.database.*
//import java.util.Locale
//
//class Report : Fragment() {
//
//    private lateinit var reportAdapter: ReportAdapter
//    private lateinit var listView: ListView
//    private lateinit var database: DatabaseReference
//    private lateinit var searchView: SearchView
//    private val reports = mutableListOf<Reports>()
//    private val filteredReports = mutableListOf<Reports>()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_report, container, false)
//
//        listView = view.findViewById(R.id.recycler_view_reports)
//        searchView = view.findViewById(R.id.search_bar)
//
//
//        database = FirebaseDatabase.getInstance().getReference("hazardReports")
//        reportAdapter = ReportAdapter(requireContext(), filteredReports)
//        listView.adapter = reportAdapter
//
//        fetchReportsFromFirebase()
//
//        setupSearchView()
////        requireActivity().onBackPressedDispatcher.addCallback(
////            viewLifecycleOwner,
////            object : OnBackPressedCallback(true) {
////                override fun handleOnBackPressed() {
////                    showExitConfirmationDialog()
////                }
////            }
////        )
//
//
//
//        return view
//    }
//
//
//private fun fetchReportsFromFirebase() {
//    database.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            reports.clear()
//            for (data in snapshot.children) {
//                val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Unknown"
//                val description = data.child("description").getValue(String::class.java) ?: ""
//                val location = data.child("location").getValue(String::class.java) ?: ""
//                val status = "Pending" // Default status
//                val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""
//
//                val reportItem = Reports(hazardType,location, status, R.drawable.pothole_image, description, imageUrl)
//                reports.add(reportItem)
//            }
//            reports.reverse()
//            filteredReports.clear()
//            filteredReports.addAll(reports)
//            reportAdapter.notifyDataSetChanged()
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.e("ReportFragment", "Failed to read reports from Firebase", error.toException())
//            Toast.makeText(requireContext(), "Error loading reports", Toast.LENGTH_SHORT).show()
//        }
//    })
//}
//
//
//    private fun setupSearchView() {
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                filterReports(query)
//                return true
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterReports(newText)
//                return true
//            }
//        })
//    }
//
//    private fun filterReports(query: String?) {
//        filteredReports.clear()
//        if (!query.isNullOrEmpty()) {
//            val searchQuery = query.lowercase(Locale.getDefault())
//            filteredReports.addAll(reports.filter { it.hazardType.lowercase(Locale.getDefault()).contains(searchQuery) })
//        } else {
//            filteredReports.addAll(reports)
//        }
//        reportAdapter.notifyDataSetChanged()
//    }
//}
//
//
//package com.example.techtonic.Fragments
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageButton
//import android.widget.ListView
//import android.widget.SearchView
//import android.widget.Toast
//import com.example.techtonic.Activity.MainActivity
//import com.example.techtonic.Authentication.SignUp
//import com.example.techtonic.Class.Reports
//import com.example.techtonic.R
//import com.example.techtonic.ReportAdapter
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import java.util.Locale
//
//class Report : Fragment() {
//
//    private lateinit var reportAdapter: ReportAdapter
//    private lateinit var listView: ListView
//    private lateinit var database: DatabaseReference
//    private lateinit var searchView: SearchView
//    private lateinit var backbutton: ImageButton
//    private val reports = mutableListOf<Reports>()
//    private val filteredReports = mutableListOf<Reports>()
//    private lateinit var userId: String
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_report, container, false)
//
//        listView = view.findViewById(R.id.recycler_view_reports)
//        searchView = view.findViewById(R.id.search_bar)
//        backbutton = view.findViewById(R.id.back_button)
//
//        // Get the current user's UID
//        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//
//        database = FirebaseDatabase.getInstance().getReference("hazardReports")
//        reportAdapter = ReportAdapter(requireContext(), filteredReports)
//        listView.adapter = reportAdapter
//
//        fetchReportsFromFirebase()
//        setupSearchView()
//        backbutton.setOnClickListener {
//            val fragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//
//            // Add custom animations (slide in/out)
//            fragmentTransaction.setCustomAnimations(
//                R.anim.slide_in_left,  // Enter animation
//                R.anim.slide_out_right, // Exit animation
//                R.anim.slide_in_right,  // Pop enter animation (for back)
//                R.anim.slide_out_left   // Pop exit animation
//            )
//
//            if (fragmentManager.backStackEntryCount > 0) {
//                fragmentManager.popBackStack()
//            } else {
//                fragmentTransaction.replace(R.id.container, Home())
//                    .addToBackStack(null)
//                    .commit()
//
////                val fragmentTransaction = parentFragmentManager.beginTransaction()
////                fragmentTransaction.replace(R.id.fragment_cont, Home())
////                    .addToBackStack(null)
////                    .commit()
//            }
//        }
//
//
//
//        return view
//    }
//
//    private fun fetchReportsFromFirebase() {
//        database.orderByChild("userId").equalTo(userId)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    reports.clear()
//                    for (data in snapshot.children) {
//                        val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Unknown"
//                        val description = data.child("description").getValue(String::class.java) ?: ""
//                        val location = data.child("location").getValue(String::class.java) ?: ""
//                        val status = "Pending"
//                        val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""
//
//                        val reportItem = Reports(hazardType, location, status, R.drawable.pothole_image, description, imageUrl)
//                        reports.add(reportItem)
//                    }
//                    reports.reverse()
//                    filteredReports.clear()
//                    filteredReports.addAll(reports)
//                    reportAdapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("ReportFragment", "Failed to read reports from Firebase", error.toException())
//                    Toast.makeText(requireContext(), "Error loading reports", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
//
//    private fun setupSearchView() {
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                filterReports(query)
//                return true
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterReports(newText)
//                return true
//            }
//        })
//    }
//
//    private fun filterReports(query: String?) {
//        filteredReports.clear()
//        if (!query.isNullOrEmpty()) {
//            val searchQuery = query.lowercase(Locale.getDefault())
//            filteredReports.addAll(reports.filter {
//                it.hazardType.lowercase(Locale.getDefault()).contains(searchQuery)
//            })
//        } else {
//            filteredReports.addAll(reports)
//        }
//        reportAdapter.notifyDataSetChanged()
//    }
//}



package com.example.techtonic.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.techtonic.Activity.MainActivity
import com.example.techtonic.Class.Reports
import com.example.techtonic.R
import com.example.techtonic.ReportAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Locale

class Report : Fragment() {

    private lateinit var reportAdapter: ReportAdapter
    private lateinit var listView: ListView
    private lateinit var database: DatabaseReference
    private lateinit var searchView: SearchView
    private lateinit var backButton: ImageButton
    private lateinit var pendingButton: Button
    private lateinit var inProgressButton: Button
    private lateinit var potholeButton: Button
    private lateinit var alligatorcracksButton: Button
    private lateinit var majorscallingButton: Button
    private val reports = mutableListOf<Reports>()
    private val filteredReports = mutableListOf<Reports>()
    private lateinit var userId: String
    private var currentStatus: String = "Pending"
    private var currentHazardType: String = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        listView = view.findViewById(R.id.recycler_view_reports)
        searchView = view.findViewById(R.id.search_bar)
        backButton = view.findViewById(R.id.back_button)
        pendingButton = view.findViewById(R.id.pending_button)
        inProgressButton = view.findViewById(R.id.in_progress_button)
        potholeButton = view.findViewById(R.id.pothole_button)
        alligatorcracksButton = view.findViewById(R.id.alligatorcracks_button)
        majorscallingButton = view.findViewById(R.id.majorscalling_button)

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        database = FirebaseDatabase.getInstance().getReference("hazardReports")
        reportAdapter = ReportAdapter(requireContext(), filteredReports)
        listView.adapter = reportAdapter

        fetchReportsFromFirebase()
        setupSearchView()
//        setupFilterButtons()

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_cont, Home())
                .addToBackStack(null)
                .commit()
        }
        

//        val hazardToggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.hazard_toggle_group)
//
//        hazardToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
//            if (!isChecked) {
//                val button = group.findViewById<MaterialButton>(checkedId)
//                button?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.button_filter_selector)
//            }
//        }



        return view
    }

    private fun fetchReportsFromFirebase() {
        database.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    reports.clear()
                    for (data in snapshot.children) {
                        val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Unknown"
                        val description = data.child("description").getValue(String::class.java) ?: ""
                        val location = data.child("location").getValue(String::class.java) ?: ""
                        val status = data.child("status").getValue(String::class.java) ?: "Pending"
                        val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""

                        val reportItem = Reports(hazardType, location, status, R.drawable.pothole_image, description, imageUrl)
                        reports.add(reportItem)
                    }
                    reports.reverse()
                    filterReports()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ReportFragment", "Failed to read reports from Firebase", error.toException())
                    Toast.makeText(requireContext(), "Error loading reports", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterReports(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterReports(newText)
                return true
            }
        })
    }

//    private fun setupFilterButtons() {
//        val buttons = listOf(potholeButton, alligatorcracksButton, majorscallingButton)
//
//        for (button in buttons) {
//            button.setOnClickListener {
//                // Reset all buttons to unselected state
//                for (btn in buttons) {
//                    btn.isSelected = false
//                    btn.setTextColor(resources.getColor(R.color.black, null))
//                }
//
//                // Set clicked button to selected state
//                button.isSelected = true
//                button.setTextColor(resources.getColor(R.color.white, null))
//
//                // Update filter
//                currentHazardType = button.text.toString()
//                filterReports()
//            }
//        }
//    }


//    private fun setupFilterButtons() {
//        val buttons = listOf(
//            potholeButton as com.google.android.material.button.MaterialButton,
//            alligatorcracksButton as com.google.android.material.button.MaterialButton,
//            majorscallingButton as com.google.android.material.button.MaterialButton,)
//
//        for (button in buttons) {
//            button.setOnClickListener {
//                // Reset all buttons to unchecked
//                buttons.forEach { it.isChecked = false }
//
//                // Set clicked button as checked
//                (it as com.google.android.material.button.MaterialButton).isChecked = true
//
//                // Update current filter
//                currentHazardType = it.text.toString()
//                filterReports()
//            }
//        }
//    }



    private fun filterReports(query: String? = null) {
        filteredReports.clear()
        val searchQuery = query?.lowercase(Locale.getDefault()) ?: ""

        val filtered = reports.filter { report ->
            val matchesStatus = report.status == currentStatus
            val matchesHazard = currentHazardType == "All" || report.hazardType == currentHazardType
            val matchesSearch = report.hazardType.lowercase(Locale.getDefault()).contains(searchQuery)
            matchesStatus && matchesHazard && matchesSearch
        }

        filteredReports.addAll(filtered)
        reportAdapter.notifyDataSetChanged()
    }
}
