package com.oryanmat.trellowidget.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.oryanmat.trellowidget.R;

import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class HttpErrorListener implements Response.ErrorListener {
    private Context context;

    public HttpErrorListener(Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(T_WIDGET, error.toString());
        String text = String.format(context.getString(R.string.http_fail), error);
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}