package com.example.finalexam.room

import android.app.Application
import androidx.room.Room
import com.example.finalexam.SharedPreferenceManager
import com.example.finalexam.service.AlarmService

class App : Application() {

    lateinit var db: AppDatabase
    lateinit var preferenceManager: SharedPreferenceManager

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        preferenceManager = SharedPreferenceManager(baseContext)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "App_DB_NAME-1-"
        ).allowMainThreadQueries().build()

        AlarmService.start(baseContext)
    }
}