package com.github.oryanmat.trellowidget.viewmodels

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.oryanmat.trellowidget.data.TrelloWidgetRepository
import com.github.oryanmat.trellowidget.data.model.Board
import com.github.oryanmat.trellowidget.data.model.BoardList
import com.github.oryanmat.trellowidget.util.network.DataStatus
import com.github.oryanmat.trellowidget.util.getBoard
import com.github.oryanmat.trellowidget.util.getList
import com.github.oryanmat.trellowidget.util.putConfigInfo
import com.github.oryanmat.trellowidget.widget.updateWidget
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val repository: TrelloWidgetRepository,
    private val appContext: Context
) : ViewModel() {
    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    var board: Board = Board()
    var list: BoardList = BoardList()

    val boards: LiveData<DataStatus<List<Board>>> = repository.boards

    fun getBoards() = viewModelScope.launch {
        repository.getBoards()
    }

    fun loadPresentConfig() {
        board = appContext.getBoard(appWidgetId)
        list = appContext.getList(appWidgetId)
    }

    fun isConfigInvalid(): Boolean = board.id.isEmpty() || list.id.isEmpty()
    fun updateConfig() {
        appContext.putConfigInfo(appWidgetId, board, list)
        appContext.updateWidget(appWidgetId)
    }
}