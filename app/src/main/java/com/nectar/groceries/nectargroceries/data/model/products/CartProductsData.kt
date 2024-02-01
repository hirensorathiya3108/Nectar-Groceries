package com.nectar.groceries.nectargroceries.data.model.products

data class CartProductsData(
    val order_id:String= "",
    val product_image:String = "",
    val product_name:String = "",
    val product_details:String = "",
    val product_benefits:String = "",
    val product_review:Double = 0.0,
    val product_quantity:String = "",
    var product_order_quantity:String = "",
    val product_weight:String ="",
    val product_orignal_price:String="",
    var product_price:String=""
)
