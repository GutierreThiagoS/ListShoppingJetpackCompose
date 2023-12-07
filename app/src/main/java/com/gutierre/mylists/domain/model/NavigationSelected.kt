package com.gutierre.mylists.domain.model

import com.gutierre.mylists.NavigationScreen

data class NavigationSelected(
    var title: String = NavigationScreen.SHOPPING.label,
    var indexSelected: Int = 0
)
