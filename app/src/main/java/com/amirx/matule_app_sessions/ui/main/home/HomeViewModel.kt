package com.amirx.matule_app_sessions.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    private val _products = MutableLiveData<ResponseState<List<Product>>>()
    val products: LiveData<ResponseState<List<Product>>> = _products

    private val _popularProducts = MutableLiveData<ResponseState<List<Product>>>()
    val popularProducts: LiveData<ResponseState<List<Product>>> = _popularProducts

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _products.value = ResponseState.Loading()
            try {
                val result = ProductRepository().getProducts()
                _products.value = ResponseState.Success(result)
            } catch (e: Exception) {
                _products.value =
                    ResponseState.Error(e.message.toString() ?: "Failed to load products")
            }
        }
    }

    fun getAllPopularProducts() {
        viewModelScope.launch {
            _products.value = ResponseState.Loading()
            try {
                val result = ProductRepository().getPopularProducts()
                _products.value = ResponseState.Success(result)
            } catch (e: Exception) {
                _products.value =
                    ResponseState.Error(e.message.toString() ?: "Failed to load popular products")
            }
        }
    }

    fun getPopularProducts() {
        viewModelScope.launch {
            _popularProducts.value = ResponseState.Loading()
            try {
                val result = ProductRepository().getPopularProducts()
                _popularProducts.value = ResponseState.Success(result.take(2))
            } catch (e: Exception) {
                _popularProducts.value =
                    ResponseState.Error(e.message.toString() ?: "Failed to load popular products")
            }
        }
    }

    fun getCategoryProducts(category: String) {
        viewModelScope.launch {
            _products.value = ResponseState.Loading()
            try {
                val result = ProductRepository().getProductsByCategory(category)
                _products.value = ResponseState.Success(result)
            } catch (e: Exception) {
                _products.value =
                    ResponseState.Error(e.message.toString() ?: "Failed to load popular products")
            }
        }
    }
}

class HomeViewModelProvider() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel() as T
    }
}