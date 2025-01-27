package com.amirx.matule_app_sessions.ui.main.cart.adapters
import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.databinding.ItemCartBinding
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context,
    private val onDeleteClick: (Cart) -> Unit,
    private val onQuantityChange: (Cart, Int) -> Unit
) : ListAdapter<Cart, SwipebaleListItem>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipebaleListItem {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SwipebaleListItem(binding.root)
    }

    override fun onBindViewHolder(holder: SwipebaleListItem, position: Int) {
        val item = getItem(position)
        val binding = ItemCartBinding.bind(holder.itemView)

        Glide.with(context)
            .load(item.product.image)
            .into(binding.productImage)
        binding.apply {
            productName.text = item.product.name
            productPrice.text = "${item.product.price}"
            quantity.text = "${item.quantity}"

            btnPlus.setOnClickListener {
                val newQuantity = item.quantity!! + 1
                // Получаем текущий список
                val currentList = currentList.toMutableList()
                // Находим индекс текущего элемента
                val index = currentList.indexOf(item)
                if (index != -1) {
                    // Обновляем количество в копии списка
                    currentList[index] = item.copy(quantity = newQuantity)
                    // Отправляем обновленный список в адаптер
                    submitList(currentList)
                }
                onQuantityChange(item, newQuantity)
            }
            btnMinus.setOnClickListener {
                if (item.quantity!! > 1) {
                    val newQuantity = item.quantity!! - 1
                    // Получаем текущий список
                    val currentList = currentList.toMutableList()
                    // Находим индекс текущего элемента
                    val index = currentList.indexOf(item)
                    if (index != -1) {
                        // Обновляем количество в копии списка
                        currentList[index] = item.copy(quantity = newQuantity)
                        // Отправляем обновленный список в адаптер
                        submitList(currentList)
                    }
                    onQuantityChange(item, newQuantity)
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

class CartDiffCallback : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }
}