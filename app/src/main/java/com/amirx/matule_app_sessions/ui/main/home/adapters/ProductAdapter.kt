package com.amirx.matule_app_sessions.ui.main.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.databinding.ItemProductBinding
import com.bumptech.glide.Glide

class ProductAdapter(
    private val context: Context,
    private val onItemClick: (Product) -> Unit
) :
    ListAdapter<Product, ProductAdapter.ViewHolder>(
        Comparator()
    ) {
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.price.text = "${product.price} rub"
            binding.titleTxt.text = product.name

            Glide.with(context)
                .load(product.image)
                .into(binding.crossIMG)

            itemView.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}