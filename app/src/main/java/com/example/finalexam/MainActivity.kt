package com.example.finalexam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalexam.fragments.InfoReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    private lateinit var infoReceiver: InfoReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navController = findNavController(R.id.navigation_fragment)

        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.alarm,
                    R.id.timer
                )
            )
        )
        bottomNavigationView.setupWithNavController(navController)
    }
}

