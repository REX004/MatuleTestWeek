package com.amirx.matule_app_sessions.data.datasource.local

import android.content.Context

class SharedPrefsManager(baseContext: Context) {
    val sharedPrefs = baseContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun loadImageUrl(imageUrl: String) {
        sharedPrefs.edit()
            .putString("imageUrl", imageUrl)
            .apply()
    }

    fun checkToken(): Boolean {
        val token = sharedPrefs.getString("user_id", "")
        return token.isNullOrEmpty()
    }

    fun saveStateOnboarding(state: Boolean) {
        sharedPrefs.edit()
            .putString("onboarding_state", state.toString())
            .apply()
    }

    fun checkOnboarding(): Boolean {
        val token = sharedPrefs.getString("onboarding_state", "true")
        if (token == "true") {
            return true
        } else {
            return false
        }
    }

    fun getImageUrl(): String {
        val imageUrl = sharedPrefs.getString("imageUrl", "")
        return imageUrl.toString()

    }

    fun loadUserNickName(imageUrl: String) {
        sharedPrefs.edit()
            .putString("user_nick", imageUrl)
            .apply()
    }

    fun saveUserToken(token: String) {
        sharedPrefs.edit()
            .putString("user_id", token)
            .apply()
    }

    fun getUserToken(): String {
        val user_id = sharedPrefs.getString("user_id", "")
        return user_id.toString()
    }

    fun getUserNickName(): String {
        val imageUrl = sharedPrefs.getString("user_nick", "")
        return imageUrl.toString()
    }

}