package com.amirx.matule_app_sessions.ui.main.cart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.databinding.ItemCartBinding
import com.bumptech.glide.Glide

class NewAdapter(
    private val context: Context,
    private val onDeleteClick: (Cart) -> Unit,
    private val onQuantityChange: (Cart, Int) -> Unit
) : ListAdapter<Cart, NewAdapter.NewViewHolder>(Comparator()) {
    inner class NewViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Cart) {
            val item = getItem(position)
            binding.apply {
                productName.text = product.product.name
                productPrice.text = "${product.product.price} rub"
                Glide.with(context)
                    .load(product.product.image)
                    .into(productImage)

                btnPlus.setOnClickListener {
                    val newQuantity = product.quantity!! + 1
                    val currentList = currentList.toMutableList()
                    val index = currentList.indexOf(item)
                    if (index != -1) {
                        currentList[index] = item.copy(quantity = newQuantity)
                        submitList(currentList)
                    }

                }
                btnMinus.setOnClickListener {
                    val newQuantity = product.quantity!! - 1
                    val currentList = currentList.toMutableList()
                    val index = currentList.indexOf(item)
                    if (index != -1) {
                        currentList[index] = item.copy(quantity = newQuantity)
                        submitList(currentList)
                    }
                }
                btnDelete.setOnClickListener {
                    val currentList = currentList.toMutableList()
                    currentList.remove(item)
                    submitList(currentList)
                    onDeleteClick(item)
                }
            }
        }
    }

    class Comparator() : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}