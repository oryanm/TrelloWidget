package com.github.oryanmat.trellowidget.di

import android.content.Context
import com.github.oryanmat.trellowidget.data.TrelloWidgetRepository
import com.github.oryanmat.trellowidget.data.remote.TrelloApi

class AppModule(val appContext: Context) {

    val trelloWidgetRepository: TrelloWidgetRepository by lazy {
        val trelloApi = TrelloApi(appContext)
        TrelloWidgetRepository(trelloApi)
    }
}