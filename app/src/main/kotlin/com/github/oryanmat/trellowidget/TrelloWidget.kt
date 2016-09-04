package com.github.oryanmat.trellowidget

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import com.github.oryanmat.trellowidget.model.Board
import com.github.oryanmat.trellowidget.model.BoardList
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.util.PrefUtil
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil
import com.github.oryanmat.trellowidget.widget.AlarmReceiver
import java.util.concurrent.Executors

val T_WIDGET = "TWidget"
val INTERNAL_PREFS = "com.oryanmat.trellowidget.prefs"
private val LIST_KEY = ""
private val BOARD_KEY = ".board"
private val DEBUG = false

class TrelloWidget : Application() {
    override fun onCreate() {
        if (DEBUG) StrictMode.enableDefaults()
        super.onCreate()
        TrelloAPIUtil.init(applicationContext)
        Executors.callable { scheduleAlarm(this@TrelloWidget) }.call()
    }

    fun scheduleAlarm(context: Context) {
        val interval = PrefUtil.getInterval(context).toLong()
        alarmManager(context).setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME, interval, interval, pendingIntent(context))
    }

    fun alarmManager(context: Context) = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun pendingIntent(context: Context): PendingIntent =
            PendingIntent.getBroadcast(context, 0,
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT)

    companion object {
        fun getList(context: Context, appWidgetId: Int): BoardList {
            val key = prefKey(appWidgetId, LIST_KEY)
            val json = preferences(context).getString(key, BoardList.NULL_JSON)
            return Json.fromJson(json, BoardList::class.java)
        }

        fun getBoard(context: Context, appWidgetId: Int): Board {
            val key = prefKey(appWidgetId, BOARD_KEY)
            val json = preferences(context).getString(key, Board.NULL_JSON)
            return Json.fromJson(json, Board::class.java)
        }

        fun putConfigInfo(context: Context, appWidgetId: Int, board: Board, list: BoardList) =
                preferences(context).edit()
                        .putString(prefKey(appWidgetId, BOARD_KEY), Json.toJson(board))
                        .putString(prefKey(appWidgetId, LIST_KEY), Json.toJson(list))
                        .apply()

        private fun preferences(context: Context) =
                context.getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)

        private fun prefKey(appWidgetId: Int, key: String) = appWidgetId.toString() + key
    }
}