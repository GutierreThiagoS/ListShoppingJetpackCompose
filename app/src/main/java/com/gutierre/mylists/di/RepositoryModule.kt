package com.gutierre.mylists.di

import com.gutierre.mylists.data.repository.CategoryRepositoryImp
import com.gutierre.mylists.data.repository.FireStoreRepositoryImp
import com.gutierre.mylists.data.repository.ProductRepositoryImp
import com.gutierre.mylists.data.repository.ShoppingRepositoryImp
import com.gutierre.mylists.data.repository.ToDoRepositoryImp
import com.gutierre.mylists.domain.repository.CategoryRepository
import com.gutierre.mylists.domain.repository.FireStoreRepository
import com.gutierre.mylists.domain.repository.ProductRepository
import com.gutierre.mylists.domain.repository.ShoppingRepository
import com.gutierre.mylists.domain.repository.ToDoRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single {
        ProductRepositoryImp(
            productDao = get(),
            itemShoppingDao = get()
        )
    } bind ProductRepository::class

    single {
        CategoryRepositoryImp(
            categoryDao = get(),
            productDao = get()
        )
    } bind CategoryRepository::class

    single {
        ShoppingRepositoryImp(
            itemShoppingDao = get(),
            productDao = get(),
            categoryDao = get()
        )
    } bind ShoppingRepository::class

    single {
        ToDoRepositoryImp(
            toDoDao = get()
        )
    } bind ToDoRepository::class

    single {
        FireStoreRepositoryImp(
            productDao = get(),
            categoryDao = get()
        )
    } bind FireStoreRepository::class
}