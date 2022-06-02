package com.example.refugeehelper

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.refugeehelper.databinding.ActivityMainBinding
import com.example.refugeehelper.foundations.FoundationActivity
import com.example.refugeehelper.requests.RequestActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private lateinit var emergencyCallButton: FloatingActionButton

    private val requestCall = 1

    private lateinit var receiver: AirplaneModeChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityContainer


        receiver = AirplaneModeChangeReceiver()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            // registering the receiver
            // it parameter which is passed in  registerReceiver() function
            // is the intent filter that we have just created
            registerReceiver(receiver, it)
        }

        val emergencyCallButton: View = findViewById<FloatingActionButton>(R.id.emergency_call_button)
        emergencyCallButton.setOnClickListener {
            callEmergencyNumber()
        }

        val button = findViewById<Button>(R.id.button);
        button.setOnClickListener {
            callEmergencyNumber()
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

//    override fun onStop() {
//        super.onStop()
////        unregisterReceiver(receiver)
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callEmergencyNumber()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callEmergencyNumber() {
        val emergencyPhoneNumber = "0761954747";
        val dialPhoneNumber = "tel:$emergencyPhoneNumber"


        val isAllowed = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_GRANTED

        if (isAllowed) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.CALL_PHONE),
                requestCall
            )
        } else {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dialPhoneNumber)))
        }
    }
}


