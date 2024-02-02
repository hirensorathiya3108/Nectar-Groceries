package com.nectar.groceries.nectargroceries.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.products.CartProductsData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.database.FirebaseDB
import com.nectar.groceries.nectargroceries.databinding.FragmentCartBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.ui.activity.LoginActivity
import com.nectar.groceries.nectargroceries.ui.adapter.BasketOrderProductAdapter
import com.nectar.groceries.nectargroceries.utils.Utils
import com.nectar.groceries.nectargroceries.view.dialog.AddressInformationDialog
import com.nectar.groceries.nectargroceries.view.dialog.OrderConfirmDialog
import needle.Needle
import needle.UiRelatedTask
import java.math.BigDecimal


class CartFragment : ParentFragment(), View.OnClickListener {
    private lateinit var binding: FragmentCartBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference
    private lateinit var basketOrderProductAdapter: BasketOrderProductAdapter
    private lateinit var orderProductList: ArrayList<CartProductsData>
    private lateinit var deviceId: String

    companion object {
        fun newInstance(title: String): CartFragment {
            val fragment = CartFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity = requireActivity()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        initViews()
        return binding.root
    }

    @SuppressLint("HardwareIds")
    private fun initViews() {
        appPreference = AppPreference(activity)
        orderProductList = ArrayList()
        binding.orderBtn.setOnClickListener(this)
    }

    private fun getOrderData() {
        orderProductList.clear()
        val documentReference = FirebaseDB().getCollectionReferenceForBasket(deviceId)
        documentReference.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    val data = document?.toObjects(CartProductsData::class.java)
                    Needle.onBackgroundThread().execute(object : UiRelatedTask<Void?>() {
                        override fun doWork(): Void? {
                            if (data != null) {
                                orderProductList.addAll(data)
                            }
                            return null
                        }

                        override fun thenDoUiRelatedWork(result: Void?) {
                            if (orderProductList.size == 0) {
                                binding.isEmptyList.beVisible()
                                binding.isFilesLoading.beGone()
                                binding.rcvOreder.beGone()
                                binding.llOrderDetails.beGone()
                            } else {
                                setBasketOrderAdapter()
                                setTotalOrderData()
                            }
                        }
                    })
                }
            }
    }

    private fun setBasketOrderAdapter() {
        basketOrderProductAdapter =
            BasketOrderProductAdapter(activity, orderProductList,
                object : BasketOrderProductAdapter.OnItemClickListener {
                    override fun onClicked(position: Int) {
                    }

                    override fun plusOrderQuantity(position: Int) {
                        val item = orderProductList[position]
                        plusOrderQuantityData(item)
                    }

                    override fun minusOrderQuantity(position: Int) {
                        val item = orderProductList[position]
                        minusOrderQuantityData(item)
                    }

                    override fun removeOrder(position: Int) {
                        val item = orderProductList[position]
                        removeOrderData(item, position)
                    }
                })
        binding.rcvOreder.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = basketOrderProductAdapter
        }
        binding.isEmptyList.beGone()
        binding.isFilesLoading.beGone()
        binding.rcvOreder.beVisible()
        if (listRefresh) listRefresh = false
    }

    private fun minusOrderQuantityData(item: CartProductsData) {
        updateProductDataInFirebase(item)
    }

    private fun plusOrderQuantityData(item: CartProductsData) {
        updateProductDataInFirebase(item)
    }

    private fun updateProductDataInFirebase(cartProduct: CartProductsData) {
        val databaseReference = FirebaseDB().getCollectionReferenceForBasket(deviceId)
            .document(cartProduct.order_id) // Assuming productId is the unique identifier for each product

        // Create a map with the updated data
        val updatedData = mapOf(
            "product_order_quantity" to cartProduct.product_order_quantity,
            "product_price" to cartProduct.product_price
            // Add other fields as needed
        )

        // Update the data in Firebase Realtime Database
        databaseReference.update(updatedData)
            .addOnSuccessListener {
                Log.d("Firebase Update", "Data updated successfully")
                activity.runOnUiThread { setTotalOrderData() }
//                getOrderData()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase Update", "Error updating data: $e")
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeOrderData(item: CartProductsData, position: Int) {
        val databaseReference = FirebaseDB().getCollectionReferenceForBasket(deviceId)
            .document(item.order_id) // Assuming productId is the unique identifier for each product
        // Update the data in Firebase Realtime Database
        databaseReference.delete()
            .addOnSuccessListener {
                Log.d("Firebase Update", "Data updated successfully")
                activity.runOnUiThread {
                    orderProductList.removeAt(position)
                    basketOrderProductAdapter.notifyDataSetChanged()
                    setTotalOrderData()
                }
//                getOrderData()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase Update", "Error updating data: $e")
            }
    }

    private fun setTotalOrderData() {
        binding.llOrderDetails.beVisible()
        val totalPrice = String.format("%.2f", calculateTotalPrice(orderProductList))
        binding.tvTotalPrice.text = activity.getString(
            R.string.total_price, totalPrice
        )
        binding.tvTotalOrder.text =
            activity.getString(R.string.total_order, orderProductList.size.toString())
    }

    private fun calculateTotalPrice(orderProductList: List<CartProductsData>): Double {
        var totalPrice = BigDecimal.ZERO

        for (product in orderProductList) {
            Log.e(
                "calculateTotalPrice: ",
                "product_price => ${extractNumericPrice(product.product_price)!!}"
            )
            val productPrice =
                extractNumericPrice2(product.product_price.replace("$", "")) ?: BigDecimal.ZERO
            totalPrice = totalPrice.add(productPrice)
            Log.e("calculateTotalPrice: ", "totalPrice => $totalPrice")
        }

        return totalPrice.toDouble()
    }


    private fun extractNumericPrice2(price: String): BigDecimal? {
        return try {
            BigDecimal(price)
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun extractNumericPrice(productPrice: String): Double? {
        // Define a regex pattern to find the numeric part after the "$" symbol
        val replacePrice = productPrice.replace("$", "")

        return replacePrice.toDouble()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.orderBtn -> {
                val isLogin = appPreference.getPreference(AppPersistence.keys.IS_LOGIN) as Boolean
                val isFileAddress = appPreference.getPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO) as Boolean
                if (!isLogin) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    if(!isFileAddress){
                        Log.e( "onClick: ","add => address" )
                        AddressInformationDialog().showDialog(activity)
                    } else {
                        if (orderProductList.size != 0){
                            OrderConfirmDialog().showDialog(activity)
                        } else Utils().showToast(activity,
                            getString(R.string.please_add_order_in_basket))
                    }
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    override fun onResume() {
        super.onResume()
        Log.e("onResume: ", "onResume => ")
        deviceId = Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
        val isLogin = appPreference.getPreference(AppPersistence.keys.IS_LOGIN) as Boolean
        val isFileAddress = appPreference.getPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO) as Boolean
        binding.orderBtn.text =
            if (isLogin){
                if (isFileAddress) activity.getString(R.string.confirm_order) else activity.getString(R.string.add_address)
            } else activity.getString(R.string.login_in)
        if (isFileAddress)binding.orderBtn.textSize = activity.resources.getDimension(R.dimen.order_btn_text_size)
        if (listRefresh) getOrderData()
    }

}