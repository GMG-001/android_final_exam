package com.example.finalexam.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.finalexam.BuildConfig
import com.example.finalexam.R
import com.example.finalexam.SharedPreferenceManager
import com.example.finalexam.room.App
import com.example.finalexam.room.Methods
import java.text.SimpleDateFormat
import java.util.*

class AlarmService : Service() {

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var db: Methods
    private lateinit var preferences: SharedPreferenceManager

    override fun onCreate() {
        super.onCreate()
        preferences = SharedPreferenceManager.instance
        db = App.instance.db.getMethods()
        notificationBuilder =
            TimerService.createNotification(this, "ALARM", null, null)
        startForeground(2, notificationBuilder.build())
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                db.getAllInfo()?.filter { it.isActive }?.forEach {
                    val currentTime = SimpleDateFormat("HH:mm:ss").format(Date())

                    if ("${it.time}:00" == currentTime) {
                        Handler(Looper.getMainLooper()).post {
                            ring()
                        }
                    }
                }
            }
        }, 1000, 1000)

        return START_STICKY
    }

    companion object {

        fun start(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, AlarmService::class.java))
            } else {
                context.startService(Intent(context, AlarmService::class.java))
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AlarmService::class.java))
        }
    }

    private fun ring() {
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "ALARM_RING")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm Ringing!!!")
                .setContentText("Your alarm is ringing...")
                .setSound("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${BuildConfig.APPLICATION_ID}/${R.raw.samsung_alarm}".toUri())
                .setAutoCancel(true)
                .setOngoing(false)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "ALARM_RING"
            val mChannel =
                NotificationChannel("ALARM_RING", name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(1200, 500, 1200, 500, 1200, 500, 1200)
            mChannel.setSound(
                "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${BuildConfig.APPLICATION_ID}/${R.raw.samsung_alarm}".toUri(),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            manager.createNotificationChannel(mChannel)
        }

        manager.notify(2, notificationBuilder.build())
    }
}