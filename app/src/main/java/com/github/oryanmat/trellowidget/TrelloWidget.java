package com.github.oryanmat.trellowidget;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.github.oryanmat.trellowidget.model.Board;
import com.github.oryanmat.trellowidget.model.BoardList;
import com.github.oryanmat.trellowidget.util.Json;
import com.github.oryanmat.trellowidget.util.PrefUtil;
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil;
import com.github.oryanmat.trellowidget.widget.AlarmReceiver;

public class TrelloWidget extends Application {
    public static final String T_WIDGET = "TWidget";
    public static final String INTERNAL_PREFS = "com.oryanmat.trellowidget.prefs";
    public static final String LIST_KEY = "";
    public static final String BOARD_KEY = ".board";
    private static final boolean DEBUG = false;

    @Override
    public void onCreate() {
        if (DEBUG) StrictMode.enableDefaults();
        super.onCreate();
        TrelloAPIUtil.init(getApplicationContext());
        startScheduleAlarmThread();
    }

    private void startScheduleAlarmThread() {
        new Thread(new Runnable() {
            public void run() {
                scheduleAlarm(TrelloWidget.this);
            }
        }).start();
    }

    public static void scheduleAlarm(Context context) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        int interval = PrefUtil.INSTANCE.getInterval(context);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, interval, interval, pendingIntent);
    }

    public static BoardList getList(Context context, int appWidgetId) {
        String key = getPreferenceKey(appWidgetId, LIST_KEY);
        String json = getPreferences(context).getString(key, BoardList.NULL_JSON);
        return Json.INSTANCE.getGson().fromJson(json, BoardList.class);
    }

    public static Board getBoard(Context context, int appWidgetId) {
        String key = getPreferenceKey(appWidgetId, BOARD_KEY);
        String json = getPreferences(context).getString(key, Board.NULL_JSON);
        return Json.INSTANCE.getGson().fromJson(json, Board.class);
    }

    public static void putConfigInfo(Context context, int appWidgetId, Board board, BoardList list) {
        getPreferences(context)
                .edit()
                .putString(getPreferenceKey(appWidgetId, BOARD_KEY), Json.INSTANCE.getGson().toJson(board))
                .putString(getPreferenceKey(appWidgetId, LIST_KEY), Json.INSTANCE.getGson().toJson(list))
                .apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE);
    }

    private static String getPreferenceKey(int appWidgetId, String key) {
        return String.valueOf(appWidgetId) + key;
    }
}
