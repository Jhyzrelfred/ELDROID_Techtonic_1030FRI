package com.example.techtonic.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.techtonic.Activity.EditProfile
import com.google.firebase.database.*
import com.example.techtonic.R
import com.example.techtonic.models.Users


class Profile : Fragment() {

    private lateinit var fullnametv: TextView
    private lateinit var Gender: TextView
    private lateinit var Edit: Button
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var database: DatabaseReference
    private val users = mutableListOf<Users>()


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        fullnametv = view.findViewById(R.id.txtfullname)
        Gender = view.findViewById(R.id.tvgender)
        emailTextView = view.findViewById(R.id.txtEmailAddress)
        phoneNumberTextView = view.findViewById(R.id.txtPhoneNumber)
        Edit = view.findViewById(R.id.btn_edit)

        val intent = Intent(requireContext(), EditProfile::class.java)


        Edit.setOnClickListener {
            startActivity(intent)
        }


        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.CAMERA
            ),
            1
        )

        database = FirebaseDatabase.getInstance().getReference("UserProfile")


        fetchUserProfilefromDatabase()

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


    private fun fetchUserProfilefromDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (data in snapshot.children) {
                    val email = data.child("email").getValue(String::class.java) ?: ""
                    val fullname = data.child("fullname").getValue(String::class.java) ?: ""
                    val phoneNumber = data.child("phoneNumber").getValue(String::class.java) ?: ""
                    val imageUrl = data.child("imageUrl").getValue(String::class.java) ?: ""
                    val gender = data.child("gender").getValue(String::class.java) ?: ""

                    val userItem = Users(email, fullname, phoneNumber, imageUrl,gender)
                    users.add(userItem)
                }

                if (users.isNotEmpty()) {
                    val firstUser = users[0]
                    fullnametv.text = firstUser.fullName
                    emailTextView.text = firstUser.email
                    phoneNumberTextView.text = firstUser.phoneNumber
                    Gender.text = firstUser.gender
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileFragment", "Failed to read users from Firebase", error.toException())
                Toast.makeText(requireContext(), "Error loading user data", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
