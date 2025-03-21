//package com.example.techtonic.Fragments
//
//
//import NotificationAdapter
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.Button
//import android.widget.ListView
//import androidx.activity.OnBackPressedCallback
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.techtonic.Activity.MainActivity
//import com.example.techtonic.Class.NotificationClass
//import com.example.techtonic.R
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class Notification : Fragment() {
//
//    private lateinit var listView: ListView
//    private lateinit var notificationAdapter: NotificationAdapter
//    private lateinit var database: DatabaseReference
//    private val notificationsList = mutableListOf<NotificationClass>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_notification, container, false)
//
//        listView = view.findViewById(R.id.recyclerViewNotifications)
//
//
//        notificationAdapter = NotificationAdapter(requireContext(), notificationsList)
//        listView.adapter = notificationAdapter
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    showExitConfirmationDialog()
//                }
//            }
//        )
//
//        database = FirebaseDatabase.getInstance().getReference("hazardReports")
//
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                notificationsList.clear()
//                for (reportSnapshot in snapshot.children) {
//                    val hazardType = reportSnapshot.child("hazardType").value.toString()
//                    val status = reportSnapshot.child("status").value.toString()
//                    val isRead = reportSnapshot.child("isRead").value as? Boolean ?: false
//                    val imageUrl = reportSnapshot.child("imageUrl").value.toString()
//                    val id = reportSnapshot.key ?: ""
//
//                    val notification = NotificationClass(
//                        id,
//                        hazardType,
//                        status,
//                        isRead,
//                        imageUrl
//                    )
//                    notificationsList.add(notification)
//                }
//                notificationAdapter.notifyDataSetChanged()
//                updateNotificationBadge()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//
//        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            val selectedNotification = notificationsList[position]
//        }
//
//        return view
//    }
//    private fun showExitConfirmationDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Exit Application")
//        builder.setMessage("Are you sure you want to exit this application?")
//
//        builder.setPositiveButton("Yes") { _, _ ->
//            requireActivity().finishAffinity()
//        }
//
//        builder.setNegativeButton("No") { dialog, _ ->
//            dialog.dismiss()
//        }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
//
//    private fun updateNotificationBadge() {
//        val unreadCount = notificationsList.count { !it.isRead }
//        val activity = activity as? MainActivity
//        activity?.updateNotificationBadge(unreadCount)
//    }
//    private fun selectTab(selectedTab: Button, unselectedTab: Button) {
//        val selectedBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.tab_selected)
//        val unselectedBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.tab_unselected)
//
//        selectedTab.background = selectedBackground
//        selectedTab.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
//
//        unselectedTab.background = unselectedBackground
//        unselectedTab.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
//    }
////}
//package com.example.techtonic.Fragments
//
//import NotificationAdapter
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.Button
//import android.widget.ImageButton
//import android.widget.ListView
//import androidx.activity.OnBackPressedCallback
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.techtonic.Activity.MainActivity
//import com.example.techtonic.Class.NotificationClass
//import com.example.techtonic.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//class Notification : Fragment() {
//
//    private lateinit var listView: ListView
//    private lateinit var notificationAdapter: NotificationAdapter
//    private lateinit var database: DatabaseReference
//    private val notificationsList = mutableListOf<NotificationClass>()
//    private lateinit var userId: String
//    private lateinit var backButton: ImageButton
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_notification, container, false)
//
//        listView = view.findViewById(R.id.recyclerViewNotifications)
//
//        // Get current user's UID
//        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//
//        notificationAdapter = NotificationAdapter(requireContext(), notificationsList)
//        listView.adapter = notificationAdapter
//
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    showExitConfirmationDialog()
//                }
//            }
//        )
//        backButton = view.findViewById(R.id.back_button)
//        backButton.setOnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_cont, Home()) // Ensure this is your correct container ID
//                .addToBackStack(null)
//                .commit()
//        }
//
//        // Query only the user's reports
//        database = FirebaseDatabase.getInstance().getReference("hazardReports")
//        fetchUserNotifications()
//
//        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            val selectedNotification = notificationsList[position]
//            markNotificationAsRead(selectedNotification.id)
//        }
//
//        return view
//    }
//
//    private fun fetchUserNotifications() {
//        database.orderByChild("userId").equalTo(userId)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    notificationsList.clear()
//                    for (reportSnapshot in snapshot.children) {
//                        val hazardType = reportSnapshot.child("hazardType").value.toString()
//                        val status = reportSnapshot.child("status").value.toString()
//                        val isRead = reportSnapshot.child("isRead").getValue(Boolean::class.java) ?: false
//                        val imageUrl = reportSnapshot.child("imageUrl").value.toString()
//                        val id = reportSnapshot.key ?: ""
//
//                        val notification = NotificationClass(
//                            id,
//                            hazardType,
//                            status,
//                            isRead,
//                            imageUrl
//                        )
//                        notificationsList.add(notification)
//                    }
//                    notificationAdapter.notifyDataSetChanged()
//                    updateNotificationBadge()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })
//    }
//
//    private fun markNotificationAsRead(notificationId: String) {
//        database.child(notificationId).child("isRead").setValue(true)
//            .addOnCompleteListener {
//                fetchUserNotifications() // Refresh list after marking as read
//            }
//    }
//
//    private fun showExitConfirmationDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Exit Application")
//        builder.setMessage("Are you sure you want to exit this application?")
//
//        builder.setPositiveButton("Yes") { _, _ ->
//            requireActivity().finishAffinity()
//        }
//
//        builder.setNegativeButton("No") { dialog, _ ->
//            dialog.dismiss()
//        }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
//
//    private fun updateNotificationBadge() {
//        val unreadCount = notificationsList.count { !it.isRead }
//        val activity = activity as? MainActivity
//        activity?.updateNotificationBadge(unreadCount)
//    }
//
//    private fun selectTab(selectedTab: Button, unselectedTab: Button) {
//        val selectedBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.tab_selected)
//        val unselectedBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.tab_unselected)
//
//        selectedTab.background = selectedBackground
//        selectedTab.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
//
//        unselectedTab.background = unselectedBackground
//        unselectedTab.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
//    }
//}
package com.example.techtonic.Fragments

