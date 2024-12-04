package com.example.techtonic.Fragments


import NotificationAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.techtonic.Activity.MainActivity
import com.example.techtonic.Class.NotificationClass
import com.example.techtonic.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Notification : Fragment() {

    private lateinit var listView: ListView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var database: DatabaseReference
    private val notificationsList = mutableListOf<NotificationClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        listView = view.findViewById(R.id.recyclerViewNotifications)

        notificationAdapter = NotificationAdapter(requireContext(), notificationsList)
        listView.adapter = notificationAdapter
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            }
        )


        database = FirebaseDatabase.getInstance().getReference("hazardReports")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationsList.clear()
                for (reportSnapshot in snapshot.children) {
                    val hazardType = reportSnapshot.child("hazardType").value.toString()
                    val status = reportSnapshot.child("status").value.toString()
                    val isRead = reportSnapshot.child("isRead").value as? Boolean ?: false
                    val imageUrl = reportSnapshot.child("imageUrl").value.toString()
                    val id = reportSnapshot.key ?: ""

                    val notification = NotificationClass(
                        id,
                        hazardType,
                        status,
                        isRead,
                        imageUrl
                    )
                    notificationsList.add(notification)
                }
                notificationAdapter.notifyDataSetChanged()
                updateNotificationBadge()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedNotification = notificationsList[position]
        }

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

    private fun updateNotificationBadge() {
        val unreadCount = notificationsList.count { !it.isRead }
        val activity = activity as? MainActivity
        activity?.updateNotificationBadge(unreadCount)
    }
}
