package com.example.techtonic.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.techtonic.Class.Reports
import com.example.techtonic.R
import com.example.techtonic.ReportAdapter
import com.google.firebase.database.*
import java.util.Locale

class Report : Fragment() {

    private lateinit var reportAdapter: ReportAdapter
    private lateinit var listView: ListView
    private lateinit var database: DatabaseReference
    private lateinit var searchView: SearchView
    private val reports = mutableListOf<Reports>()
    private val filteredReports = mutableListOf<Reports>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        listView = view.findViewById(R.id.recycler_view_reports)
        searchView = view.findViewById(R.id.search_bar)


        database = FirebaseDatabase.getInstance().getReference("hazardReports")
        reportAdapter = ReportAdapter(requireContext(), filteredReports)
        listView.adapter = reportAdapter

        fetchReportsFromFirebase()

        setupSearchView()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            }
        )



        return view
    }


    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit Application")
        builder.setMessage("Are you sure you want to exit this application?")

        builder.setPositiveButton("Yes") { _, _ ->
            requireActivity().finishAffinity()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
private fun fetchReportsFromFirebase() {
    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            reports.clear()
            for (data in snapshot.children) {
                val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Unknown"
                val description = data.child("description").getValue(String::class.java) ?: ""
                val location = data.child("location").getValue(String::class.java) ?: ""
                val status = "Pending" // Default status
                val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""

                val reportItem = Reports(hazardType,location, status, R.drawable.pothole_image, description, imageUrl)
                reports.add(reportItem)
            }
            reports.reverse()
            filteredReports.clear()
            filteredReports.addAll(reports)
            reportAdapter.notifyDataSetChanged()
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

    private fun filterReports(query: String?) {
        filteredReports.clear()
        if (!query.isNullOrEmpty()) {
            val searchQuery = query.lowercase(Locale.getDefault())
            filteredReports.addAll(reports.filter { it.hazardType.lowercase(Locale.getDefault()).contains(searchQuery) })
        } else {
            filteredReports.addAll(reports)
        }
        reportAdapter.notifyDataSetChanged()
    }
}


