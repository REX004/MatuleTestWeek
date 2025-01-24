package com.amirx.matule_app_sessions.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import com.amirx.matule_app_sessions.ui.main.cart.CartViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class CartViewModell(private val repository: ProductRepository) : ViewModel() {
    private val _cartState =
        MutableStateFlow<ResponseState<List<CartGroupItem>>>(ResponseState.Loading())
    val cartState: StateFlow<ResponseState<List<CartGroupItem>>> = _cartState.asStateFlow()

    fun loadCart(userId: String) = viewModelScope.launch {
        try {
            val products = repository.getProductsCart(userId)
            _cartState.value = ResponseState.Success(
                products
                    .sortedByDescending { it.created_at }
                    .groupBy { cart ->
                        when {
                            cart.created_at?.isToday() == true -> "Сегодня"
                            cart.created_at?.isYesterday() == true -> "Вчера"
                            else -> SimpleDateFormat(
                                "dd.MM.yyyy",
                                Locale.getDefault()
                            ).format(cart.created_at)
                        }
                    }
                    .flatMap { (date, items) ->
                        listOf(CartGroupItem.Header(date)) +
                                items.map { CartGroupItem.Item(it) }
                    }
            )
        } catch (e: Exception) {
            _cartState.value = ResponseState.Error("Failed to get cart")
        }
    }
}

class CartViewModellFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModell(ProductRepository()) as T
    }
}
