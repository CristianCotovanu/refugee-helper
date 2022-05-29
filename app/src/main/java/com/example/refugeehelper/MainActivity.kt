package com.example.refugeehelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_foundation -> {
                    val intent = Intent(this@MainActivity, FoundationActivity::class.java)
                    startActivity(intent)
                }
                //TODO: add all other activity redirects here
            }
            true
        }




    }

//    @GET("Medications.json")
//    fun getMedications(): Call<List<Medication>>
}
