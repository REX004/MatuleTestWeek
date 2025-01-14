package com.amirx.matule_app_sessions.data.repository

import android.content.Context
import com.amirx.matule_app_sessions.data.datasource.local.SharedPrefsManager
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.ktor.client.plugins.HttpRequestTimeoutException

class AuthRepository {

    suspend fun signUser(
        email: String,
        password: String,
        context: Context
    ): ResponseState<Unit> {

        try {
            SupabaseClient.supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
                val userId = SupabaseClient.supabase.auth.currentUserOrNull()?.id ?: ""
                SharedPrefsManager(context).saveUserToken(userId)
            }
            return ResponseState.Success(Unit)

        } catch (e: HttpRequestTimeoutException) {
            return ResponseState.Error("Time limited")
        } catch (e: HttpRequestException) {
            return ResponseState.Error("No internet")
        } catch (e: RestException) {
            return ResponseState.Error("User not found")
        } catch (e: Exception) {
            return ResponseState.Error("Some error")
        }
    }
}