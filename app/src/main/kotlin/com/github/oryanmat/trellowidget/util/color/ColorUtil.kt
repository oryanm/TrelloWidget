package com.github.oryanmat.trellowidget.util.color

import android.graphics.Color
import android.support.annotation.ColorInt

@ColorInt fun Int.dim(value: Float = .5f): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    // set the value component
    hsv[2] *= value
    return Color.HSVToColor(Color.alpha(this), hsv)
}

@ColorInt fun Int.lightDim(): Int = dim(.75f)