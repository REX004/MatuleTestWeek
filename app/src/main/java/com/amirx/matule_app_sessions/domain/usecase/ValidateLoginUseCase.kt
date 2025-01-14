package com.amirx.matule_app_sessions.domain.usecase

import android.util.Patterns
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.google.android.gms.auth.api.identity.SignInPassword

class ValidateLoginUseCase {
    fun execute(email: String, password: String): ResponseState<Unit> {
        return when {
            isEmailInvalid(email) -> ResponseState.Error("Неверный формат email")
            isPasswordInvalid(password) -> ResponseState.Error("Пароль не может быть пустым")
            else -> ResponseState.Success(Unit)
        }
    }

    private fun isEmailInvalid(email: String): Boolean {
        return email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordInvalid(password: String): Boolean {
        return password.isBlank()
    }
}