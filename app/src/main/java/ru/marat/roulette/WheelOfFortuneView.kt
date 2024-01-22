package ru.marat.roulette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.withSave
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.Integer.max

class WheelOfFortuneView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private val pointerPaint = Paint()

    private val scope = CoroutineScope(Dispatchers.Main)
    private val wheel = WheelOfFortune()

    private val springForce = SpringForce(166f).apply {
        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        stiffness = 10f
    }
    private val animatedValue = FloatValueHolder(0f)

    @ColorInt
    private var currentColor = Color.TRANSPARENT

    private var spinCount = 0
    val animation = SpringAnimation(animatedValue).apply {
        spring = springForce
        minimumVisibleChange = 0.05f
        addUpdateListener { animation, value, _ ->
            println("TAGTGA $value")
            invalidate()
        }
        addEndListener { _, _, _, _ ->
            spinCount = (springForce.finalPosition / 360.0).toInt()
            currentColor = (animatedValue.value - (360f * spinCount)).getColor()
            animatedValue.value = springForce.finalPosition - (360f * spinCount)
            springForce.finalPosition = animatedValue.value + (0..(360 * 15)).random()
        }
    }

    var items: List<Item> = listOf()
        set(value) {
            field = value
            wheel.items = value
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (items.isEmpty()) return
        val maxValue = max(w, h)
        wheel.prepareBitmap(maxValue)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withSave {
            rotate(animatedValue.value, wheel.center, wheel.center)
            wheel.wheelBitmap?.let { drawBitmap(it, 0f, 0f, paint) }
        }
        canvas.drawPointer()
    }

    private fun Canvas.drawPointer() {
        drawLine(
            wheel.center,
            0f,
            wheel.center,
            100f,
            pointerPaint.apply {
                color = Color.CYAN
                strokeWidth = 20f
                style = Paint.Style.FILL
            })
        drawLine(
            wheel.center,
            0f,
            wheel.center,
            95f,
            pointerPaint.apply {
                color = currentColor
                strokeWidth = 10f
                style = Paint.Style.FILL
            })
    }

    private fun Float.getColor(): Int { //fixme delete
        wheel.run {
        var startAngle = 0f
        items.forEach {
            val sweepAngle = it.value.toSweepAngle()
            if (this@getColor in startAngle..(startAngle + sweepAngle)) return it.color
            startAngle += sweepAngle
        } }
        return Color.TRANSPARENT
    }
}