package ru.marat.roulette

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Measure available width
 */
fun measureWidth(sideWidth: Float, height: Float): Float {
    return sqrt(sideWidth.pow(2) - height.pow(2)) * 2f
}

/**
 * @return true if out of bounds
 */
fun WheelOfFortune.checkOutOfBounds(
    textLayoutHeight: Float,
    textLayoutWidth: Float,
    sweepAngle: Float
): Boolean {
    val triangleHeight = center - edgePadding - textLayoutHeight
    val leg = textLayoutWidth / 2
    val triangleSide = sqrt(triangleHeight.pow(2) + leg.pow(2))
    val angle = measureAngle(triangleSide, triangleSide, textLayoutWidth)
    return sweepAngle <= angle
}

/**
 * measure the angle of a triangle
 *
 * A,B,C - width of triangle sides
 * @return angle AB
 */
fun measureAngle(a: Float, b: Float, c: Float): Double {
    val cosAB = ((a.pow(2) + b.pow(2)) - c.pow(2)) / (2 * a * b)
    return acos(cosAB) * 180.0 / PI
}