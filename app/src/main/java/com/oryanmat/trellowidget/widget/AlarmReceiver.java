package com.oryanmat.trellowidget.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(T_WIDGET, "AlarmReceiver.onReceive");
        TrelloWidgetProvider.updateAllWidgets(context);
        TrelloWidgetProvider.updateCardList(context);
    }
}