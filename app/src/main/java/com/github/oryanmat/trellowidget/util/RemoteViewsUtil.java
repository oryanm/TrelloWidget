package com.github.oryanmat.trellowidget.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.RemoteViews;

import static android.graphics.Color.alpha;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class RemoteViewsUtil {
    public static final String METHOD_SET_ALPHA = "setAlpha";
    public static final String METHOD_SET_COLOR_FILTER = "setColorFilter";
    public static final String METHOD_SET_BACKGROUND_COLOR = "setBackgroundColor";
    static final double IMAGE_SCALE = 1.5;

    public static void setTextView(Context context, RemoteViews views,
                                   @IdRes int textView, String text,
                                   @ColorInt int color, @DimenRes int dimen) {
        setTextView(views, textView, text, color);
        views.setTextViewTextSize(textView, TypedValue.COMPLEX_UNIT_SP,
                getScaledValue(context, dimen));
    }

    public static void setTextView(RemoteViews views, @IdRes int textView,
                                   String text, @ColorInt int color) {
        views.setTextViewText(textView, text);
        views.setTextColor(textView, color);
    }

    public static void setImage(Context context, RemoteViews views,
                                @IdRes int view, @DrawableRes int image) {
        Drawable drawable = ContextCompat.getDrawable(context, image);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        float density = context.getResources().getDisplayMetrics().density;
        float prefTextScale = PrefUtil.getPrefTextScale(context);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * IMAGE_SCALE * prefTextScale / density),
                (int) (bitmap.getHeight() * IMAGE_SCALE * prefTextScale / density), true);
        views.setImageViewBitmap(view, scaledBitmap);
    }

    public static void setImageViewColor(RemoteViews views, @IdRes int view, @ColorInt int color) {
        int opaqueColor = Color.rgb(red(color), green(color), blue(color));
        views.setInt(view, METHOD_SET_COLOR_FILTER, opaqueColor);
        views.setInt(view, METHOD_SET_ALPHA, alpha(color));
    }

    public static float getScaledValue(Context context, @DimenRes int dimen) {
        float dimension = context.getResources().getDimension(dimen);
        float density = context.getResources().getDisplayMetrics().density;
        float prefTextScale = PrefUtil.getPrefTextScale(context);
        return dimension * prefTextScale / density;
    }
}
