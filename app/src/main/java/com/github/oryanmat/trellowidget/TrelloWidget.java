package com.github.oryanmat.trellowidget;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import com.github.oryanmat.trellowidget.model.BoardList;
import com.github.oryanmat.trellowidget.util.Json;
import com.github.oryanmat.trellowidget.util.PrefUtil;
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil;
import com.github.oryanmat.trellowidget.widget.AlarmReceiver;

public class TrelloWidget extends Application {
    public static final String T_WIDGET = "TWidget";
    public static final String INTERNAL_PREFS = "com.oryanmat.trellowidget.prefs";
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

        int interval = PrefUtil.getInterval(context);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, interval, interval, pendingIntent);
    }

    public static BoardList getList(Context context, int appWidgetId) {
        return Json.get().fromJson(context.getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .getString(String.valueOf(appWidgetId), BoardList.NULL_JSON), BoardList.class);
    }

    public static void putList(Context context, int appWidgetId, BoardList list) {
        context.getSharedPreferences(INTERNAL_PREFS, MODE_PRIVATE)
                .edit()
                .putString(String.valueOf(appWidgetId), Json.get().toJson(list))
                .apply();
    }
}
