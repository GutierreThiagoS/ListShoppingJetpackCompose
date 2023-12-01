package com.example.mylists.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mylists.data.local.AppDataBase
import org.koin.dsl.module

val roomDatabaseModule = module {
    single {
        Room.databaseBuilder(get(), AppDataBase::class.java, "database-lista-de-compras")
            .addMigrations(
                *arrayOf(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4
                )
            )
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
    }
}