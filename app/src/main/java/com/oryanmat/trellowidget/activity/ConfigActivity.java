package com.oryanmat.trellowidget.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;
import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.TrelloWidget;
import com.oryanmat.trellowidget.model.Board;
import com.oryanmat.trellowidget.model.BoardList;
import com.oryanmat.trellowidget.model.ListArray;
import com.oryanmat.trellowidget.util.HttpErrorListener;
import com.oryanmat.trellowidget.util.Json;
import com.oryanmat.trellowidget.util.OnItemSelectedAdapter;
import com.oryanmat.trellowidget.util.TrelloAPIUtil;
import com.oryanmat.trellowidget.widget.TrelloWidgetProvider;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.getInstance;

public class ConfigActivity extends Activity {
    int appWidgetId = INVALID_APPWIDGET_ID;
    BoardList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setWidgetId();
        get(TrelloAPIUtil.instance.boards(), new BoardListener());
    }

    private void setWidgetId() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            appWidgetId = extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    void get(String url, Response.Listener<String> listener) {
        TrelloAPIUtil.instance.getAsync(url, listener, new HttpErrorListener(this));
    }

    class BoardListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            final Board[] boards = Json.tryParseJson(response, Board[].class, new Board[]{});
            setSpinner(R.id.boardSpinner, boards, new OnItemSelectedAdapter() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Board board = (Board) parent.getItemAtPosition(position);
                    get(String.format(TrelloAPIUtil.instance.boardLists(), board.id), new ListListener());
                }
            });
        }
    }

    class ListListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            final ListArray array = Json.tryParseJson(response, ListArray.class, ListArray.oneItemList(response));
            setList(array);
            setSpinner(R.id.listSpinner, array.lists, new OnItemSelectedAdapter() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    list = (BoardList) parent.getItemAtPosition(position);
                }
            });
        }

        private void setList(ListArray lists) {
            if (lists.lists.length > 0) {
                list = lists.lists[0];
            }
        }
    }

    private <T> Spinner setSpinner(@IdRes int id, T[] lists, AdapterView.OnItemSelectedListener listener) {
        Spinner spinner = (Spinner) findViewById(id);
        ArrayAdapter<T> adapter = new ArrayAdapter<>(
                ConfigActivity.this, android.R.layout.simple_spinner_item, lists);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
        return spinner;
    }

    public void ok(View view) {
        if (list == null) {
            return;
        }

        TrelloWidget.putList(this, appWidgetId, list);
        updateWidget();
        returnOk();
    }

    private void returnOk() {
        Intent resultValue = new Intent();
        resultValue.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void updateWidget() {
        Intent intent = new Intent(this, TrelloWidgetProvider.class);
        intent.setAction(ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        sendBroadcast(intent);

        AppWidgetManager appWidgetManager = getInstance(this);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.card_list);
    }

    public void cancel(View view) {
        finish();
    }
}
