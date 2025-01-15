package com.amirx.matule_app_sessions.data.repository

import android.util.Log
import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient
import com.amirx.matule_app_sessions.data.models.Product
import io.github.jan.supabase.postgrest.from

class ProductRepository {

    suspend fun getProducts(): List<Product> {
        try {
            val result = SupabaseClient.supabase.from("Product")
                .select()
                .decodeList<Product>()
            Log.d("ProductRepository", result.toString())
            return result
        } catch (e: Exception) {
            Log.e("ProductRepository", e.message.toString())
            return emptyList()
        }
    }

    suspend fun getPopularProducts(): List<Product> {
        try {
            val result = SupabaseClient.supabase.from("Product")
                .select {
                    filter {
                        eq("popular", true)
                    }
                }
                .decodeList<Product>().take(2)
            Log.d("ProductRepository", result.toString())

            return result
        } catch (e: Exception) {
            return emptyList()
        }
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        try {
            val result = SupabaseClient.supabase.from("Product")
                .select {
                    filter {
                        eq("category", category)
                    }
                }
                .decodeList<Product>()
            Log.d("ProductRepository", result.toString())

            return result
        } catch (e: Exception) {
            return emptyList()
        }
    }
}