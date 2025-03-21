package com.example.techtonic.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.techtonic.Activity.History
import com.example.techtonic.Authentication.SignIn
import com.example.techtonic.R


class Settings : Fragment() {
    private lateinit var history: ImageView
    private lateinit var Logout: Button
    private lateinit var backButton: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        Logout = view.findViewById(R.id.logout)
        history = view.findViewById(R.id.historyreports)
        Logout.setOnClickListener {
            showLogoutConfirmation()
        }
        history.setOnClickListener {
            val intent = Intent(requireContext(), History::class.java)
            startActivity(intent)
        }
        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_cont, Home()) // Ensure this is your correct container ID
                .addToBackStack(null)
                .commit()
        }


        return view
    }
    private fun showLogoutConfirmation() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")

        builder.setPositiveButton("Yes") { _, _ ->
            logoutUser()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun logoutUser() {
        val intent = Intent(requireContext(), SignIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

}