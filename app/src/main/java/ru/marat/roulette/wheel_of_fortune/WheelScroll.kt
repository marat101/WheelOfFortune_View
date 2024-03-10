package ru.marat.roulette.wheel_of_fortune

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.roundToInt

class WheelScroll(val scrollBy: (degree: Int) -> Unit): GestureDetector.SimpleOnGestureListener() {
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        scrollBy(distanceX.roundToInt())
        return true
    }
}