import NotificationAdapter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.techtonic.Activity.MainActivity
import com.example.techtonic.Class.NotificationClass
import com.example.techtonic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Notification : Fragment() {

    private lateinit var listView: ListView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var database: DatabaseReference
    private val notificationsList = mutableListOf<NotificationClass>()
    private lateinit var userId: String
    private lateinit var backButton: ImageButton
    private lateinit var btnNotice: Button
    private lateinit var btnCompleted: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        listView = view.findViewById(R.id.recyclerViewNotifications)
        backButton = view.findViewById(R.id.back_button)
        val btnNotice = view.findViewById<Button>(R.id.btn_notice)
        val btnCompleted = view.findViewById<Button>(R.id.btn_completed)

        var isNoticeInFront = true

        btnNotice.setOnClickListener {
            if (!isNoticeInFront) {
                swapTabs(btnNotice, btnCompleted)
                isNoticeInFront = true
            }
        }

        btnCompleted.setOnClickListener {
            if (isNoticeInFront) {
                swapTabs(btnCompleted, btnNotice)
                isNoticeInFront = false
            }
        }



        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
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

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_cont, Home())
                .addToBackStack(null)
                .commit()
        }

        btnNotice.setOnClickListener { selectTab(btnNotice, btnCompleted); fetchUserNotifications("Pending") }
        btnCompleted.setOnClickListener { selectTab(btnCompleted, btnNotice); fetchUserNotifications("Completed") }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedNotification = notificationsList[position]
            markNotificationAsRead(selectedNotification.id)
        }

        fetchUserNotifications("Pending")
        return view
    }
    private fun swapTabs(front: Button, back: Button) {
        front.bringToFront()
        front.translationZ = 3f
        back.translationZ = 1f

        front.animate()
            .scaleX(1.1f).scaleY(1.1f)
            .setDuration(200)
            .start()

        back.animate()
            .scaleX(1f).scaleY(1f)
            .setDuration(200)
            .start()
    }

    private fun fetchUserNotifications(status: String) {
        database = FirebaseDatabase.getInstance().getReference("hazardReports")
        database.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notificationsList.clear()
                    for (reportSnapshot in snapshot.children) {
                        val hazardType = reportSnapshot.child("hazardType").value.toString()
                        val reportStatus = reportSnapshot.child("status").value.toString()
                        val isRead = reportSnapshot.child("isRead").getValue(Boolean::class.java) ?: false
                        val imageUrl = reportSnapshot.child("imageUrl").value.toString()
                        val id = reportSnapshot.key ?: ""

                        if (reportStatus == status) {
                            val notification = NotificationClass(
                                id, hazardType, reportStatus, isRead, imageUrl
                            )
                            notificationsList.add(notification)
                        }
                    }
                    notificationAdapter.notifyDataSetChanged()
                    updateNotificationBadge()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun markNotificationAsRead(notificationId: String) {
        database.child(notificationId).child("isRead").setValue(true)
            .addOnCompleteListener { fetchUserNotifications("Pending") }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit Application")
            .setMessage("Are you sure you want to exit this application?")
            .setPositiveButton("Yes") { _, _ -> requireActivity().finishAffinity() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .create().show()
    }

    private fun updateNotificationBadge() {
        val unreadCount = notificationsList.count { !it.isRead }
        (activity as? MainActivity)?.updateNotificationBadge(unreadCount)
    }

    private fun selectTab(selectedTab: Button, unselectedTab: Button) {
        selectedTab.background = ContextCompat.getDrawable(requireContext(), R.drawable.tab_selected)
        selectedTab.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

        unselectedTab.background = ContextCompat.getDrawable(requireContext(), R.drawable.tab_unselected)
        unselectedTab.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
    }
}
