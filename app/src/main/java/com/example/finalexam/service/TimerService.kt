package com.example.finalexam.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.finalexam.R
import com.example.finalexam.SharedPreferenceManager
import com.example.finalexam.room.App
import com.example.finalexam.room.Methods
import java.util.concurrent.TimeUnit

class TimerService : Service() {

    override fun onBind(p0: Intent?): IBinder? = null

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var db: Methods
    private lateinit var preferences: SharedPreferenceManager

    override fun onCreate() {
        super.onCreate()
        preferences = SharedPreferenceManager.instance
        db = App.instance.db.getMethods()
        notificationBuilder = createNotification(this, "TIMER", "Timer is running", null)
        startForeground(1, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        preferences.listenTimerUpdates {
            String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(it),
                TimeUnit.MILLISECONDS.toSeconds(it) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(it))
            ).let { time ->
                notificationBuilder.setContentTitle(time)
                notificationBuilder.setContentText("Remaining Time")
                updateNotification(this, notificationBuilder.build())
            }
        }

        return START_NOT_STICKY
    }

    companion object {

        lateinit var countdown_timer: CountDownTimer

        fun start(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, TimerService::class.java))
            } else {
                context.startService(Intent(context, TimerService::class.java))
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, TimerService::class.java))
        }

        fun createNotification(
            context: Context,
            channelId: String,
            title: String?,
            message: String?
        ): NotificationCompat.Builder {
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setOngoing(true)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = channelId
                val mChannel = NotificationChannel(channelId, name, IMPORTANCE_LOW)
                mChannel.enableVibration(true)
                notificationManager.createNotificationChannel(mChannel)
            }

            return notificationBuilder
        }

        fun updateNotification(context: Context, notification: Notification) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1, notification)
        }
    }
}