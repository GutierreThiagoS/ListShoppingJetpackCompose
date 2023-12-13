package com.gutierre.mylists.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.gutierre.mylists.data.local.AppDataBase
import org.koin.dsl.module

val roomDatabaseModule = module {
    single {
        Room.databaseBuilder(get(), AppDataBase::class.java, "database-lista-de-compras")
            /*.addMigrations(
                *arrayOf(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5,
                    MIGRATION_5_6
                )
            )*/
            .fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
    }
}