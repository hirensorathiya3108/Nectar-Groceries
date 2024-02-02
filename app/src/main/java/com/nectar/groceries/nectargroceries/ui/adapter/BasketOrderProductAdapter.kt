package com.nectar.groceries.nectargroceries.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.products.CartProductsData
import com.nectar.groceries.nectargroceries.databinding.ItemCartProductBinding

class BasketOrderProductAdapter(
    private val activity: Activity,
    private val orderList: ArrayList<CartProductsData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BasketOrderProductAdapter.ItemViewHolder>() {

    interface OnItemClickListener {
        fun onClicked(position: Int)
        fun plusOrderQuantity(position: Int)
        fun minusOrderQuantity(position: Int)
        fun removeOrder(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemCartProductBinding.inflate(
                LayoutInflater.from(activity),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = orderList[position]
        holder.bind(item, position)
    }

    inner class ItemViewHolder(val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartProductsData, position: Int) {
            if (item.product_image != "") Glide.with(activity).load(item.product_image)
                .into(binding.ivProductImage)
            if (item.product_name != "") binding.tvProductTitle.text = item.product_name
            if (item.product_name != "") binding.tvProductQuantity.text = item.product_quantity
            if (item.product_name != "") binding.tvProductWeight.text = item.product_weight
            if (item.product_order_quantity != "") binding.tvOrderQuantity.text = item.product_order_quantity
            if (item.product_price != "") binding.tvProductPrice.text = item.product_price

            binding.btnMinus.setOnClickListener {
                if (item.product_order_quantity > "1") minusOrderQuantityData(item,position)
            }
            binding.btnPlus.setOnClickListener {
                if (item.product_order_quantity >= "1") plusOrderQuantityData(item,position)
            }

            binding.ivRemove.setOnClickListener {
                listener.removeOrder(position)
            }
        }

        private fun minusOrderQuantityData(item: CartProductsData, position: Int) {
            val minusOrderQuantity = item.product_order_quantity.toInt() - 1
            binding.tvOrderQuantity.text = minusOrderQuantity.toString()
            val totalPrice = extractNumericPrice(item.product_orignal_price)!! * minusOrderQuantity.toDouble()
            binding.tvProductPrice.text = activity.getString(R.string.set_price,totalPrice.toString())
            item.product_order_quantity = minusOrderQuantity.toString()
            item.product_price = activity.getString(R.string.set_price,totalPrice.toString())
            notifyDataSetChanged()
            listener.minusOrderQuantity(position)
        }

        private fun plusOrderQuantityData(item: CartProductsData, position: Int) {
            val plusOrderQuantity = item.product_order_quantity.toInt() + 1
            binding.tvOrderQuantity.text = plusOrderQuantity.toString()
            val totalPrice = extractNumericPrice(item.product_orignal_price)!! * plusOrderQuantity.toDouble()
            binding.tvProductPrice.text = activity.getString(R.string.set_price,totalPrice.toString())
            item.product_order_quantity = plusOrderQuantity.toString()
            item.product_price = activity.getString(R.string.set_price,totalPrice.toString())
            notifyDataSetChanged()
            listener.plusOrderQuantity(position)
        }

        private fun extractNumericPrice(productPrice: String): Double? {
            // Define a regex pattern to find the numeric part after the "$" symbol
            val replacePrice = productPrice.replace("£","")

            return replacePrice.toDouble()
        }

    }
}