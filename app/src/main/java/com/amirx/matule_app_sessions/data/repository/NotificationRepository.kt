package com.amirx.matule_app_sessions.data.repository

import android.util.Log
import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient
import com.amirx.matule_app_sessions.data.models.Notification
import com.amirx.matule_app_sessions.data.models.Product
import io.github.jan.supabase.postgrest.from

class NotificationRepository {

    suspend fun getNotifications(): List<Notification> {
        try {
            val result = SupabaseClient.supabase.from("Notification")
                .select()
                .decodeList<Notification>()
            Log.d("ProductRepository", result.toString())
            return result
        } catch (e: Exception) {
            Log.e("ProductRepository", e.message.toString())
            return emptyList()
        }
    }
}