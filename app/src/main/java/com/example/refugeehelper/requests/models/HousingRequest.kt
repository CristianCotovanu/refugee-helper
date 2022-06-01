package com.example.refugeehelper.requests.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class HousingRequest : BaseRequest {
    var adultsNumber: Int = 0
    var childrenNumber: Int = 0

    constructor () : super()

    constructor (
        description: String,
        requestPeriod: String,
        contactPhoneNumber: Int,
        creationDate: String,
        adultsNumber: Int,
        childrenNumber: Int
    ) : super(
        contactPhoneNumber,
        requestPeriod,
        description,
        creationDate
    )
}