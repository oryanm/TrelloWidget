package com.github.oryanmat.trellowidget.util.color

import android.graphics.Color
import android.support.annotation.ColorInt

@ColorInt fun Int.dim(): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    // half the value component
    hsv[2] *= 0.5f
    return Color.HSVToColor(hsv)
}
