package com.example.mylists.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mylists.data.local.dao.CategoryDao
import com.example.mylists.data.local.dao.ItemShoppingDao
import com.example.mylists.data.local.dao.ProductDao
import com.example.mylists.data.local.dao.ToDoDao
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.model.ToDoItem

@Database(entities = [
    Product::class,
    Category::class,
    ItemShopping::class,
    ToDoItem::class,
                     ], version = 2)
abstract class AppDataBase :RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getItemShoppingDao(): ItemShoppingDao
    abstract fun getToDoDao(): ToDoDao
}