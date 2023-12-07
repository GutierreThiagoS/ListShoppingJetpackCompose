package com.gutierre.mylists.di

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(" AlTER TABLE ToDoItem ADD hourAlert TEXT NOT NULL DEFAULT ''")
        db.execSQL(" AlTER TABLE ToDoItem ADD hourInitAlert TEXT NOT NULL DEFAULT ''")
    }
}
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(" AlTER TABLE ToDoItem ADD dateHour INTEGER NOT NULL DEFAULT 0")
    }
}
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(" AlTER TABLE ToDoItem ADD extendTimer INTEGER NOT NULL DEFAULT 5")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(" AlTER TABLE Product ADD categoryName Text NOT NULL DEFAULT ''")
    }
}