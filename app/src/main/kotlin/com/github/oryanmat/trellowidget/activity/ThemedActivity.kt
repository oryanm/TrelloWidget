package com.github.oryanmat.trellowidget.activity

import android.app.Activity
import android.os.Bundle
import com.github.oryanmat.trellowidget.TrelloWidget

abstract class ThemedActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as TrelloWidget
        setTheme(app.dialogTheme)
    }
}