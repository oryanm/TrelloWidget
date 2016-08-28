package com.github.oryanmat.trellowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.ColorInt
import android.widget.RemoteViews
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.TrelloWidget
import com.github.oryanmat.trellowidget.activity.ConfigActivity
import com.github.oryanmat.trellowidget.model.Board
import com.github.oryanmat.trellowidget.util.PrefUtil
import com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setImageViewColor
import com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setTextView

class TrelloWidgetProvider : AppWidgetProvider() {
    val REFRESH_ACTION = "com.github.oryanmat.trellowidget.refreshAction"
    val WIDGET_ID = "com.github.oryanmat.trellowidget.widgetId"
    val TRELLO_PACKAGE_NAME = "com.trello"
    val TRELLO_URL = "https://www.trello.com"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // TODO: We should update both the BoardList and Board on a refresh
        val list = TrelloWidget.getList(context, appWidgetId)
        val board = TrelloWidget.getBoard(context, appWidgetId)
        @ColorInt val color = PrefUtil.getForegroundColor(context)

        val views = RemoteViews(context.packageName, R.layout.trello_widget)
        setTextView(views, R.id.list_title, list.name, color)
        views.setOnClickPendingIntent(R.id.refreshButt, getRefreshPendingIntent(context, appWidgetId))
        views.setOnClickPendingIntent(R.id.configButt, getReconfigPendingIntent(context, appWidgetId))
        views.setOnClickPendingIntent(R.id.list_title, getTitleIntent(context, board))
        views.setPendingIntentTemplate(R.id.card_list, getCardPendingIntent(context))
        setImageViewColor(views, R.id.refreshButt, color)
        setImageViewColor(views, R.id.configButt, color)
        setImageViewColor(views, R.id.divider, color)
        views.setRemoteAdapter(R.id.card_list, getRemoteAdapterIntent(context, appWidgetId))
        views.setEmptyView(R.id.card_list, R.id.empty_card_list)
        views.setTextColor(R.id.empty_card_list, color)
        setImageViewColor(views, R.id.background_image, PrefUtil.getBackgroundColor(context))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getRemoteAdapterIntent(context: Context, appWidgetId: Int): Intent {
        val intent = Intent(context, WidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        return intent
    }

    private fun getRefreshPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
        val refreshIntent = Intent(context, TrelloWidgetProvider::class.java)
        refreshIntent.action = REFRESH_ACTION
        refreshIntent.putExtra(WIDGET_ID, appWidgetId)
        return PendingIntent.getBroadcast(context, appWidgetId, refreshIntent, 0)
    }

    private fun getReconfigPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
        val reconfigIntent = Intent(context, ConfigActivity::class.java)
        reconfigIntent.action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
        reconfigIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        return PendingIntent.getActivity(context, appWidgetId, reconfigIntent, 0)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == REFRESH_ACTION) {
            notifyDataChanged(context, intent.getIntExtra(WIDGET_ID, 0))
        }
    }

    private fun getCardPendingIntent(context: Context): PendingIntent {
        // individual card URIs are set in a RemoteViewsFactory.setOnClickFillInIntent
        return PendingIntent.getActivity(context, 0, Intent(Intent.ACTION_VIEW), 0)
    }

    private fun getTitleIntent(context: Context, board: Board): PendingIntent {
        val intent = if (PrefUtil.isTitleEnabled(context)) getBoardIntent(context, board) else Intent()
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getBoardIntent(context: Context, board: Board) = if (!board.url.isEmpty()) {
        Intent(Intent.ACTION_VIEW, Uri.parse(board.url))
    } else {
        getTrelloIntent(context)
    }

    private fun getTrelloIntent(context: Context): Intent {
        // try to find trello's app if installed. otherwise just open the website.
        val intent = context.packageManager.getLaunchIntentForPackage(TRELLO_PACKAGE_NAME)
        return intent ?: Intent(Intent.ACTION_VIEW, Uri.parse(TRELLO_URL))
    }
}
