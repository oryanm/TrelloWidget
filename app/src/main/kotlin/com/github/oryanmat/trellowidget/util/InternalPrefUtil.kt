package com.github.oryanmat.trellowidget.util

import android.content.Context
import com.github.oryanmat.trellowidget.model.Board
import com.github.oryanmat.trellowidget.model.BoardList

private val INTERNAL_PREFS = "com.oryanmat.trellowidget.prefs"
private val LIST_KEY = ""
private val BOARD_KEY = ".board"

internal fun Context.getList(appWidgetId: Int): BoardList =
        get(appWidgetId, LIST_KEY, BoardList.NULL_JSON, BoardList::class.java)

internal fun Context.getBoard(appWidgetId: Int): Board =
        get(appWidgetId, BOARD_KEY, Board.NULL_JSON, Board::class.java)

private fun <T> Context.get(appWidgetId: Int, key: String, nullObject: String, c: Class<T>): T =
        Json.fromJson(preferences().getString(prefKey(appWidgetId, key), nullObject)!!, c)

internal fun Context.putConfigInfo(appWidgetId: Int, board: Board, list: BoardList) =
        preferences().edit()
                .putString(prefKey(appWidgetId, BOARD_KEY), Json.toJson(board))
                .putString(prefKey(appWidgetId, LIST_KEY), Json.toJson(list))
                .apply()

internal fun Context.preferences() = getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)

private fun prefKey(appWidgetId: Int, key: String) = appWidgetId.toString() + key