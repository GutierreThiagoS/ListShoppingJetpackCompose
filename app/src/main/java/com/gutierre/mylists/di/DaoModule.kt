package com.gutierre.mylists.di

import com.gutierre.mylists.data.local.AppDataBase
import org.koin.dsl.module

val daoModule = module {
    single { get<AppDataBase>().getCategoryDao() }
    single { get<AppDataBase>().getItemShoppingDao() }
    single { get<AppDataBase>().getProductDao() }
    single { get<AppDataBase>().getToDoDao() }
}