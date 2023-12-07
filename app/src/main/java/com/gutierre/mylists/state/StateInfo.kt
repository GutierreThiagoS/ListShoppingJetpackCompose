package com.gutierre.mylists.state

import java.lang.Exception

sealed class StateInfo{
    data class Success(
        val info: String
    ): StateInfo()

    data class Error(
        val info: String,
        val exception: Exception? = null
    ): StateInfo()
}
