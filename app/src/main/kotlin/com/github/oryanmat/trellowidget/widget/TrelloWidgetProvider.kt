package com.github.oryanmat.trellowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.activity.ConfigActivity
import com.github.oryanmat.trellowidget.data.model.Board
import com.github.oryanmat.trellowidget.util.*
import com.github.oryanmat.trellowidget.util.Constants.TRELLO_PACKAGE_NAME
import com.github.oryanmat.trellowidget.util.Constants.TRELLO_URL
import com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setBackgroundColor
import com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setImage
import com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setImageViewColor
import com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setTextView
import com.github.oryanmat.trellowidget.util.color.lightDim

private const val REFRESH_ACTION = "com.github.oryanmat.trellowidget.refreshAction"
private const val WIDGET_ID = "com.github.oryanmat.trellowidget.widgetId"


class TrelloWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { updateAppWidget(context, appWidgetManager, it) }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            REFRESH_ACTION -> context.notifyDataChanged(intent.getIntExtra(WIDGET_ID, 0))
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // TODO: We should update both the BoardList and Board on a refresh
        val views = RemoteViews(context.packageName, R.layout.trello_widget)
        updateTitleBar(appWidgetId, context, views)
        updateCardList(appWidgetId, context, views)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun updateTitleBar(appWidgetId: Int, context: Context, views: RemoteViews) {
        val board = context.getBoard(appWidgetId)
        val list = context.getList(appWidgetId)
        @ColorInt val foregroundColor = context.getTitleForegroundColor()

        setBackgroundColor(views, R.id.title_bar, context.getTitleBackgroundColor())
        views.setViewVisibility(R.id.board_name,
                if (context.displayBoardName()) View.VISIBLE else View.GONE)
        setTextView(context, views, R.id.board_name, board.name + " / ", foregroundColor, R.dimen.widget_title_text)
        setTextView(context, views, R.id.list_name, list.name, foregroundColor, R.dimen.widget_title_text)
        views.setOnClickPendingIntent(R.id.list_title, getTitleIntent(context, board))

        setImage(context, views, R.id.refreshButt, R.drawable.ic_refresh_white_24dp)
        setImage(context, views, R.id.configButt, R.drawable.ic_settings_white_24dp)
        setImageViewColor(views, R.id.refreshButt, foregroundColor.lightDim())
        setImageViewColor(views, R.id.configButt, foregroundColor.lightDim())
        views.setOnClickPendingIntent(R.id.refreshButt, getRefreshPendingIntent(context, appWidgetId))
        views.setOnClickPendingIntent(R.id.configButt, getReconfigPendingIntent(context, appWidgetId))

        setImageViewColor(views, R.id.divider, foregroundColor)
    }

    private fun updateCardList(appWidgetId: Int, context: Context, views: RemoteViews) {
        setBackgroundColor(views, R.id.card_frame, context.getCardBackgroundColor())
        views.setTextColor(R.id.empty_card_list, context.getCardForegroundColor())
        views.setEmptyView(R.id.card_list, R.id.empty_card_list)
        views.setPendingIntentTemplate(R.id.card_list, getCardPendingIntent(context))
        views.setRemoteAdapter(R.id.card_list, getRemoteAdapterIntent(context, appWidgetId))
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

    private fun getCardPendingIntent(context: Context): PendingIntent {
        // individual card URIs are set in a RemoteViewsFactory.setOnClickFillInIntent
        return PendingIntent.getActivity(context, 0, Intent(Intent.ACTION_VIEW), 0)
    }

    private fun getTitleIntent(context: Context, board: Board): PendingIntent {
        val intent = if (context.isTitleEnabled()) getBoardIntent(context, board) else Intent()
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