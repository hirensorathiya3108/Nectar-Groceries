package com.nectar.groceries.nectargroceries.data.model.products

import com.nectar.groceries.nectargroceries.data.model.user.PaymentData

data class ProductOffer(
    val id : String,
    val url : String
){
    constructor() : this("", "")
}