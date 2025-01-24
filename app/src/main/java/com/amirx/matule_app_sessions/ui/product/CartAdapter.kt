import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.databinding.ItemCartBinding
import com.amirx.matule_app_sessions.databinding.ItemCartHeaderBinding
import com.amirx.matule_app_sessions.ui.main.cart.adapters.SwipebaleListItem
import com.amirx.matule_app_sessions.ui.product.CartGroupItem
import com.amirx.matule_app_sessions.ui.product.isToday
import com.amirx.matule_app_sessions.ui.product.isYesterday
import com.bumptech.glide.Glide

class CartAdapter(
    private val context: Context,
    private val onDeleteClick: (Cart) -> Unit,
    private val onQuantityChange: (Cart, Int) -> Unit
) : ListAdapter<CartGroupItem, RecyclerView.ViewHolder>(CartGroupItemDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getCurrentList()[position]) {
            is CartGroupItem.Header -> VIEW_TYPE_HEADER
            is CartGroupItem.Item -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemCartHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemCartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CartItemViewHolder(binding, context, onDeleteClick, onQuantityChange)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getCurrentList()[position]) {
            is CartGroupItem.Header -> (holder as HeaderViewHolder).bind(item.title)
            is CartGroupItem.Item -> (holder as CartItemViewHolder).bind(item.cart)
        }
    }

    fun submitGroupedList(list: List<Cart>) {
        val groupedItems = list
            .sortedByDescending { it.created_at }
            .groupBy { cart ->
                when {
                    cart.created_at?.isToday() == true -> "Сегодня"
                    cart.created_at?.isYesterday() == true -> "Вчера"
                    else -> java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(cart.created_at)
                }
            }
            .flatMap { (date, items) ->
                listOf(CartGroupItem.Header(date)) +
                        items.map { CartGroupItem.Item(it) }
            }
        submitList(groupedItems)
    }

    class HeaderViewHolder(private val binding: ItemCartHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.headerTitle.text = title
        }
    }

    class CartItemViewHolder(
        private val binding: ItemCartBinding,
        private val context: Context,
        private val onDeleteClick: (Cart) -> Unit,
        private val onQuantityChange: (Cart, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            Glide.with(context)
                .load(cart.product.image)
                .into(binding.productImage)

            binding.apply {
                productName.text = cart.product.name
                productPrice.text = "${cart.product.price}"
                quantity.text = "${cart.quantity}"

                btnPlus.setOnClickListener {
                    val newQuantity = cart.quantity!! + 1
                    onQuantityChange(cart, newQuantity)
                }

                btnMinus.setOnClickListener {
                    if (cart.quantity!! > 1) {
                        val newQuantity = cart.quantity!! - 1
                        onQuantityChange(cart, newQuantity)
                    }
                }

                btnDelete.setOnClickListener {
                    onDeleteClick(cart)
                }
            }
        }
    }
}

class CartGroupItemDiffCallback : DiffUtil.ItemCallback<CartGroupItem>() {
    override fun areItemsTheSame(oldItem: CartGroupItem, newItem: CartGroupItem): Boolean {
        return when {
            oldItem is CartGroupItem.Header && newItem is CartGroupItem.Header ->
                oldItem.title == newItem.title
            oldItem is CartGroupItem.Item && newItem is CartGroupItem.Item ->
                oldItem.cart.product.id == newItem.cart.product.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: CartGroupItem, newItem: CartGroupItem): Boolean {
        return oldItem == newItem
    }
}
