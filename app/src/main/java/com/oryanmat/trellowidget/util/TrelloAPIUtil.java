package com.oryanmat.trellowidget.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.model.BoardList;
import com.oryanmat.trellowidget.model.CardArray;

import java.util.concurrent.ExecutionException;

import static com.oryanmat.trellowidget.TrelloWidget.INTERNAL_PREFS;
import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class TrelloAPIUtil {
    public static final String TOKEN_PREF_KEY = "com.oryanmat.trellowidget.usertoken";
    public static final String APP_KEY = "b250ef70ccf79ea5e107279a91045e6e";
    public static final String BASE_URL = "https://api.trello.com/";
    public static final String API_VERSION = "1/";
    public static final String KEY = String.format("&key=%s", APP_KEY);
    public static final String AUTH_URL = "https://trello.com/1/authorize" +
            "?name=TrelloWidget" +
            KEY +
            "&expiration=never" +
            "&callback_method=fragment" +
            "&return_url=trello-widget://callback";

    public static final String USER = "members/me?fields=fullName,username";
    public static final String BOARDS = "members/me/boards?filter=open&fields=id,name";
    public static final String BOARD_LISTS = "boards/%s?fields=name&lists=open&list_fields=name";
    public static final String LIST_CARDS = "lists/%s?cards=open&card_fields=name,badges";

    public static TrelloAPIUtil instance;

    Context context;
    RequestQueue queue;
    SharedPreferences preferences;

    private TrelloAPIUtil(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        instance = new TrelloAPIUtil(context);
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }

    public String buildURL() {
        String token = preferences.getString(TrelloAPIUtil.TOKEN_PREF_KEY, "");
        return BASE_URL + API_VERSION + "%s" + KEY + "&" + token;
    }

    public String user() {
        return String.format(buildURL(), USER);
    }

    public String boards() {
        return String.format(buildURL(), BOARDS);
    }

    public String boardLists() {
        return String.format(buildURL(), BOARD_LISTS);
    }

    public CardArray getCards(BoardList list) {
        String json = get(String.format(String.format(buildURL(), LIST_CARDS), list.id));

        return Json.tryParseJson(json, CardArray.class, CardArray.oneItemList(json));
    }

    String get(String url) {
        RequestFuture<String> future = RequestFuture.newFuture();
        getRequestQueue().add(new StringRequest(Request.Method.GET, url, future, future));

        return get(future);
    }

    String get(RequestFuture<String> future) {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            String msg = String.format(context.getString(R.string.http_fail), e);
            Log.d(T_WIDGET, msg);
            return msg;
        }
    }

    public void getAsync(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
        getRequestQueue().add(request);
    }
}
