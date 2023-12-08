package com.gutierre.mylists.framework.presentation.configuration

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierre.mylists.domain.model.CategoryAndCountProduct
import com.gutierre.mylists.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationViewModel(
    private val repository: CategoryRepository
): ViewModel() {

    val categoryList: Flow<List<CategoryAndCountProduct>> = repository.consultCategoryAndCountProductList()

    private val _isUserSupport = MutableStateFlow(false)
    val isUserSupport: StateFlow<Boolean> = _isUserSupport

    private val _isEditCategory= MutableStateFlow(false)
    val isEditCategory: StateFlow<Boolean> = _isEditCategory

    fun editCategory(newName: String, category: CategoryAndCountProduct, context: Context) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable -> Log.e("removeCategory", "$throwable") }) {
            val value = repository.editCategory(newName, category)
            withContext(Dispatchers.Main) {
                Log.e("removeCategory", value)
                Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun removeCategory(category: CategoryAndCountProduct, returnDelete: (value: String) -> Unit) {
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

    fun setEditCategory(value: Boolean) {
        _isEditCategory.value = value
    }
}