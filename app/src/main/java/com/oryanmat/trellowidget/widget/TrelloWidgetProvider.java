package com.oryanmat.trellowidget.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.TrelloWidget;
import com.oryanmat.trellowidget.model.BoardList;

import static android.graphics.Color.alpha;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class TrelloWidgetProvider extends AppWidgetProvider {
    private static final String METHOD_SET_ALPHA = "setAlpha";
    private static final String METHOD_SET_COLOR_FILTER = "setColorFilter";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(T_WIDGET, "Provider.onUpdate");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        BoardList list = TrelloWidget.getList(context, appWidgetId);

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trello_widget);
        views.setTextViewText(R.id.list_title, list.name);
        views.setRemoteAdapter(R.id.card_list, intent);
        views.setEmptyView(R.id.card_list, R.id.empty_card_list);
        configureBackground(context, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateCardList(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName compName = new ComponentName(context, TrelloWidgetProvider.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(compName);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.card_list);
    }

    public static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName compName = new ComponentName(context, TrelloWidgetProvider.class);
        Intent intent = new Intent(context, TrelloWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                appWidgetManager.getAppWidgetIds(compName));
        context.sendBroadcast(intent);
    }

    private void configureBackground(Context context, RemoteViews rv) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int color = prefs.getInt(context.getString(R.string.pref_back_color_key),
                context.getResources().getInteger(R.integer.pref_back_color_default));
        int opaqueColor = Color.rgb(red(color), green(color), blue(color));
        setColorFilter(rv, R.id.background_image, opaqueColor);
        setAlpha(rv, R.id.background_image, alpha(color));
    }

    static void setAlpha(RemoteViews views, int viewId, int alpha) {
        views.setInt(viewId, METHOD_SET_ALPHA, alpha);
    }

    static void setColorFilter(RemoteViews views, int viewId, int color) {
        views.setInt(viewId, METHOD_SET_COLOR_FILTER, color);
    }
}
