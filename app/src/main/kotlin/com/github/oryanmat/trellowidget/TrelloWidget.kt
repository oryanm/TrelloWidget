package com.github.oryanmat.trellowidget

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import com.github.oryanmat.trellowidget.di.AppModule
import com.github.oryanmat.trellowidget.util.getInterval
import com.github.oryanmat.trellowidget.widget.AlarmReceiver
import java.util.concurrent.Executors

private const val DEBUG = false

class TrelloWidget : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        if (DEBUG) StrictMode.enableDefaults()
        super.onCreate()
        appModule = AppModule(this)
        Executors.callable { scheduleAlarm(this@TrelloWidget) }.call()
    }

    fun scheduleAlarm(context: Context) {
        val interval = context.getInterval().toLong()
        alarmManager(context).setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME, interval, interval, pendingIntent(context))
    }

    fun alarmManager(context: Context) = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun pendingIntent(context: Context): PendingIntent =
            PendingIntent.getBroadcast(context, 0,
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE)
}