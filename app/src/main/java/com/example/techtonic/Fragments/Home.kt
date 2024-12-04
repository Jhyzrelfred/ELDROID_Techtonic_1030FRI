package com.example.techtonic.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.techtonic.Activity.HazardReport
import com.example.techtonic.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class Home : Fragment(), OnMapReadyCallback {

    private lateinit var btnadd: ImageButton
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    private lateinit var currentLocation: Location
    private val permission = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        btnadd = view.findViewById(R.id.addreport)
        btnadd.setOnClickListener {
            context?.let {
                startActivity(Intent(it, HazardReport::class.java))
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            }
        )

        val searchView = view.findViewById<SearchView>(R.id.searchLocation)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchLocation(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
            mapFragment.getMapAsync(this)

            getCurrentLocationUser()
        }
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

    private fun getCurrentLocationUser() {
        if (isAdded) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    permission
                )
                return
            }

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                    context?.let {
                        Toast.makeText(it, "${currentLocation.latitude}, ${currentLocation.longitude}", Toast.LENGTH_LONG).show()
                    }

                    mMap?.let {
                        onMapReady(it)
                    }
                } else {
                    context?.let {
                        Toast.makeText(it, "Unable to fetch location", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                context?.let {
                    Toast.makeText(it, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchLocation(query: String) {
        if (isAdded) {
            val geocoder = Geocoder(requireContext())
            try {
                val addressList = geocoder.getFromLocationName(query, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap?.clear()
                    mMap?.addMarker(MarkerOptions().position(latLng).title(query))
                    mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    context?.let {
                        Toast.makeText(it, "Location found: ${address.locality}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    context?.let {
                        Toast.makeText(it, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                context?.let {
                    Toast.makeText(it, "Error fetching location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permission && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationUser()
        } else {
            context?.let {
                Toast.makeText(it, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (::currentLocation.isInitialized) {
            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("Ari ka gapuyo nimal ka")
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            mMap?.addMarker(markerOptions)
        }
    }

}



