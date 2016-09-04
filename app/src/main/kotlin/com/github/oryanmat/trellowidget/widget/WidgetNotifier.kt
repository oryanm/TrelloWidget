package com.github.oryanmat.trellowidget.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.github.oryanmat.trellowidget.R

fun updateWidgetsData(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val compName = ComponentName(context, TrelloWidgetProvider::class.java)
    val widgetIds = appWidgetManager.getAppWidgetIds(compName)
    appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.card_list)
}

fun updateWidgets(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val compName = ComponentName(context, TrelloWidgetProvider::class.java)
    sendUpdateBroadcast(context, *appWidgetManager.getAppWidgetIds(compName))
}

fun updateWidget(context: Context, appWidgetId: Int) {
    sendUpdateBroadcast(context, appWidgetId)
    notifyDataChanged(context, appWidgetId)
}

private fun sendUpdateBroadcast(context: Context, vararg appWidgetIds: Int) {
    val intent = Intent(context, TrelloWidgetProvider::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    context.sendBroadcast(intent)
}

fun notifyDataChanged(context: Context, vararg appWidgetIds: Int) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.card_list)
}