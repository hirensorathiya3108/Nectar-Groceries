package com.nectar.groceries.nectargroceries.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.products.CartProductsData
import com.nectar.groceries.nectargroceries.data.model.products.Products
import com.nectar.groceries.nectargroceries.data.model.user.ProfileData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.database.FirebaseDB
import com.nectar.groceries.nectargroceries.databinding.ActivityProductDetailsBinding
import com.nectar.groceries.nectargroceries.ui.fragment.ParentFragment
import com.nectar.groceries.nectargroceries.utils.Utils

class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference
    private lateinit var productImage: String
    private lateinit var productTitle: String
    private lateinit var productWeight: String
    private lateinit var productQuantity: String
    private lateinit var productPrice: String
    private lateinit var productDetails: String
    private lateinit var productBenefits: String
    private var productReview: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        activity = this@ProductDetailsActivity
        initViews()
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        productImage = intent.getStringExtra("productImage")!!
        productTitle = intent.getStringExtra("productTitle")!!
        productQuantity = intent.getStringExtra("productQuantity")!!
        productWeight = intent.getStringExtra("productWeight")!!
        productPrice = intent.getStringExtra("productPrice")!!
        productDetails = intent.getStringExtra("productDetails")!!
        productBenefits = intent.getStringExtra("productBenefits")!!
        productReview = intent.getFloatExtra("productReview", 0F)
        setDataInField()
        binding.btnAddCart.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    // TODO: set profile data
    @SuppressLint("SetTextI18n")
    private fun setDataInField() {
        Glide.with(activity).load(productImage).into(binding.ivProductImage)
        binding.tvProductTitle.text = productTitle
        binding.tvProductSubDetails.text = productQuantity + productWeight
        binding.tvProductPrice.text = productPrice
        binding.tvProductDetails.text = productDetails
        binding.tvProductBenefits.text = productBenefits
        binding.productReview.rating = productReview
    }

    @SuppressLint("HardwareIds")
    private fun addProductInBasket() {
        val deviceId: String =
            Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
        val documentReference =
            FirebaseDB().getCollectionReferenceForBasket(deviceId)
        val lastOrderId =
            appPreference.getPreference(AppPersistence.keys.LAST_ORDER_ID_NUMBER) as Int
        val orderIdNumber = lastOrderId + 1
        val customId = "order_$orderIdNumber"
        val product_order_quantity = "1"
        val basketProduct = CartProductsData(
            customId,
            productImage,
            productTitle,
            productDetails,
            productBenefits,
            productReview.toDouble(),
            productQuantity,
            product_order_quantity,
            productWeight,
            productPrice,
            productPrice
        )
        documentReference.document(customId).set(basketProduct)
            .addOnSuccessListener {
                // Data uploaded successfully
                // You can add any success handling logic here
                appPreference.setPreference(AppPersistence.keys.LAST_ORDER_ID_NUMBER, orderIdNumber)
                Utils().showToast(activity, getString(R.string.product_add_in_basket))
                ParentFragment.listRefresh = true

            }
            .addOnFailureListener { e ->
                // Handle the failure
                Utils().showToast(activity, getString(R.string.product_add_failed_in_basket))
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.btnAddCart -> {
                addProductInBasket()
            }
        }
    }
}