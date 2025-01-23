package com.amirx.matule_app_sessions.ui.main.notification.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.data.models.Notification
import com.amirx.matule_app_sessions.databinding.ItemNotificationsBinding

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(Comparator()) {

    inner class NotificationViewHolder(private val binding: ItemNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.titleTxt.text = notification.title
            binding.dateTxt.text = notification.created_at
            binding.descriptionTxt.text = notification.description
        }
    }

    class Comparator : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}