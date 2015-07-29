package com.oryanmat.trellowidget.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TrelloWidgetProvider.updateWidgets(context);
        TrelloWidgetProvider.updateWidgetsData(context);
    }
}