package com.github.oryanmat.trellowidget.activity

import android.app.Activity
import android.app.ProgressDialog
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.T_WIDGET
import com.github.oryanmat.trellowidget.model.Board
import com.github.oryanmat.trellowidget.model.BoardList
import com.github.oryanmat.trellowidget.model.BoardList.Companion.BOARD_LIST_TYPE
import com.github.oryanmat.trellowidget.util.*
import com.github.oryanmat.trellowidget.widget.updateWidget
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : Activity(), OnItemSelectedAdapter, Response.Listener<String>, Response.ErrorListener {
    private var appWidgetId = INVALID_APPWIDGET_ID
    private var board: Board = Board()
    private var list: BoardList = BoardList()
    private val dialog: ProgressDialog by lazy { showProgressDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        setWidgetId()
        dialog.show()
        get(TrelloAPIUtil.instance.boards(), this)
    }

    private fun setWidgetId() {
        if (intent.extras != null) {
            appWidgetId = intent.extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
        }

        if (appWidgetId == INVALID_APPWIDGET_ID) {
            finish()
        }
    }

    private fun showProgressDialog(): ProgressDialog {
        val dialog = ProgressDialog(this)
        dialog.setCancelable(false)
        dialog.setMessage(getString(R.string.loading_message))
        return dialog
    }

    private fun get(url: String, listener: ConfigActivity) =
            TrelloAPIUtil.instance.getAsync(url, listener, listener)

    override fun onResponse(response: String) {
        val boards = Json.tryParseJson(response, BOARD_LIST_TYPE, emptyList<Board>())
        board = getBoard(appWidgetId)
        setSpinner(boardSpinner, boards, this, boards.indexOf(board))
    }

    override fun onErrorResponse(error: VolleyError) {
        dialog.dismiss()
        finish()

        Log.e(T_WIDGET, error.toString())
        val text = String.format(getString(R.string.board_load_fail), error)
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent) {
            boardSpinner -> boardSelected(parent, position)
            listSpinner -> list = parent.getItemAtPosition(position) as BoardList
        }
    }

    private fun boardSelected(spinner: AdapterView<*>, position: Int) {
        board = spinner.getItemAtPosition(position) as Board
        list = getList(appWidgetId)
        setSpinner(listSpinner, board.lists, this, board.lists.indexOf(list))
        dialog.dismiss()
    }

    private fun <T> setSpinner(spinner: Spinner, lists: List<T>,
                               listener: AdapterView.OnItemSelectedListener, selectedIndex: Int): Spinner {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lists)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = listener
        spinner.setSelection(if (selectedIndex > -1) selectedIndex else 0)
        return spinner
    }

    fun ok(view: View) {
        if (board.id.isEmpty() || list.id.isEmpty()) return
        putConfigInfo(appWidgetId, board, list)
        updateWidget(appWidgetId)
        returnOk()
    }

    private fun returnOk() {
        val resultValue = Intent()
        resultValue.putExtra(EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    fun cancel(view: View) = finish()
}