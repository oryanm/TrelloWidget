package com.github.oryanmat.trellowidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.github.oryanmat.trellowidget.R;
import com.github.oryanmat.trellowidget.TrelloWidget;
import com.github.oryanmat.trellowidget.model.BoardList;
import com.github.oryanmat.trellowidget.util.PrefUtil;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;
import static com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setImageViewColor;
import static com.github.oryanmat.trellowidget.util.RemoteViewsUtil.setTextView;

public class TrelloWidgetProvider extends AppWidgetProvider {
    private static final String REFRESH_ACTION = "refreshAction";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        BoardList list = TrelloWidget.getList(context, appWidgetId);
        Intent intent = getRemoteAdapterIntent(context, appWidgetId);
        PendingIntent pendingIntent = getRefreshPendingIntent(context);
        @ColorInt int color = PrefUtil.getForegroundColor(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trello_widget);
        setTextView(views, R.id.list_title, list.name, color);
        views.setOnClickPendingIntent(R.id.refreshButt, pendingIntent);
        setImageViewColor(views, R.id.refreshButt, color);
        setImageViewColor(views, R.id.divider, color);
        views.setRemoteAdapter(R.id.card_list, intent);
        views.setEmptyView(R.id.card_list, R.id.empty_card_list);
        views.setTextColor(R.id.empty_card_list, color);
        setImageViewColor(views, R.id.background_image,
                PrefUtil.getBackgroundColor(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private Intent getRemoteAdapterIntent(Context context, int appWidgetId) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        return intent;
    }

    private PendingIntent getRefreshPendingIntent(Context context) {
        Intent refreshIntent = new Intent(context, TrelloWidgetProvider.class);
        refreshIntent.setAction(REFRESH_ACTION);
        return PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(REFRESH_ACTION)) {
            updateWidgetsData(context);
        }
    }

    public static void updateWidgetsData(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName compName = new ComponentName(context, TrelloWidgetProvider.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(compName);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.card_list);
    }

    public static void updateWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName compName = new ComponentName(context, TrelloWidgetProvider.class);
        Intent intent = new Intent(context, TrelloWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                appWidgetManager.getAppWidgetIds(compName));
        context.sendBroadcast(intent);
    }

    public static void updateWidget(Context context, int appWidgetId) {
        Intent intent = new Intent(context, TrelloWidgetProvider.class);
        intent.setAction(ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        context.sendBroadcast(intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.card_list);
    }
}
