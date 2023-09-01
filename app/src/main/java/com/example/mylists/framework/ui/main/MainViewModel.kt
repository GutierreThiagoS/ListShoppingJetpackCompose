package com.example.mylists.framework.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.lifecycle.ViewModel
import com.example.mylists.NavigationScreen
import com.example.mylists.domain.model.BottomNavigationItem
import com.example.mylists.domain.model.NavigationSelected
import com.example.mylists.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val shoppingRepository: ShoppingRepository
): ViewModel() {

    private val _navigationState = MutableStateFlow(NavigationSelected())
    val navigationState: StateFlow<NavigationSelected> = _navigationState

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
            title = NavigationScreen.ADD.label,
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            hasNews = false
        ),
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

    fun navigationBadgeCount(title: String) = shoppingRepository.navigationBadgeCount(title)

}