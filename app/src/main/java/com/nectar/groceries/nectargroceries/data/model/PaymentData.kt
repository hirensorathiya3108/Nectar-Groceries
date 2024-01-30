package com.nectar.groceries.nectargroceries.data.model

data class PaymentData(
    val card_name:String = "",
    val card_number:Int = 0,
    val card_expiry_date: String = "",
    val card_security_code:Int = 0
)
