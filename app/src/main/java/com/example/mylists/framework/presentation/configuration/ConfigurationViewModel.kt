package com.example.mylists.framework.presentation.configuration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationViewModel(
    private val repository: ProductRepository
): ViewModel() {

    val categoryList: Flow<List<Category>> = repository.consultCategoryList()

    fun removeCategory(category: Category, returnDelete: (value: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable -> Log.e("removeCategory", "$throwable") }) {
            val value = repository.removerCategoryCheckProducts(category)
            withContext(Dispatchers.Main) {
                Log.e("removeCategory", value)
                returnDelete(value)
            }
        }
    }
}