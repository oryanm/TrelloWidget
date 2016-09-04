package com.github.oryanmat.trellowidget.util.color

import android.graphics.Color

fun dim(color: Int): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    // half the value component
    hsv[2] *= 0.5f
    return Color.HSVToColor(hsv)
}
