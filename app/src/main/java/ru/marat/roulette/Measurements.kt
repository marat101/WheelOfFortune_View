package ru.marat.roulette

import android.util.Log
import kotlin.math.acos
import kotlin.math.acosh
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Measure available width
 */
fun measureWidth(sideWidth: Float, height: Float, sweepAngle: Float): Float {
    val bottomSideWidth = sqrt(sideWidth.pow(2) - height.pow(2)) * 2f
    val angle =
        acos(((sideWidth.pow(2) + sideWidth.pow(2)) - bottomSideWidth.pow(2)) / (2 * sideWidth * sideWidth)) * 180.0 / Math.PI

    Log.e("TAGTAG", "$bottomSideWidth $sweepAngle $angle")
    return if (sweepAngle < angle) 0f else bottomSideWidth
}