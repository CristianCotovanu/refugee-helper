package com.example.refugeehelper.foundations.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Foundation(
    var name: String,
    var description: String,
    var phoneNumber: String, // Add validation
    var websiteUrl: String, // display clickable href
    var creationDate: String
) {
}