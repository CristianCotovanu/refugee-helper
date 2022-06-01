package com.example.refugeehelper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.refugeehelper.foundations.FoundationActivity
import com.example.refugeehelper.requests.RequestActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { navItem ->
            when (navItem.itemId) {
                R.id.ic_foundation -> {
                    val intent = Intent(this@MainActivity, FoundationActivity::class.java)
                    startActivity(intent)
                }
                R.id.ic_requests -> {
                    val intent = Intent(this@MainActivity, RequestActivity::class.java)
                    startActivity(intent)
                }
                //TODO: add all other activity redirects here
            }
            true
        }
    }
}
