package com.nectar.groceries.nectargroceries.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.products.ProductOffer

class ImageAdapter : ListAdapter<ProductOffer, ImageAdapter.ViewHolder>(DiffCallback()){

    class DiffCallback : DiffUtil.ItemCallback<ProductOffer>(){
        override fun areItemsTheSame(oldItem: ProductOffer, newItem: ProductOffer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductOffer, newItem: ProductOffer): Boolean {
            return oldItem == newItem
        }

    }
    class ViewHolder(iteView: View): RecyclerView.ViewHolder(iteView){
        private val imageView = iteView.findViewById<ImageView>(R.id.imageView)

        fun bindData(item: ProductOffer){
            Glide.with(itemView)
                .load(item.url)
                .into(imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image_slider,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = getItem(position)
        holder.bindData(imageItem)
    }
}