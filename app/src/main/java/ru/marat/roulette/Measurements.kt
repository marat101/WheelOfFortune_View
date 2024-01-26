package ru.marat.roulette

import android.util.Log
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Measure available width
 */
fun measureWidth(sideHeight: Float, height: Float, sweepAngle: Float): Float {
//    b=√(a^2-h^2 )/2
    val bottomSideWidth = sqrt(sideHeight.pow(2) - height.pow(2)) / 2f
    val degrees = acos((sideHeight.pow(2) +sideHeight.pow(2) - bottomSideWidth.pow(2)) / (2 * sideHeight.pow(2) * sideHeight.pow(2)))

    Log.e("TAGTAG", "$bottomSideWidth $sweepAngle $degrees")
    return bottomSideWidth
}