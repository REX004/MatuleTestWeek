package com.amirx.matule_app_sessions.ui.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Cart
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _products = MutableLiveData<ResponseState<List<Cart>>>()
    val products: LiveData<ResponseState<List<Cart>>> = _products

    suspend fun getProducts() {
        _products.value = ResponseState.Loading()
        try {
            val products = ProductRepository().getProductsCart("some_user_id")
            _products.value = ResponseState.Success(products)
        } catch (e: Exception) {
            _products.value = ResponseState.Error("Failed to get cart")
        }
    }

    fun deleteFromCart(cart: Cart) {
        viewModelScope.launch {
            val result = ProductRepository().deleteFromCart(cart)
            if (result is ResponseState.Success) {
                getProducts()
            } else if (result is ResponseState.Error) {
            }
        }
    }

    fun updateQuantity(cart: Cart, newQuantity: Int) {
        viewModelScope.launch {
            val result = ProductRepository().updateQuantity(cart, newQuantity)
            if (result is ResponseState.Success) {
                getProducts()
            } else if (result is ResponseState.Error) {
            }
        }
    }
}

class CartViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel() as T
    }
}