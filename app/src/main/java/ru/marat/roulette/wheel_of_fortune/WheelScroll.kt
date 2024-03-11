package ru.marat.roulette.wheel_of_fortune

import android.view.MotionEvent
import java.lang.Math.toDegrees
import kotlin.math.absoluteValue
import kotlin.math.atan2

class RotationGestureDetector(private val mListener: OnRotationGestureListener?) {

    private var prevAngle: Float = 0f
    private var angle = 0f

    fun onTouchEvent(event: MotionEvent, center: Float) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> prevAngle =
                angleBetweenLines(center, center, event.x, event.y)

            MotionEvent.ACTION_MOVE -> {
                angle = angleBetweenLines(center, center, event.x, event.y)
                val delta = angle - prevAngle
                if (delta.absoluteValue < 180f) mListener?.onRotation(delta)
                prevAngle = angle
            }

            MotionEvent.ACTION_UP -> prevAngle = 0f

            MotionEvent.ACTION_CANCEL -> prevAngle = 0f
        }
    }

    private fun angleBetweenLines(
        nfX: Float,
        nfY: Float,
        nsX: Float,
        nsY: Float
    ): Float {
        val angle2 = atan2((nfY - nsY).toDouble(), (nfX - nsX).toDouble()).toFloat()
        var angle = toDegrees(angle2.toDouble()).toFloat() % 360
        if (angle < -180f) angle += 360.0f
        if (angle > 180f) angle -= 360.0f
        return angle
    }

    interface OnRotationGestureListener {
        fun onRotation(angleDelta: Float)
    }
}