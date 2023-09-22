package com.example.mylists.di

import com.example.mylists.data.repository.ProductRepositoryImp
import com.example.mylists.data.repository.ShoppingRepositoryImp
import com.example.mylists.data.repository.ToDoRepositoryImp
import com.example.mylists.domain.repository.ProductRepository
import com.example.mylists.domain.repository.ShoppingRepository
import com.example.mylists.domain.repository.ToDoRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single { ProductRepositoryImp(
        productDao = get(),
        categoryDao = get(),
        itemShoppingDao = get()
    ) } bind ProductRepository::class

    single { ShoppingRepositoryImp(
        itemShoppingDao = get(),
        productDao = get(),
        categoryDao = get()
    ) } bind ShoppingRepository::class

    single { ToDoRepositoryImp(
        toDoDao = get()
    ) } bind ToDoRepository::class
}