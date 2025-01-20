package com.amirx.matule_app_sessions.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amirx.matule_app_sessions.data.datasource.local.SharedPrefsManager
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient
import com.amirx.matule_app_sessions.data.datasource.network.SupabaseClient.supabase
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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

            }
            val userId = supabase.auth.currentUserOrNull()?.id ?: ""
            Toast.makeText(context, "Token saved: $userId", Toast.LENGTH_LONG).show()
            SharedPrefsManager(context).saveUserToken(userId)
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

    suspend fun getOtpUser(
        email: String,
        context: Context
    ): ResponseState<Unit> {
        try {
            supabase.auth.signInWith(OTP) {
                this.email = "puknitov@gmail.com"
            }

            return ResponseState.Success(Unit)
        } catch (e: HttpRequestTimeoutException) {
            return ResponseState.Error("Превышено время ожидания")
        } catch (e: HttpRequestException) {
            return ResponseState.Error("Отсутствует подключение к интернету")
        }
    }

    suspend fun checkOtpUser(
        email: String,
        token: String
    ): ResponseState<Unit> {
        try {
            supabase.auth.verifyEmailOtp(OtpType.Email.RECOVERY, email, token)

            return ResponseState.Success(Unit)
        } catch (e: HttpRequestTimeoutException) {
            return ResponseState.Error("Превышено время ожидания")
        } catch (e: HttpRequestException) {
            return ResponseState.Error("Отсутствует подключение к интернету")
        }
    }



    suspend fun signupUser(
        email: String,
        password: String,
        name: String,
        context: Context
    ): ResponseState<Unit> {

        try {
            SupabaseClient.supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put("name", name)
                }
            }
            val userId = supabase.auth.currentUserOrNull()?.id ?: ""
            Toast.makeText(context, "Token saved: $userId", Toast.LENGTH_LONG).show()
            SharedPrefsManager(context).saveUserToken(userId)
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