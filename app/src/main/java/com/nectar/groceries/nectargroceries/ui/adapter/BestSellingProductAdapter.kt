package com.nectar.groceries.nectargroceries.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nectar.groceries.nectargroceries.data.model.products.Products
import com.nectar.groceries.nectargroceries.databinding.ItemProductBinding

class BestSellingProductAdapter(
    private val activity: Activity,
    private val bestSellingProductList: ArrayList<Products>,
    private val listener:OnItemClickListener
) :RecyclerView.Adapter<BestSellingProductAdapter.ItemViewHolder>(){

    interface OnItemClickListener{
        fun onClicked(position: Int)
        fun addCart(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemProductBinding.inflate(LayoutInflater.from(activity),parent,false))
    }

    override fun getItemCount(): Int {
        return bestSellingProductList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = bestSellingProductList[position]
        holder.bind(item,position)
    }

    inner class ItemViewHolder(val binding:ItemProductBinding ):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Products, position: Int) {
            if (item.product_image != "")Glide.with(activity).load(item.product_image).into(binding.ivProductImage)
            if (item.product_name != "")binding.tvProductTitle.text = item.product_name
            if (item.product_name != "")binding.tvProductQuantity.text = item.product_quantity
            if (item.product_name != "")binding.tvProductWeight.text = item.product_weight
            if (item.product_price != "")binding.tvProductPrice.text = item.product_price

            binding.clProductRoot.setOnClickListener { listener.onClicked(position) }
            binding.btnAddCart.setOnClickListener { listener.addCart(position) }
        }

    }
}