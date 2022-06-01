package com.example.refugeehelper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.refugeehelper.databinding.ActivityMainBinding
import com.example.refugeehelper.foundations.FoundationActivity
import com.example.refugeehelper.requests.RequestActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var emergencyCallButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityContainer


        emergencyCallButton = findViewById(R.id.emergency_call_button)
        emergencyCallButton.setOnClickListener { view ->
            callEmergencyNumber(view)
        }

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

    private fun callEmergencyNumber(view: View) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0761954747"))
        startActivity(intent)
    }
}
