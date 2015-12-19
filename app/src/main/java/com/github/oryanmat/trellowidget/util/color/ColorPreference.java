package com.github.oryanmat.trellowidget.util.color;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.github.oryanmat.trellowidget.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

/**
 * a Preference class for colors (using a color picker) in the PreferenceFragments/Activities
 */
public class ColorPreference extends DialogPreference {
    public static final int DEFAULT_VALUE = Color.BLACK;

    ColorPicker picker;
    int color = DEFAULT_VALUE;

    public ColorPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setDialogLayoutResource(R.layout.color_chooser);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        picker = (ColorPicker) view.findViewById(R.id.color_picker);
        picker.addSVBar((SVBar) view.findViewById(R.id.svbar));
        picker.addOpacityBar((OpacityBar) view.findViewById(R.id.opacitybar));
        picker.setColor(this.getPersistedInt(DEFAULT_VALUE));
        picker.setOldCenterColor(this.getPersistedInt(DEFAULT_VALUE));
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                color = i;
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(color);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            color = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            color = (Integer) defaultValue;
            persistInt(color);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray array, int index) {
        return array.getInteger(index, DEFAULT_VALUE);
    }

    public int getColor() {
        return color;
    }
}
