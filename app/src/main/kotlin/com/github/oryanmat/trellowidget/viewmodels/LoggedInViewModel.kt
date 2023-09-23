package com.github.oryanmat.trellowidget.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.github.oryanmat.trellowidget.data.TrelloWidgetRepository
import com.github.oryanmat.trellowidget.data.model.User

class LoggedInViewModel(private val repository: TrelloWidgetRepository) : ViewModel() {
    var user = User()
    var loadingPanelVisibility = View.VISIBLE
    var loginAttempts = 0
}