package com.github.oryanmat.trellowidget.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.ColorInt
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.RemoteViews

object RemoteViewsUtil {
    val METHOD_SET_ALPHA = "setAlpha"
    val METHOD_SET_COLOR_FILTER = "setColorFilter"
    internal val IMAGE_SCALE = 1.5

    fun setTextView(context: Context, views: RemoteViews,
                    @IdRes textView: Int, text: String,
                    @ColorInt color: Int, @DimenRes dimen: Int) {
        setTextView(views, textView, text, color)
        views.setTextViewTextSize(textView, TypedValue.COMPLEX_UNIT_SP,
                getScaledValue(context, dimen))
    }

    fun setTextView(views: RemoteViews, @IdRes textView: Int,
                    text: String, @ColorInt color: Int) {
        views.setTextViewText(textView, text)
        views.setTextColor(textView, color)
    }

    fun setImage(context: Context, views: RemoteViews,
                 @IdRes view: Int, @DrawableRes image: Int) {
        val drawable = ContextCompat.getDrawable(context, image)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val density = context.resources.displayMetrics.density
        val prefTextScale = PrefUtil.getPrefTextScale(context)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                (bitmap.width.toDouble() * IMAGE_SCALE * prefTextScale.toDouble() / density).toInt(),
                (bitmap.height.toDouble() * IMAGE_SCALE * prefTextScale.toDouble() / density).toInt(), true)
        views.setImageViewBitmap(view, scaledBitmap)
    }

    fun setImageViewColor(views: RemoteViews, @IdRes view: Int, @ColorInt color: Int) {
        val opaqueColor = Color.rgb(red(color), green(color), blue(color))
        views.setInt(view, METHOD_SET_COLOR_FILTER, opaqueColor)
        views.setInt(view, METHOD_SET_ALPHA, alpha(color))
    }

    fun getScaledValue(context: Context, @DimenRes dimen: Int): Float {
        val dimension = context.resources.getDimension(dimen)
        val density = context.resources.displayMetrics.density
        val prefTextScale = PrefUtil.getPrefTextScale(context)
        return dimension * prefTextScale / density
    }
}
