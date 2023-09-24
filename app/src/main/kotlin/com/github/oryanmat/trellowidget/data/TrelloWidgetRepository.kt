package com.github.oryanmat.trellowidget.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.github.oryanmat.trellowidget.data.model.Board
import com.github.oryanmat.trellowidget.data.model.BoardList
import com.github.oryanmat.trellowidget.data.model.User
import com.github.oryanmat.trellowidget.data.remote.TrelloApi
import com.github.oryanmat.trellowidget.util.DataStatus

class TrelloWidgetRepository(private val trelloApi: TrelloApi) {

    val user = MutableLiveData<DataStatus<User>>()
    val boards = MutableLiveData<DataStatus<List<Board>>>()

    @WorkerThread
    fun getUser() =
        trelloApi.getUser {
            user.postValue(it)
        }

    @WorkerThread
    fun getBoards() =
        trelloApi.getBoards { dataStatus ->
            boards.postValue(dataStatus)
        }

    @WorkerThread
    fun getBoardList(listId: String): BoardList = trelloApi.getCards(listId)
}