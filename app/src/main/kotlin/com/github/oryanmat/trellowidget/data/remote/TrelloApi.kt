package com.github.oryanmat.trellowidget.data.remote

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.oryanmat.trellowidget.data.model.Board
import com.github.oryanmat.trellowidget.data.model.Board.Companion.LIST_OF_BOARDS_TYPE
import com.github.oryanmat.trellowidget.data.model.BoardList
import com.github.oryanmat.trellowidget.data.model.User
import com.github.oryanmat.trellowidget.util.Constants.API_VERSION
import com.github.oryanmat.trellowidget.util.Constants.BASE_URL
import com.github.oryanmat.trellowidget.util.Constants.BOARDS_PATH
import com.github.oryanmat.trellowidget.util.Constants.KEY
import com.github.oryanmat.trellowidget.util.Constants.LIST_CARDS_PATH
import com.github.oryanmat.trellowidget.util.Constants.TOKEN_PREF_KEY
import com.github.oryanmat.trellowidget.util.Constants.USER_PATH
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.util.network.DataStatus
import com.github.oryanmat.trellowidget.util.preferences
import java.lang.reflect.Type

class TrelloApi(appContext: Context) {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(appContext.applicationContext)
    }
    private val preferences = appContext.preferences()

    private fun buildURL(query: String) =
        "$BASE_URL$API_VERSION$query$KEY&${preferences.getString(TOKEN_PREF_KEY, "")}"

    fun getCards(id: String): DataStatus<BoardList> {
        val url = buildURL(LIST_CARDS_PATH.format(id))
        return getSynchronously(url, BoardList::class.java, BoardList.error())
    }

    fun getUser(listener: (DataStatus<User>) -> Unit) {
        val url = buildURL(USER_PATH)
        getAsynchronously(url, User::class.java, User(), listener)
    }

    fun getBoards(listener: (DataStatus<List<Board>>) -> Unit) {
        val url = buildURL(BOARDS_PATH)
        getAsynchronously(url, LIST_OF_BOARDS_TYPE, emptyList(), listener)
    }

    private fun <T> getSynchronously(url: String, type: Type, defaultValue: T): DataStatus<T> {

        val future = RequestFuture.newFuture<String>()
        requestQueue.add(StringRequest(Request.Method.GET, url, future, future))

        return try {
            val json = future.get()
            val data = Json.tryParseJson(json, type, defaultValue)
            DataStatus.success(data)
        } catch (e: Exception) {
            val msg = "HTTP request to Trello failed: ${e.stackTraceToString()}"
            DataStatus.error(msg)
        }
    }

    private fun <T> getAsynchronously(
        url: String,
        type: Type,
        defaultValue: T,
        listener: (DataStatus<T>) -> Unit
    ) {
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val data = Json.tryParseJson(response, type, defaultValue)
                val dataStatus = DataStatus.success(data)
                listener(dataStatus)
            },
            { error ->
                val dataStatus: DataStatus<T> = DataStatus.error(error.toString())
                listener(dataStatus)
            }
        )
        requestQueue.add(request)
    }
}