package com.github.oryanmat.trellowidget.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.github.oryanmat.trellowidget.data.model.Board
import com.github.oryanmat.trellowidget.data.model.BoardList
import com.github.oryanmat.trellowidget.data.remote.TrelloApi
import com.github.oryanmat.trellowidget.util.DataStatus

class TrelloWidgetRepository(private val trelloApi: TrelloApi) {

    val boards = MutableLiveData<DataStatus<List<Board>>>()

    @WorkerThread
    fun getUser(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) = trelloApi.getUserAsync(listener, errorListener)

    @WorkerThread
    fun getBoards() =
        trelloApi.getBoards { dataStatus ->
            boards.postValue(dataStatus)
        }

    @WorkerThread
    fun getBoardList(listId: String): BoardList = trelloApi.getCards(listId)
}