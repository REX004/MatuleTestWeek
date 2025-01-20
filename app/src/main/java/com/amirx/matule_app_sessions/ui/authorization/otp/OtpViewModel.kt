package com.amirx.matule_app_sessions.ui.authorization.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.repository.AuthRepository

class OtpViewModel : ViewModel() {

    private val _state = MutableLiveData<ResponseState<Unit>>()
    val state: LiveData<ResponseState<Unit>> = _state

    suspend fun checkOtp(email: String, otpCode: String) {
        _state.value = ResponseState.Loading()
        try {
            val result = AuthRepository().checkOtpUser(email, otpCode)
            _state.value = ResponseState.Success(Unit)
        } catch (e: Exception) {
            _state.value = ResponseState.Error("Incorrect otp code")
        }
    }
}

@Suppress("UNCHECKED_CAST")
class OtpViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtpViewModel() as T
    }
}