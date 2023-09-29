package com.example.mylists.state

import com.example.mylists.domain.model.Product

sealed class StateProductBarCode {

    data class SuccessRoom(
        val info: String = "Tem no Banco interno!"
    ): StateProductBarCode()

    data class SuccessService(
        val product: Product,
        val categoryName: String
    ): StateProductBarCode()

    data class Error(
        val info: String,
        val code: Int? = null
    ): StateProductBarCode()
}
