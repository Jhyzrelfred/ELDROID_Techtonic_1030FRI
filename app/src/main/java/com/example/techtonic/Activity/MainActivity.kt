package com.example.techtonic.Activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.techtonic.Fragments.Home
import com.example.techtonic.Fragments.Notification
import com.example.techtonic.Fragments.Profile
import com.example.techtonic.Fragments.Report
import com.example.techtonic.Fragments.Settings
import com.example.techtonic.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, Home())
                .commit()
        }



        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_reports -> {
                    loadFragment(Report())
                    true
                }
                R.id.nav_notifications -> {
                    loadFragment(Notification())
                    true
                }
                R.id.nav_home -> {
                    loadFragment(Home())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(Profile())
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(Settings())
                    true
                }
                else -> false
            }
        }


        bottomNav.selectedItemId = R.id.nav_home
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
    fun updateNotificationBadge(count: Int) {
        val badge = bottomNav.getOrCreateBadge(R.id.nav_notifications)
        badge.isVisible = count > 0
        badge.number = count
    }


}

