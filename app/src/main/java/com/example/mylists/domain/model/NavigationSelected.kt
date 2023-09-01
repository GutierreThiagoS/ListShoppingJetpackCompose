package com.example.mylists.domain.model

import com.example.mylists.NavigationScreen

data class NavigationSelected(
    var title: String = NavigationScreen.SHOPPING.label,
    var indexSelected: Int = 0
)
