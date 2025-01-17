package com.amirx.matule_app_sessions.ui.main.favorite
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import kotlinx.coroutines.launch


class FavoriteViewModel : ViewModel() {

    private val _products = MutableLiveData<ResponseState<List<Product>>>()
    val products: LiveData<ResponseState<List<Product>>> = _products


    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _products.value = ResponseState.Loading()
            try {
                val result = ProductRepository().getFavoriteProducts("some_user_id")
                _products.value = ResponseState.Success(result)
            } catch (e: Exception) {
                _products.value =
                    ResponseState.Error(e.message.toString() ?: "Failed to load products")
            }
        }
    }
}

class FavoriteViewModelProvider() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel() as T
    }
}