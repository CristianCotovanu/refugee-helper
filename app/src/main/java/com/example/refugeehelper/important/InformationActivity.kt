package com.example.refugeehelper.important

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.refugeehelper.databinding.ActivityInformationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InformationActivity : AppCompatActivity() {
    private val NATIONAL_REFUGEE_HELP_PHONE_NUMBER = "0214144494"

    private lateinit var binding: ActivityInformationBinding
    private lateinit var database: DatabaseReference

//    private lateinit var text: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference


    }
}