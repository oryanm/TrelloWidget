package com.github.oryanmat.trellowidget.util.color;

import android.graphics.Color;

public class ColorUtil {
    public static int dim(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        // half the value component
        hsv[2] *= 0.5f;
        return Color.HSVToColor(hsv);
    }
}
