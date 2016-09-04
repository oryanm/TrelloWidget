package com.github.oryanmat.trellowidget.util

import android.view.View
import android.widget.AdapterView

interface OnItemSelectedAdapter : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }
}