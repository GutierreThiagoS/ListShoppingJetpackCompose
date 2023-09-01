package com.example.mylists.di

fun getKoinModuleList() = listOf(
    roomDatabaseModule,
    daoModule,
    repositoryModule,
    viewModelModule
)