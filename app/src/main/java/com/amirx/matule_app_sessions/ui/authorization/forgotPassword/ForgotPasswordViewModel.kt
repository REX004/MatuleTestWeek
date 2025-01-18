package com.amirx.matule_app_sessions.ui.authorization.forgotPassword

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.repository.AuthRepository
import com.amirx.matule_app_sessions.domain.usecase.ValidateLoginUseCase
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState> = _state

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val validateLoginUseCase = ValidateLoginUseCase()
    private val _loginErrors = MutableLiveData<Map<String, String>>()
    val loginErrors: LiveData<Map<String, String>> = _loginErrors

    fun loginUser(email: String, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = validateLoginUseCase.execute(email, "hello")) {
                is ResponseState.Success -> {
                    when (val authResult = AuthRepository().getOtpUser(email, context)) {
                        is ResponseState.Success -> {
                            _loading.value = false
                            _state.value = LoginState.Success()
                        }

                        is ResponseState.Error -> {
                            _loading.value = false
                            _state.value = LoginState.Error(authResult.message)
                        }

                        else -> {

                        }
                    }
                }

                is ResponseState.Error -> {
                    val errors = mutableMapOf<String, String>()
                    val errorMessage = result.message
                    if (errorMessage.contains(email)) {
                        errors["email"] = errorMessage
                    } else if (errorMessage.contains("Пароль")) {
                        errors["password"] = errorMessage
                    }
                    _loginErrors.value = errors
                    _loading.value = false
                    _state.value = LoginState.Error(errorMessage)
                }

                else -> {


                }
            }

        }

    }
}

sealed class LoginState {
    class Success : LoginState()
    class Error(val message: String) : LoginState()
    class Loading : LoginState()
}

class ForgotPasswordViewModelProvider() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel() as T
    }
}