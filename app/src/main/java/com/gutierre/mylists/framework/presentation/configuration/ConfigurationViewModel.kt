package com.gutierre.mylists.framework.presentation.configuration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationViewModel(
    private val repository: ProductRepository
): ViewModel() {

    val categoryList: Flow<List<Category>> = repository.consultCategoryList()

    private val _isUserSupport = MutableStateFlow(false)
    val isUserSupport: StateFlow<Boolean> = _isUserSupport

    fun removeCategory(category: Category, returnDelete: (value: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable -> Log.e("removeCategory", "$throwable") }) {
            val value = repository.removerCategoryCheckProducts(category)
            withContext(Dispatchers.Main) {
                Log.e("removeCategory", value)
                returnDelete(value)
            }
        }
    }

    fun setUserSupport(value: Boolean) {
        _isUserSupport.value = value
    }
}