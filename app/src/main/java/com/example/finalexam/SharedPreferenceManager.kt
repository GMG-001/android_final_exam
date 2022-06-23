package com.example.finalexam

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferenceManager(
    private val context: Context
) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        lateinit var instance: SharedPreferenceManager
            private set
    }

    init {
        instance = this
    }

    fun setTimer(time_in_seconds: Long) {
        preferences.edit().putLong("timer", time_in_seconds).apply()
    }

    fun getTimerInSeconds(): Long {
        return preferences.getLong("timer", 0)
    }

    fun listenTimerUpdates(onNewValue: (Long) -> Unit) {
        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
            onNewValue.invoke(sharedPreferences.getLong("timer", 0))
        }
    }

    fun deleteTimer() {
        preferences.edit().remove("timer").apply()
    }
}