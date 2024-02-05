package ru.marat.roulette

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
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

fun Float.roundDpToPx(context: Context): Int {
    return dpToPx(context).roundToInt()
}