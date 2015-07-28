package com.oryanmat.trellowidget.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.oryanmat.trellowidget.util.HttpErrorListener;
import com.oryanmat.trellowidget.util.Json;
import com.oryanmat.trellowidget.util.OnItemSelectedAdapter;
import com.oryanmat.trellowidget.util.TrelloAPIUtil;
import com.oryanmat.trellowidget.widget.TrelloWidgetProvider;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class ConfigActivity extends Activity {
    int appWidgetId = INVALID_APPWIDGET_ID;
    BoardList list;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setWidgetId();
        showProgressDialog();
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

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading_message));
        dialog.show();
    }

    void get(String url, Response.Listener<String> listener) {
        TrelloAPIUtil.instance.getAsync(url, listener, new HttpErrorListener(this));
    }

    class BoardListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            Board[] boards = Json.tryParseJson(response, Board[].class, new Board[]{});
            setSpinner(R.id.boardSpinner, boards, new BoardsItemSelected());
        }
    }

    class BoardsItemSelected extends OnItemSelectedAdapter {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Board board = (Board) parent.getItemAtPosition(position);
            setList(board);
            setSpinner(R.id.listSpinner, board.lists, new ListsItemSelected());
            dialog.dismiss();
        }

        private void setList(Board board) {
            if (board.lists.length > 0) {
                list = board.lists[0];
            }
        }
    }

    class ListsItemSelected extends OnItemSelectedAdapter {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            list = (BoardList) parent.getItemAtPosition(position);
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
        TrelloWidgetProvider.updateWidget(this, appWidgetId);
        returnOk();
    }

    private void returnOk() {
        Intent resultValue = new Intent();
        resultValue.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}
