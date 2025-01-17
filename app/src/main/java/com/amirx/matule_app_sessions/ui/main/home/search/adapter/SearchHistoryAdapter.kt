package com.amirx.matule_app_sessions.ui.main.home.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.databinding.ItemSearchBinding

class SearchHistoryAdapter(
    private var history: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    inner class SearchHistoryViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(query: String) {
            binding.historyTxt.text = query
            binding.root.setOnClickListener {
                onItemClick(query)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(history[position])
    }

    fun submitList(newHistory: List<String>) {
        history = newHistory
        notifyDataSetChanged()
    }
}