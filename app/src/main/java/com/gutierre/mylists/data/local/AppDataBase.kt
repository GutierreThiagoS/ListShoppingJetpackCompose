package com.gutierre.mylists.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gutierre.mylists.data.local.dao.CategoryDao
import com.gutierre.mylists.data.local.dao.ItemShoppingDao
import com.gutierre.mylists.data.local.dao.ProductDao
import com.gutierre.mylists.data.local.dao.ProductFireBaseDao
import com.gutierre.mylists.data.local.dao.ToDoDao
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.model.ProductFireBase
import com.gutierre.mylists.domain.model.ToDoItem

@Database(entities = [
    Product::class,
    Category::class,
    ItemShopping::class,
    ToDoItem::class,
    ProductFireBase::class
],
    version = 1, exportSchema = false
)
abstract class AppDataBase :RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getItemShoppingDao(): ItemShoppingDao
    abstract fun getToDoDao(): ToDoDao
    abstract fun getProductFireBaseDao(): ProductFireBaseDao
}