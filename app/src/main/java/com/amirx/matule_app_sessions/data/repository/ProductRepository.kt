package com.amirx.matule_app_sessions.data.repository

import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient
import com.amirx.matule_app_sessions.data.models.Product
import io.github.jan.supabase.postgrest.from

class ProductRepository {

    suspend fun getProducts(): List<Product> {
        try {
            val result = SupabaseClient.supabase.from("Product")
                .select()
                .decodeList<Product>()
            return result
        } catch (e: Exception) {
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
                .decodeList<Product>()
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
            return result
        } catch (e: Exception) {
            return emptyList()
        }
    }
}