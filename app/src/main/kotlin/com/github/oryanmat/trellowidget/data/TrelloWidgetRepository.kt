package com.github.oryanmat.trellowidget.data

import android.content.Context
import com.android.volley.Response
import com.github.oryanmat.trellowidget.data.model.BoardList
import com.github.oryanmat.trellowidget.data.remote.TrelloApi

class TrelloWidgetRepository(context: Context) {

    private val api = TrelloApi(context)

    companion object {
        lateinit var instance: TrelloWidgetRepository

        fun init(context: Context) {
            instance = TrelloWidgetRepository(context.applicationContext)
        }
    }

    fun getUser(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) = api.getUserAsync(listener, errorListener)

    fun getBoards(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) = api.getBoardsAsync(listener, errorListener)

    fun getBoardList(listId: String): BoardList = api.getCards(listId)
}