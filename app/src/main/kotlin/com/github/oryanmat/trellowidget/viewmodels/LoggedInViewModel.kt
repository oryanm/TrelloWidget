package com.github.oryanmat.trellowidget.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.oryanmat.trellowidget.data.TrelloWidgetRepository
import com.github.oryanmat.trellowidget.data.model.User
import com.github.oryanmat.trellowidget.util.network.DataStatus
import kotlinx.coroutines.launch

class LoggedInViewModel(private val repository: TrelloWidgetRepository) : ViewModel() {
    val user: LiveData<DataStatus<User>> = repository.user
    var loginAttempts = 0

    fun tryLogin() {
        loginAttempts++
        viewModelScope.launch {
            repository.getUser()
        }
    }
}