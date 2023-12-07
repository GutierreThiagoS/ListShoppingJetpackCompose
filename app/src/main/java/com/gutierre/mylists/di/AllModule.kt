package com.gutierre.mylists.di

fun getKoinModuleList() = listOf(
    roomDatabaseModule,
    daoModule,
    repositoryModule,
    viewModelModule
)