package com.github.oryanmat.trellowidget.data.remote

import android.content.Context
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
import com.github.oryanmat.trellowidget.util.network.DataStatus
import com.github.oryanmat.trellowidget.util.network.VolleyUtil
import com.github.oryanmat.trellowidget.util.preferences

class TrelloApi(appContext: Context) {
    private val volleyUtil = VolleyUtil(appContext)
    private val preferences = appContext.preferences()

    private fun buildURL(query: String) =
        "$BASE_URL$API_VERSION$query$KEY&${preferences.getString(TOKEN_PREF_KEY, "")}"

    fun getCards(id: String): DataStatus<BoardList> {
        val url = buildURL(LIST_CARDS_PATH.format(id))
        return volleyUtil.getSynchronously(url, BoardList::class.java, BoardList.error())
    }

    fun getUser(listener: (DataStatus<User>) -> Unit) {
        val url = buildURL(USER_PATH)
        volleyUtil.getAsynchronously(url, User::class.java, User(), listener)
    }

    fun getBoards(listener: (DataStatus<List<Board>>) -> Unit) {
        val url = buildURL(BOARDS_PATH)
        volleyUtil.getAsynchronously(url, LIST_OF_BOARDS_TYPE, emptyList(), listener)
    }
}