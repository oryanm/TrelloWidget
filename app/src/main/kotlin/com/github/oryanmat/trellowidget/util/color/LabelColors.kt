package com.github.oryanmat.trellowidget.util.color

import android.graphics.Color

const val PINK = "#FF80CE"
const val ORANGE = "#FFAB4A"
const val LIME = "#51E898"
const val SKY = "#00C2E0"
const val BLUE = "#0079BF"
const val GREEN = "#61BD4F"
const val YELLOW = "#F2D600"
const val RED = "#EB5A46"
const val PURPLE = "#C377E0"
const val LIGHT_GREY = "#C4C9CC"

val colors = mapOf(
        "black" to Color.BLACK,
        "purple" to Color.parseColor(PURPLE),
        "pink" to Color.parseColor(PINK),
        "red" to Color.parseColor(RED),
        "orange" to Color.parseColor(ORANGE),
        "yellow" to Color.parseColor(YELLOW),
        "green" to Color.parseColor(GREEN),
        "lime" to Color.parseColor(LIME),
        "sky" to Color.parseColor(SKY),
        "blue" to Color.parseColor(BLUE),
        null to Color.parseColor(LIGHT_GREY))