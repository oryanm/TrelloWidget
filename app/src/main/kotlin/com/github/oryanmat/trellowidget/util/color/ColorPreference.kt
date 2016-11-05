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

data class ColorData(val title: CharSequence = "", val color: Int = 0)

/**
 * a Preference class for colors (using a color picker) in the PreferenceFragments/Activities
 */
class ColorPreference @JvmOverloads constructor(
        context: Context, attributeSet: AttributeSet,
        var color: Int = DEFAULT_VALUE,
        var copyData: ColorData = ColorData()) : DialogPreference(context, attributeSet) {

    init {
        dialogLayoutResource = R.layout.color_chooser
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        initPicker(view)
        initCopyButton(view)
    }

    private fun ColorPreference.initPicker(view: View) = with(view.color_picker) {
        addSVBar(view.svbar)
        addOpacityBar(view.opacitybar)
        color = getPersistedInt(DEFAULT_VALUE)
        oldCenterColor = getPersistedInt(DEFAULT_VALUE)
        onColorChangedListener = ColorPicker.OnColorChangedListener { this@ColorPreference.color = it }
        repairSVBar(view, color)
    }

    private fun ColorPreference.initCopyButton(view: View) = with(view.copyButton) {
        visibility = if (copyData.title.isEmpty().not()) View.VISIBLE else View.GONE
        text = context.getString(R.string.pref_title_copy_from_card, copyData.title)
        setOnClickListener {
            view.color_picker.color = copyData.color
            repairSVBar(view, copyData.color)
        }
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

    override fun onGetDefaultValue(array: TypedArray, index: Int): Any = array.getInteger(index, DEFAULT_VALUE)

    fun asColorData() = ColorData(this.title, this.color)

    /**
     * This is a work-around for a defect in holocolorpicker when using black with an SVBar
     */
    fun repairSVBar(view: View, colorToFix: Int) {
        val hsv = FloatArray(3)
        Color.colorToHSV(colorToFix, hsv)
        if (hsv[1] == 0.0f && hsv[2] == 0.0f) {
            view.svbar.setValue(hsv[2])
        }
    }
}