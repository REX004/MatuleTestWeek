package com.amirx.matule_app_sessions.ui.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<Product, CategoryAdapter.ViewHolder>(Comporator()) {
    inner class ViewHolder(
        private val binding: ItemCategoryBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.categoryTxt.text = product.category

            itemView.setOnClickListener {
                onItemClick(product.category.toString())
            }
        }

    }

    class Comporator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}