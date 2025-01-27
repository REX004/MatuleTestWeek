package com.amirx.matule_app_sessions.ui.detail.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.R
import com.amirx.matule_app_sessions.data.models.Product
import com.bumptech.glide.Glide

class ImageIndicatorAdapter(
    private val context: Context,
    private var productList: List<Product>
) :
    RecyclerView.Adapter<ImageIndicatorAdapter.ViewHolder>() {

    private var selectedPosition = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indicatorImage: ImageView = itemView.findViewById(R.id.cross_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_indicator, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        val imageUrl = product.image

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.indicatorImage)
    }

    override fun getItemCount(): Int = productList.size

    fun setSelectedPosition(position: Int) {
        if (position != selectedPosition) {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    private var itemClickListener: ((position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        itemClickListener = listener
    }
}