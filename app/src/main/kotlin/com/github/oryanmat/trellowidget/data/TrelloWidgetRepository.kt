package com.github.oryanmat.trellowidget.data

import com.android.volley.Response
import com.github.oryanmat.trellowidget.data.model.BoardList
import com.github.oryanmat.trellowidget.data.remote.TrelloApi

class TrelloWidgetRepository(private val trelloApi: TrelloApi) {

    fun getUser(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) = trelloApi.getUserAsync(listener, errorListener)

    fun getBoards(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) = trelloApi.getBoardsAsync(listener, errorListener)

    fun getBoardList(listId: String): BoardList = trelloApi.getCards(listId)
}