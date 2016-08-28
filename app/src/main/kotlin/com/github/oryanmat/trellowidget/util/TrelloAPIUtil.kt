package com.github.oryanmat.trellowidget.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.oryanmat.trellowidget.INTERNAL_PREFS
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.T_WIDGET
import com.github.oryanmat.trellowidget.model.BoardList
import java.util.concurrent.ExecutionException

val TOKEN_PREF_KEY = "com.oryanmat.trellowidget.usertoken"
val APP_KEY = "b250ef70ccf79ea5e107279a91045e6e"
val BASE_URL = "https://api.trello.com/"
val API_VERSION = "1/"
val KEY = String.format("&key=%s", APP_KEY)
val AUTH_URL = "https://trello.com/1/authorize" +
        "?name=TrelloWidget" +
        KEY +
        "&expiration=never" +
        "&callback_method=fragment" +
        "&return_url=trello-widget://callback"

val USER = "members/me?fields=fullName,username"
val BOARDS = "members/me/boards?filter=open&fields=id,name,url" + "&lists=open&list_fields=id,name"
val LIST_CARDS = "lists/%s?cards=open&card_fields=name,badges,labels,url"

class TrelloAPIUtil private constructor(internal var context: Context) {
    internal val queue: RequestQueue by lazy { Volley.newRequestQueue(context) }
    internal var preferences: SharedPreferences = context
            .getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)

    companion object {
        lateinit var instance: TrelloAPIUtil

        fun init(context: Context) {
            instance = TrelloAPIUtil(context)
        }
    }

    fun buildURL(query: String) = "$BASE_URL$API_VERSION$query$KEY&${preferences.getString(TOKEN_PREF_KEY, "")}"

    fun user() = buildURL(USER)

    fun boards() = buildURL(BOARDS)

    fun getCards(list: BoardList): BoardList {
        val json = get(buildURL(LIST_CARDS.format(list.id)))

        return Json.tryParseJson(json, BoardList::class.java, BoardList.oneItemList(json))
    }

    fun getAsync(url: String, listener: Response.Listener<String>, errorListener: Response.ErrorListener) {
        queue.add(StringRequest(Request.Method.GET, url, listener, errorListener))
    }

    private fun get(url: String): String {
        val future = RequestFuture.newFuture<String>()
        queue.add(StringRequest(Request.Method.GET, url, future, future))

        return get(future)
    }

    private fun get(future: RequestFuture<String>) = try {
        future.get()
    } catch (e: ExecutionException) {
        s(e)
    } catch (e: InterruptedException) {
        s(e)
    }

    private fun s(e: Exception): String {
        val msg = String.format(context.getString(R.string.http_fail), e)
        Log.e(T_WIDGET, msg)
        return msg
    }
}