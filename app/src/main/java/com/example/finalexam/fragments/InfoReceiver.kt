package com.example.finalexam.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import com.example.finalexam.room.App
import com.example.finalexam.room.Info
import java.util.*


class InfoReceiver (app: App) : BroadcastReceiver() {

//    var app = app
//
//
//    val formatedTime = SimpleDateFormat("HH:mm").format(Date())

    override fun onReceive(context: Context?, intent: Intent?) {

//        val connectivity: Boolean = (intent?.getIntExtra("state",-1) ?: return) as Boolean
//
//        if (connectivity){
//            Toast.makeText(context,"Internet Connecting", Toast.LENGTH_LONG).show()
//
//        }
           // App.instance.db.getMethods().insert(Info(0,"WakeUp",formatedTime)
//        }else if (earPhoneEnable == 0) {
//            Toast.makeText(context,"EarPhone is unplugged", Toast.LENGTH_LONG).show()
//            App.instance.db.getMethods().insert((Info(0, "WakeUpDear")))
    }
}