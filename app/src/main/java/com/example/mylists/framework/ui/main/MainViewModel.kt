package com.example.mylists.framework.ui.main

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylists.NavigationScreen
import com.example.mylists.domain.model.BottomNavigationItem
import com.example.mylists.domain.model.NavigationSelected
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.repository.ShoppingRepository
import com.example.mylists.state.StateProductBarCode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val shoppingRepository: ShoppingRepository
): ViewModel() {

    private val _navigationState = MutableStateFlow(NavigationSelected())
    val navigationState: StateFlow<NavigationSelected> = _navigationState

    private val _barCodeState = MutableStateFlow<String?>(null)
    val barCodeState: StateFlow<String?> = _barCodeState

    private val _productState = MutableStateFlow<Product?>(null)
    val productState: StateFlow<Product?> = _productState

    private val _categoryState = MutableStateFlow<String?>(null)
    val categoryState: StateFlow<String?> = _categoryState

    val total: Flow<Float?> = shoppingRepository.getTotal()

    val items = arrayListOf(
        BottomNavigationItem(
            title = NavigationScreen.SHOPPING.label,
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            hasNews = false
        ),
        BottomNavigationItem(
            title = NavigationScreen.PRODUCTS.label,
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.List,
            hasNews = false
        ),
        BottomNavigationItem(
            title = NavigationScreen.TO_DO.label,
            selectedIcon = Icons.Filled.EventNote,
            unselectedIcon = Icons.Outlined.EventNote,
            hasNews = false
        ),
        /*BottomNavigationItem(
            title = NavigationScreen.ADD.label,
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            hasNews = false
        ),*/
        BottomNavigationItem(
            title = NavigationScreen.SETTINGS.label,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            hasNews = true,
        ),
    )

    fun setNavigation(title: String, index: Int? = null) {
        _navigationState.value  =
            NavigationSelected(title, index ?: items.indexOf(items.find { it.title == title }))
    }

    fun setBarCode(barCode: String?) {
        _barCodeState.value = barCode
        if (_productState.value != null) _productState.value = null
        if (_categoryState.value != null) _categoryState.value = null
    }

    fun checkProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingRepository.checkProduct()
        }
    }

    fun navigationBadgeCount(title: String) = shoppingRepository.navigationBadgeCount(title)

    fun getBarCodeInProduct(barCode: String, redirect: (info: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("CoroutineExceptionHandler", "$throwable")
        }) {
            val state = shoppingRepository.getProductInBarCode(barCode)
            withContext(Dispatchers.Main) {
                when(state){
                    is StateProductBarCode.SuccessRoom -> {}
                    is StateProductBarCode.SuccessService -> {
                        _productState.value = state.product
                        _categoryState.value = state.categoryName
                        redirect("")
                    }
                    is StateProductBarCode.Error -> {redirect("${state.info} - Code: ${state.code}")}
                }
            }
        }
    }

    fun setProductEdit(product: Product, categoryName: String) {
        if (_productState.value != null) _productState.value = null
        if (_categoryState.value != null) _categoryState.value = null
        _productState.value = product
        _categoryState.value = categoryName
    }

}