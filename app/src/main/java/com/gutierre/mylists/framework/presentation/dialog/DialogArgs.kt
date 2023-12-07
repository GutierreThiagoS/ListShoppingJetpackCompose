package com.gutierre.mylists.framework.presentation.dialog

data class DialogArgs(
    val title: String = "",
    val message: String = "Algo Aconteceu",
    var isShow: Boolean = false,
)
