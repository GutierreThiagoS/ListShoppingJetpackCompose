package com.gutierre.mylists.framework.presentation.add

import androidx.compose.ui.text.input.KeyboardType

data class ItemFieldAddProd(
    val label: String = "Digite o texto",
    var textEdit: String = "",
    val keyboardType: KeyboardType = KeyboardType.Text
)
