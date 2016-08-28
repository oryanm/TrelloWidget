package com.github.oryanmat.trellowidget.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TrelloWidgetProvider.Companion.updateWidgets(context);
        TrelloWidgetProvider.Companion.updateWidgetsData(context);
    }
}