package com.github.oryanmat.trellowidget.util.color

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import com.github.oryanmat.trellowidget.R
import com.larswerkman.holocolorpicker.ColorPicker
import kotlinx.android.synthetic.main.color_chooser.view.*

val DEFAULT_VALUE = Color.BLACK

/**
 * a Preference class for colors (using a color picker) in the PreferenceFragments/Activities
 */
class ColorPreference @JvmOverloads constructor(
        context: Context, attributeSet: AttributeSet,
        var color: Int = DEFAULT_VALUE) : DialogPreference(context, attributeSet) {

    init {
        dialogLayoutResource = R.layout.color_chooser
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        val picker = view.color_picker
        picker.addSVBar(view.svbar)
        picker.addOpacityBar(view.opacitybar)
        picker.color = getPersistedInt(DEFAULT_VALUE)
        picker.oldCenterColor = getPersistedInt(DEFAULT_VALUE)
        picker.onColorChangedListener = ColorPicker.OnColorChangedListener { color = it }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            persistInt(color)
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            color = this.getPersistedInt(DEFAULT_VALUE)
        } else {
            color = defaultValue as Int
            persistInt(color)
        }
    }

    override fun onGetDefaultValue(array: TypedArray, index: Int): Any {
        return array.getInteger(index, DEFAULT_VALUE)
    }
}