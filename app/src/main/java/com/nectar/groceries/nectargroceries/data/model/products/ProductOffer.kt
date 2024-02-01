package com.nectar.groceries.nectargroceries.data.model.products

data class ProductOffer(
    val id : String,
    val url : String
){
    constructor() : this("", "")
}