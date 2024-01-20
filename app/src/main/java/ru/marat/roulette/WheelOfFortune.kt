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

class WheelOfFortune @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    val paint = Paint()
    val arcPaint = Paint()

    private var totalValue = .0
    private val scope = CoroutineScope(Dispatchers.Main)
    private var wheelBitmap: Bitmap? = null
    private var wheelCanvas: Canvas? = null

    private val springForce = SpringForce(166f).apply {
        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        stiffness = 15f
    }
    private val animatedValue = FloatValueHolder(0f)

    @ColorInt
    private var currentColor = Color.TRANSPARENT
    private var spinCount = 0
    val animation = SpringAnimation(animatedValue).apply {
        spring = springForce
        minimumVisibleChange = 0.2f
        addUpdateListener { animation, value, _ ->

            currentColor = value.toFloat().getColor()
            invalidate()
        }
        addEndListener { _, _, _, _ ->
            springForce.finalPosition = springForce.finalPosition + (0..(360 * 15)).random()
//            spinCount = (springForce.finalPosition / 360.0).toInt()
        }
    }

    var items: List<Item> = listOf()
        set(value) {
            totalValue = .0
            value.forEach { totalValue += it.value }
            field = value
            requestLayout()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (items.isEmpty()) return
        val maxValue = max(w, h)
        wheelBitmap = Bitmap.createBitmap(maxValue, maxValue, Bitmap.Config.ARGB_8888)
        wheelCanvas = Canvas(wheelBitmap!!)
        drawWheel(wheelCanvas!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withSave {
            rotate(animatedValue.value, wheelCanvas!!.width / 2f, wheelCanvas!!.width / 2f)
            wheelBitmap?.let { drawBitmap(it, 0f, 0f, paint) }
        }
        canvas.drawPointer()

    }

    private fun Canvas.drawPointer() {
        drawLine(
            wheelCanvas!!.width / 2f,
            0f,
            wheelCanvas!!.width / 2f,
            100f,
            arcPaint.apply {
                color = Color.CYAN
                strokeWidth = 20f
            })
        drawLine(
            wheelCanvas!!.width / 2f,
            0f,
            wheelCanvas!!.width / 2f,
            95f,
            arcPaint.apply {
                color = currentColor
                strokeWidth = 10f
            })
    }

    private fun Float.getColor(): Int { //fixme delete
        var startAngle = 0f
        items.forEach {
            val sweepAngle = it.value.toSweepAngle()
            if (this in startAngle..(startAngle + sweepAngle)) return it.color
            startAngle += sweepAngle
        }
        return Color.TRANSPARENT
    }

    private fun drawWheel(canvas: Canvas) = canvas.run {
        canvas.rotate(90f, canvas.width / 2f, canvas.width / 2f)
        var startAngle = 0f
        items.forEach {
            val sweepAngle = it.value.toSweepAngle(true)
            drawArc(
                RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()),
                startAngle,
                sweepAngle,
                true,
                arcPaint.apply { color = it.color }
            )
            startAngle += sweepAngle
        }
    }

    private fun Double.toSweepAngle(reversed: Boolean = false): Float {
        val sweep = (this / totalValue) * 360.0
        return (if (reversed) -sweep else sweep).toFloat()
    }
}