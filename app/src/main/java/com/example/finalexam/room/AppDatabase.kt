package com.example.finalexam.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Info::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMethods(): Methods
}