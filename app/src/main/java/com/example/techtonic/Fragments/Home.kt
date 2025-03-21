//package com.example.techtonic.Fragments
//
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.location.Location
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.SearchView
//import android.widget.Toast
//import androidx.activity.OnBackPressedCallback
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.techtonic.Activity.HazardReport
//import com.example.techtonic.R
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import java.io.IOException
//
//class Home : Fragment(), OnMapReadyCallback {
//
//    private lateinit var btnadd: ImageButton
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private var mMap: GoogleMap? = null
//    private lateinit var currentLocation: Location
//    private val permission = 101
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//
//        btnadd = view.findViewById(R.id.addreport)
//        btnadd.setOnClickListener {
//            context?.let {
//                startActivity(Intent(it, HazardReport::class.java))
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    showExitConfirmationDialog()
//                }
//            }
//        )
//
//        val searchView = view.findViewById<SearchView>(R.id.searchLocation)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (!query.isNullOrEmpty()) {
//                    searchLocation(query)
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean = false
//        })
//
//        return view
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        if (isAdded) {
//            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//            val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
//            mapFragment.getMapAsync(this)
//
//            getCurrentLocationUser()
//        }
//    }
//        private fun showExitConfirmationDialog() {
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Exit Application")
//            builder.setMessage("Are you sure you want to exit this application?")
//
//            builder.setPositiveButton("Yes") { _, _ ->
//                requireActivity().finishAffinity()
//            }
//
//            builder.setNegativeButton("No") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }
//
//    private fun getCurrentLocationUser() {
//        if (isAdded) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(
//                    requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                    permission
//                )
//                return
//            }
//
//            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
//                if (location != null) {
//                    currentLocation = location
//                    context?.let {
//                        Toast.makeText(it, "${currentLocation.latitude}, ${currentLocation.longitude}", Toast.LENGTH_LONG).show()
//                    }
//
//                    mMap?.let {
//                        onMapReady(it)
//                    }
//                } else {
//                    context?.let {
//                        Toast.makeText(it, "Unable to fetch location", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }.addOnFailureListener {
//                context?.let {
//                    Toast.makeText(it, "Failed to get location", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun searchLocation(query: String) {
//        if (isAdded) {
//            val geocoder = Geocoder(requireContext())
//            try {
//                val addressList = geocoder.getFromLocationName(query, 1)
//                if (addressList != null && addressList.isNotEmpty()) {
//                    val address = addressList[0]
//                    val latLng = LatLng(address.latitude, address.longitude)
//                    mMap?.clear()
//                    mMap?.addMarker(MarkerOptions().position(latLng).title(query))
//                    mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
//                    context?.let {
//                        Toast.makeText(it, "Location found: ${address.locality}", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    context?.let {
//                        Toast.makeText(it, "Location not found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//                context?.let {
//                    Toast.makeText(it, "Error fetching location", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == permission && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocationUser()
//        } else {
//            context?.let {
//                Toast.makeText(it, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        if (::currentLocation.isInitialized) {
//            val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
//            val markerOptions = MarkerOptions().position(latLng).title("Ari ka gapuyo nimal ka")
//            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//            mMap?.addMarker(markerOptions)
//        }
//    }
//
//}
//
//
//
//package com.example.techtonic.Fragments
//
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.techtonic.Activity.HazardReport
//import com.example.techtonic.R
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.firebase.database.*
//
//class Home : Fragment(), OnMapReadyCallback {
//
//    private lateinit var btnAdd: ImageButton
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private lateinit var database: DatabaseReference
//    private var mMap: GoogleMap? = null
//    private val permission = 101
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        btnAdd = view.findViewById(R.id.addreport)
//        btnAdd.setOnClickListener {
//            context?.let {
//                startActivity(Intent(it, HazardReport::class.java))
//            }
//        }
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        database = FirebaseDatabase.getInstance().getReference("hazardReports")
//
//        return view
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        val database = FirebaseDatabase.getInstance().reference.child("hazardReports")
//
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                mMap!!.clear() // Clear old markers
//                for (reportSnapshot in snapshot.children) {
//                    val latitude = reportSnapshot.child("latitude").getValue(Double::class.java)
//                    val longitude = reportSnapshot.child("longitude").getValue(Double::class.java)
//                    val hazardType = reportSnapshot.child("hazardType").getValue(String::class.java)
//                    val location = reportSnapshot.child("location").getValue(String::class.java)
//
//                    if (latitude != null && longitude != null) {
//                        val reportLocation = LatLng(latitude, longitude)
//                        mMap!!.addMarker(
//                            MarkerOptions()
//                                .position(reportLocation)
//                                .title("$hazardType at $location")
//                        )
//                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(reportLocation, 15f))
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("HomeFragment", "Failed to load markers", error.toException())
//            }
//        })
//    }
//
//
//    private fun getCurrentLocationUser() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                permission
//            )
//            return
//        }
//
//        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
//            location?.let {
//                val currentLatLng = LatLng(it.latitude, it.longitude)
//                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
//            } ?: Toast.makeText(context, "Unable to fetch location", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun fetchReportsFromFirebase() {
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                mMap?.clear() // Clear existing markers
//                for (data in snapshot.children) {
//                    val lat = data.child("latitude").getValue(Double::class.java)
//                    val lng = data.child("longitude").getValue(Double::class.java)
//                    val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Hazard"
//
//                    if (lat != null && lng != null) {
//                        val location = LatLng(lat, lng)
//                        mMap?.addMarker(MarkerOptions().position(location).title(hazardType))
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("HomeFragment", "Failed to load markers", error.toException())
//                Toast.makeText(context, "Failed to load hazard locations", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == permission && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocationUser()
//        } else {
//            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//}


package com.example.techtonic.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
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
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class Home : Fragment(), OnMapReadyCallback {

    private lateinit var btnAdd: ImageButton
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var mMap: GoogleMap? = null
    private val permission = 101
    private var userLocation: Location? = null
    private val radiusInMeters = 3000.0 // 3 km radius

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        btnAdd = view.findViewById(R.id.addreport)
        btnAdd.setOnClickListener {
            startActivity(Intent(requireContext(), HazardReport::class.java))
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("hazardReports")

        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getCurrentLocationUser()

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fetchReportsFromFirebase()
    }

    private fun fetchReportsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mMap?.clear() // Clear old markers
                userLocation?.let { location ->
                    for (data in snapshot.children) {
                        val lat = data.child("latitude").getValue(Double::class.java)
                        val lng = data.child("longitude").getValue(Double::class.java)
                        val hazardType = data.child("hazardType").getValue(String::class.java) ?: "Hazard"
                        val reportLocation = data.child("location").getValue(String::class.java) ?: "Unknown Location"
                        val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""

                        if (lat != null && lng != null) {
                            val reportLatLng = LatLng(lat, lng)
                            val distance = calculateDistance(location.latitude, location.longitude, lat, lng)

                            if (distance <= radiusInMeters) {
                                val marker = mMap?.addMarker(
                                    MarkerOptions()
                                        .position(reportLatLng)
                                        .title(hazardType)
                                        .snippet(reportLocation) // This is displayed under the title
                                        .icon(bitmapDescriptorFromVector(R.drawable.hazard))
                                )

                                // Store marker data
                                marker?.tag = imageUrl
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to load hazard locations", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Set custom InfoWindow adapter
        mMap?.setInfoWindowAdapter(CustomInfoWindowAdapter())
    }
    private inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private val window = LayoutInflater.from(requireContext()).inflate(R.layout.custom_info_window, null)

        override fun getInfoWindow(marker: Marker): View? {
            return null // Use default InfoWindow background
        }

        override fun getInfoContents(marker: Marker): View? {
            val hazardTypeText = window.findViewById<TextView>(R.id.hazardType)
            val locationText = window.findViewById<TextView>(R.id.hazardLocation)
            val imageView = window.findViewById<ImageView>(R.id.hazardImage)

            hazardTypeText.text = marker.title
            locationText.text = marker.snippet

            val imageUrl = marker.tag as? String
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.pothole_image) // Placeholder image
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.pothole_image)
            }

            return window
        }
    }
    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable: Drawable? = ContextCompat.getDrawable(requireContext(), vectorResId)
        if (vectorDrawable == null) {
            Log.e("HomeFragment", "Vector drawable not found")
            return BitmapDescriptorFactory.defaultMarker() // Fallback
        }

        // Scale down to approximately 5cm x 5cm (around 30px x 30px)
        val width = 100
        val height = 100
        vectorDrawable.setBounds(0, 0, width, height)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getCurrentLocationUser() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null && isAdded) {
                userLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

                mMap?.addCircle(
                    CircleOptions()
                        .center(currentLatLng)
                        .radius(radiusInMeters)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(70, 255, 0, 0)) // Semi-transparent fill
                        .strokeWidth(2f)
                )
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
            } else if (isAdded) {
                Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            if (isAdded) {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Location permission is needed to show hazards nearby", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            permission
        )
    }

    private fun calculateDistance(userLat: Double, userLng: Double, reportLat: Double, reportLng: Double): Float {
        val userLocation = Location("").apply {
            latitude = userLat
            longitude = userLng
        }
        val reportLocation = Location("").apply {
            latitude = reportLat
            longitude = reportLng
        }
        return userLocation.distanceTo(reportLocation)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permission && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationUser()
        } else if (isAdded) {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
