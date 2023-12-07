package com.gutierre.mylists.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val idProduct: Int = 0,
    val description: String,
    val imgProduct: String = "",
    val brandProduct: String,
    val idCategoryFK: Int,
    val ean: String = "",
    val price: Float,
    val categoryName: String = ""
)
