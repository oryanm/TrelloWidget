package com.github.oryanmat.trellowidget

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.StrictMode

import com.github.oryanmat.trellowidget.model.Board
import com.github.oryanmat.trellowidget.model.BoardList
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.util.PrefUtil
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil
import com.github.oryanmat.trellowidget.widget.AlarmReceiver

val T_WIDGET = "TWidget"
val INTERNAL_PREFS = "com.oryanmat.trellowidget.prefs"
val LIST_KEY = ""
val BOARD_KEY = ".board"
private val DEBUG = false

class TrelloWidget : Application() {
    override fun onCreate() {
        if (DEBUG) StrictMode.enableDefaults()
        super.onCreate()
        TrelloAPIUtil.init(applicationContext)
        startScheduleAlarmThread()
    }

    private fun startScheduleAlarmThread() = Thread(Runnable { scheduleAlarm(this@TrelloWidget) }).start()

    companion object {

        fun scheduleAlarm(context: Context) {
            val pendingIntent = PendingIntent.getBroadcast(context, 0,
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT)

            val interval = PrefUtil.getInterval(context)
            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, interval.toLong(), interval.toLong(), pendingIntent)
        }

        fun getList(context: Context, appWidgetId: Int): BoardList {
            val key = getPreferenceKey(appWidgetId, LIST_KEY)
            val json = getPreferences(context).getString(key, BoardList.NULL_JSON)
            return Json.gson.fromJson(json, BoardList::class.java)
        }

        fun getBoard(context: Context, appWidgetId: Int): Board {
            val key = getPreferenceKey(appWidgetId, BOARD_KEY)
            val json = getPreferences(context).getString(key, Board.NULL_JSON)
            return Json.gson.fromJson(json, Board::class.java)
        }

        fun putConfigInfo(context: Context, appWidgetId: Int, board: Board, list: BoardList) {
            getPreferences(context).edit().putString(getPreferenceKey(appWidgetId, BOARD_KEY), Json.gson.toJson(board)).putString(getPreferenceKey(appWidgetId, LIST_KEY), Json.gson.toJson(list)).apply()
        }

        private fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
        }

        private fun getPreferenceKey(appWidgetId: Int, key: String): String {
            return appWidgetId.toString() + key
        }
    }
}
