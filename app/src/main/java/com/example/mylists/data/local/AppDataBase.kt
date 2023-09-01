package com.example.mylists.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mylists.data.local.dao.CategoryDao
import com.example.mylists.data.local.dao.ItemShoppingDao
import com.example.mylists.data.local.dao.ProductDao
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.Product

@Database(entities = [
    Product::class,
    Category::class,
    ItemShopping::class,
                     ], version = 1)
abstract class AppDataBase :RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getItemShoppingDao(): ItemShoppingDao
}