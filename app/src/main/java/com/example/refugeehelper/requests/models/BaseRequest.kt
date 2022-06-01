package com.example.refugeehelper.requests.models

open class BaseRequest {
    var description: String = ""
    var requestPeriod: String = ""
    var contactPhoneNumber: Int = 0
    var creationDate: String = ""

    constructor()

    constructor(
        contactPhoneNumber: Int,
        requestPeriod: String,
        description: String,
        creationDate: String
    ) {
        this.contactPhoneNumber = contactPhoneNumber
        this.requestPeriod = requestPeriod
        this.description = description
        this.creationDate = creationDate
    }
}