package com.amirx.matule_app_sessions.data.repository

import android.util.Log
import androidx.room.Query
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.data.models.Favorite
import com.amirx.matule_app_sessions.data.models.Product
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.ktor.client.plugins.HttpRequestTimeoutException

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

    suspend fun getFavoriteProducts(user_id: String): List<Product> {
        try {
            val result = SupabaseClient.supabase.from("Favorite")
                .select {
                    filter {
                        eq("user_id", user_id)
                    }
                }
                .decodeList<Product>()
            Log.d("ProductRepository", result.toString())
            return result
        } catch (e: Exception) {
            Log.e("ProductRepository", e.message.toString())
            return emptyList()
        }
    }

    suspend fun saveToFavorite(favorite: Product, user_id: String) {
        try {
            val data = Favorite(favorite, user_id)
            val result = SupabaseClient.supabase.from("Favorite").insert(data)
            Log.d("ProductRepository", result.toString())

        } catch (e: Exception) {
        }
    }

    suspend fun deleteFromFavorite(favorite: Product, user_id: String) {
        try {
            val result = SupabaseClient.supabase.from("Favorite").delete {
                filter {
                    eq("product", favorite)
                    eq("user_id", user_id)
                }
            }
            Log.d("ProductRepository", result.toString())

        } catch (e: Exception) {
        }
    }

    suspend fun getProductsCart(user_id: String): List<Cart> {
        try {
            val result = SupabaseClient.supabase.from("Cart")
                .select {
                    filter {
                        eq("user_id", user_id)
                    }
                }
                .decodeList<Cart>()
            Log.d("ProductRepository", result.toString())

            return result
        } catch (e: Exception) {
            return emptyList()
        }
    }

    suspend fun saveToCart(product: Product, user_id: String) {
        try {
            val result = Cart(product = product, user_id = user_id, quantity = 1)
            SupabaseClient.supabase.from("Cart").insert(result)
            Log.d("ProductRepository", result.toString())
        } catch (e: Exception) {

        }
    }

    suspend fun updateQuantity(cart: Cart, newQuantity: Int): ResponseState<Unit> {
        return try {
            SupabaseClient.supabase.from("Cart").update({
                set("quantity", newQuantity)
            }) {
                filter {
                    eq("id", cart.id!!)
                }
            }
            ResponseState.Success(Unit)
        } catch (e: HttpRequestTimeoutException) {
            ResponseState.Error("Time limited")
        } catch (e: HttpRequestException) {
            ResponseState.Error("No internet")
        } catch (e: RestException) {
            ResponseState.Error("Error updating cart item quantity")
        } catch (e: Exception) {
            ResponseState.Error("Some error occurred")
        }
    }

    suspend fun deleteFromCart(cart: Cart): ResponseState<Unit> {
        return try {
            SupabaseClient.supabase.from("Cart").delete {
                filter {
                    eq("id", cart.id!!)
                }
            }
            ResponseState.Success(Unit)
        } catch (e: HttpRequestTimeoutException) {
            ResponseState.Error("Time limited")
        } catch (e: HttpRequestException) {
            ResponseState.Error("No internet")
        } catch (e: RestException) {
            ResponseState.Error("Error deleting item from cart")
        } catch (e: Exception) {
            ResponseState.Error("Some error occurred")
        }
    }

    suspend fun searchProducts(query: String): List<Product> {
        return try {
            SupabaseClient.supabase
                .from("Product")
                .select {
                    filter {
                        ilike("name", "%$query%")

                    }
                    filter {
                        ilike("description", "%$query%")
                    }
                }
                .decodeList<Product>()
        } catch (e: Exception) {
            emptyList()
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

    suspend fun saveFavorite(product: Product, user_id: String) {
        try {
            val result = SupabaseClient.supabase.from("Product")
                .insert(product)
            Log.d("ProductRepository", result.toString())

        } catch (e: Exception) {

        }
    }
}