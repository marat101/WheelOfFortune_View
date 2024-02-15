package ru.marat.roulette.wheel_of_fortune

import android.content.Context
import android.graphics.Color
import android.text.StaticLayout
import android.util.TypedValue
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt


fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

fun StaticLayout.getMaxLineWidth(): Float {
    val linesWidth = mutableListOf<Float>()
    repeat(lineCount) {
        linesWidth.add(getLineWidth(it))
    }
    return linesWidth.max()
}

fun Float.roundDpToPx(context: Context): Int {
    return dpToPx(context).roundToInt()
}

fun getTextColor(color: Int): Int {
    return if (ColorUtils.calculateLuminance(color) > 0.5) {
        Color.BLACK // If it's a light color
    } else {
        Color.WHITE // If it's a dark color
    }
}