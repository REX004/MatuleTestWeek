package com.amirx.matule_app_sessions.ui.main.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.amirx.matule_app_sessions.data.datasource.local.SharedPrefsManager
import com.amirx.matule_app_sessions.data.datasource.network.ResponseState
import com.amirx.matule_app_sessions.data.models.Product
import com.amirx.matule_app_sessions.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val repository: ProductRepository,
    private val sharedPrefsManager: SharedPrefsManager
) : ViewModel() {

    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> = _searchHistory

    private val _searchResults = MutableLiveData<ResponseState<List<Product>>>()
    val searchResults: LiveData<ResponseState<List<Product>>> = _searchResults

    init {

    }

    fun loadSearchHistory() {
        _searchHistory.value = sharedPrefsManager.getSearchHistory()
    }

    fun saveSearchQuery(query: String) {
        sharedPrefsManager.saveSearchQuery(query)
        loadSearchHistory()
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchResults.value = ResponseState.Loading()
            try {
                val products = withContext(Dispatchers.IO) {
                    repository.searchProducts(query)
                }
                _searchResults.value = ResponseState.Success(products)
            } catch (e: Exception) {
                _searchResults.value = ResponseState.Error("Search failed: ${e.message}")
            }
        }
    }
}

class SearchViewModelFactory(private val sharedPrefsManager: SharedPrefsManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(ProductRepository(), sharedPrefsManager) as T
    }

}