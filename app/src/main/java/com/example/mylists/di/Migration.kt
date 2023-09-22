package com.example.mylists.di

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS ToDoItem ( 
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                title TEXT NOT NULL, 
                description TEXT NOT NULL, 
                dateCreate TEXT NOT NULL, 
                dateFinal TEXT NOT NULL, 
                dateUpdate TEXT NOT NULL, 
                level INTEGER NOT NULL, 
                alert INTEGER NOT NULL, 
                concluded INTEGER NOT NULL, 
                deleted INTEGER NOT NULL 
            )
        """)
    }

}