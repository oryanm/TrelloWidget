package com.github.oryanmat.trellowidget.data.remote

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.oryanmat.trellowidget.util.Constants.T_WIDGET_TAG
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
import com.github.oryanmat.trellowidget.util.DataStatus
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.util.preferences
import java.util.concurrent.ExecutionException

class TrelloApi(appContext: Context) {
    private val queue: RequestQueue by lazy { Volley.newRequestQueue(appContext) }
    private val networkQueue = NetworkQueue(appContext)
    private val preferences = appContext.preferences()
    private fun buildURL(query: String) =
        "$BASE_URL$API_VERSION$query$KEY&${preferences.getString(TOKEN_PREF_KEY, "")}"

    fun getCards(id: String): BoardList {
        val json = get(buildURL(LIST_CARDS_PATH.format(id)))

        return Json.tryParseJson(json, BoardList::class.java, BoardList.error(json))
    }

    fun getUser(listener: (DataStatus<User>) -> Unit) {
        val request = StringRequest(
            Request.Method.GET,
            buildURL(USER_PATH),
            { response ->
                val user = Json.tryParseJson(response, User::class.java, User())
                val dataStatus = DataStatus.success(user)
                listener(dataStatus)
            },
            { error ->
                val dataStatus: DataStatus<User> = DataStatus.error(error.toString())
                listener(dataStatus)
            }
        )
        networkQueue.addToRequestQueue(request)
    }

    fun getBoards(listener: (DataStatus<List<Board>>) -> Unit) {
        val request = StringRequest(
            Request.Method.GET,
            buildURL(BOARDS_PATH),
            { response ->
                val boards = Json.tryParseJson(response, LIST_OF_BOARDS_TYPE, emptyList<Board>())
                val dataStatus = DataStatus.success(boards)
                listener(dataStatus)
            },
            { error ->
                val dataStatus: DataStatus<List<Board>> = DataStatus.error(error.toString())
                listener(dataStatus)
            }
        )
        networkQueue.addToRequestQueue(request)
    }

    private fun get(url: String): String {
        val future = RequestFuture.newFuture<String>()
        queue.add(StringRequest(Request.Method.GET, url, future, future))

        return try {
            future.get()
        } catch (e: ExecutionException) {
            logException(e)
        } catch (e: InterruptedException) {
            logException(e)
        }
    }

    private fun logException(e: Exception): String {
        val msg = "HTTP request to Trello failed: ${e.stackTraceToString()}"
        Log.e(T_WIDGET_TAG, msg)
        return msg
    }
}