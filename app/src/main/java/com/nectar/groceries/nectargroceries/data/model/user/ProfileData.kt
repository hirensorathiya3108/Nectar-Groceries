package com.nectar.groceries.nectargroceries.data.model.user

data class ProfileData(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val payment_info: PaymentData,
    val address_info: AddressData
){
    // Add a no-argument constructor
    constructor() : this("", "", "", PaymentData(),AddressData())
}