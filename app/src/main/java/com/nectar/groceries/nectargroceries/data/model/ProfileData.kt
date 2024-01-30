package com.nectar.groceries.nectargroceries.data.model

data class ProfileData(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val payment_info:PaymentData
){
    // Add a no-argument constructor
    constructor() : this("", "", "", PaymentData())
}