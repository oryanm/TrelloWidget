package com.github.oryanmat.trellowidget.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.oryanmat.trellowidget.R;
import com.github.oryanmat.trellowidget.TrelloWidget;
import com.github.oryanmat.trellowidget.model.Board;
import com.github.oryanmat.trellowidget.model.BoardList;
import com.github.oryanmat.trellowidget.util.Json;
import com.github.oryanmat.trellowidget.util.OnItemSelectedAdapter;
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil;
import com.github.oryanmat.trellowidget.widget.TrelloWidgetProvider;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static com.github.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class ConfigActivity extends Activity {
    int appWidgetId = INVALID_APPWIDGET_ID;
    Board board;
    BoardList list;
    Context context;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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

    void get(String url, BoardListener listener) {
        TrelloAPIUtil.instance.getAsync(url, listener, listener);
    }

    class BoardListener implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onResponse(String response) {
            Board[] boards = Json.tryParseJson(response, Board[].class, new Board[]{});
            Board currentBoard = TrelloWidget.getBoard(context, appWidgetId);
            int selectedIndex = getSelectedIndex(boards, currentBoard);
            board = getSelectedItem(boards, selectedIndex);
            setSpinner(R.id.boardSpinner, boards, new BoardsItemSelected(), selectedIndex);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dialog.dismiss();
            finish();

            Log.e(T_WIDGET, error.toString());
            String text = String.format(getString(R.string.board_load_fail), error);
            Toast.makeText(ConfigActivity.this, text, Toast.LENGTH_LONG).show();
        }
    }

    class BoardsItemSelected extends OnItemSelectedAdapter {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            board = (Board) parent.getItemAtPosition(position);
            BoardList currentList = TrelloWidget.getList(context, appWidgetId);
            int selectedIndex = getSelectedIndex(board.lists, currentList);
            list = getSelectedItem(board.lists, selectedIndex);
            setSpinner(R.id.listSpinner, board.lists, new ListsItemSelected(), selectedIndex);
            dialog.dismiss();
        }
    }

    class ListsItemSelected extends OnItemSelectedAdapter {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            list = (BoardList) parent.getItemAtPosition(position);
        }
    }

    private <T> Spinner setSpinner(@IdRes int id, T[] lists, AdapterView.OnItemSelectedListener listener, int selectedIndex) {
        Spinner spinner = (Spinner) findViewById(id);
        ArrayAdapter<T> adapter = new ArrayAdapter<>(
                ConfigActivity.this, android.R.layout.simple_spinner_item, lists);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (selectedIndex > -1) {
            spinner.setSelection(selectedIndex);
        }
        spinner.setOnItemSelectedListener(listener);
        return spinner;
    }

    public void ok(View view) {
        if (board == null || list == null) {
            return;
        }

        TrelloWidget.putConfigInfo(this, appWidgetId, board, list);
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

    public static <T> int getSelectedIndex(T[] array, T currentItem) {
        int selectedIndex = (array.length > 0) ? 0 : -1;
        for (int i = 0; i < array.length; ++i) {
            if (currentItem.equals(array[i])) {
                selectedIndex = i;
                break;
            }
        }
        return selectedIndex;
    }

    public static <T> T getSelectedItem(T[] array, int selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < array.length) {
            return array[selectedIndex];
        }
        return null;
    }
}
