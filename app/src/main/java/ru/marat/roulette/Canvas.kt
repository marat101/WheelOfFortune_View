package ru.marat.roulette

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.drawWithLayer(paint: Paint? = null, block: Canvas.() -> Unit) {
    val checkpoint = saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint)
    block()
    restoreToCount(checkpoint)
}