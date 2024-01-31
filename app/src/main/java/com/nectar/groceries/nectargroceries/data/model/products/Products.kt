package com.nectar.groceries.nectargroceries.data.model.products

data class Products(
    val product_name:String = "",
    val product_sub_desc:String = "",
    val product_details:String = "",
    val product_benefits:String = "",
    val product_review:Int = 0,
    val product_quantity:Int = 0,
    val product_price:Int=0
)
