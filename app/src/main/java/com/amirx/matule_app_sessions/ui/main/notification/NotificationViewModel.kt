package com.amirx.matule_app_sessions.ui.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Notification
import com.amirx.matule_app_sessions.data.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val _state = MutableLiveData<ResponseState<List<Notification>>>()
    val state: LiveData<ResponseState<List<Notification>>> = _state

    init {
        getNotifications()
    }

    private fun getNotifications() {
        _state.value = ResponseState.Loading()
        viewModelScope.launch {
            try {
                val result = NotificationRepository().getNotifications()
                _state.value = ResponseState.Success(result)
            } catch (e: Exception) {
                _state.value = ResponseState.Error("Failed to get notifications")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class NotificationViewModelProvider : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationViewModel() as T
    }
}