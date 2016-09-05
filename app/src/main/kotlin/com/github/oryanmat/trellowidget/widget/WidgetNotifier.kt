package com.github.oryanmat.trellowidget.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.github.oryanmat.trellowidget.R

internal fun Context.updateWidgetsData() =
        widgetManager().notifyAppWidgetViewDataChanged(getWidgetIds(), R.id.card_list)

internal fun Context.updateWidgets() = sendUpdateBroadcast(*getWidgetIds())

internal fun Context.updateWidget(appWidgetId: Int) {
    sendUpdateBroadcast(appWidgetId)
    notifyDataChanged(appWidgetId)
}

private fun Context.sendUpdateBroadcast(vararg appWidgetIds: Int) {
    val intent = Intent(this, TrelloWidgetProvider::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    sendBroadcast(intent)
}

internal fun Context.notifyDataChanged(vararg appWidgetIds: Int) =
        widgetManager().notifyAppWidgetViewDataChanged(appWidgetIds, R.id.card_list)

private fun Context.getWidgetIds() = widgetManager().getAppWidgetIds(trelloComponentName())

private fun Context.trelloComponentName() = ComponentName(this, TrelloWidgetProvider::class.java)

private fun Context.widgetManager() = AppWidgetManager.getInstance(this)