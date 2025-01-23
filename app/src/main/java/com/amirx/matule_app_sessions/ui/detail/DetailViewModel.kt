package com.amirx.matule_app_sessions.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _product = MutableLiveData<ResponseState<Product>>()
    val product: LiveData<ResponseState<Product>> = _product

    private val _popularProducts = MutableLiveData<ResponseState<List<Product>>>()
    val popularProducts: LiveData<ResponseState<List<Product>>> = _popularProducts

    private val _state = MutableLiveData<ResponseState<Unit>>()
    val state: LiveData<ResponseState<Unit>> = _state

    private val _cart = MutableLiveData<ResponseState<Unit>>()
    val cart: LiveData<ResponseState<Unit>> = _cart

    fun getPopularProducts() {
        viewModelScope.launch {
            _popularProducts.postValue(ResponseState.Loading())
            try {
                val products = repository.getProducts()
                _popularProducts.postValue(ResponseState.Success(products))
            } catch (e: Exception) {
                _popularProducts.postValue(ResponseState.Error(e.message.toString()))
            }
        }
    }

    fun saveToCart(product: Product, user_id: String) {
        viewModelScope.launch {
            _cart.value = ResponseState.Loading()
            try {
                val result = repository.saveToCart(product, user_id)
                _cart.value = ResponseState.Success(result)
            } catch (e: Exception) {
                _cart.value = ResponseState.Error("Failed add to cart")
            }
        }
    }

    fun saveToFavorite(favorite: Product, user_id: String) {
        viewModelScope.launch {
            _state.value = ResponseState.Loading()
            try {
                val result = repository.saveToFavorite(favorite, user_id)
                _state.value = ResponseState.Success(Unit)
            } catch (e: Exception) {
                _state.value = ResponseState.Error("Failed save to favorite: ${e.message}")
            }
        }
    }

    fun deleteFromFavorite(favorite: Product, user_id: String) {
        viewModelScope.launch {
            _state.value = ResponseState.Loading()
            try {
                val result = repository.deleteFromFavorite(favorite, user_id)
                _state.value = ResponseState.Success(Unit)
            } catch (e: Exception) {
                _state.value = ResponseState.Error("Failed save to favorite: ${e.message}")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val repository: ProductRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}