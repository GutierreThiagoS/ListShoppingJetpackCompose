package com.example.mylists.di

import com.example.mylists.data.local.AppDataBase
import org.koin.dsl.module

val daoModule = module {
    single { get<AppDataBase>().getCategoryDao() }
    single { get<AppDataBase>().getItemShoppingDao() }
    single { get<AppDataBase>().getProductDao() }
}