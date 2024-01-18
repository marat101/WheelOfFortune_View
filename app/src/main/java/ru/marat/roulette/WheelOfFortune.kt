package ru.marat.roulette

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
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

    private val springForce = SpringForce(360f).apply {
        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        stiffness = 15f
    }
    private val animatedValue = FloatValueHolder(0f)
    val animation = SpringAnimation(animatedValue).apply {
        spring = springForce
        setMinimumVisibleChange(0.2f)
        addUpdateListener { animation, value, _ ->
            println("TAGTAG ${value}")
            invalidate()
        }
        addEndListener { _, _, _, _ ->
//            animatedValue.value = 0f
            springForce.finalPosition = springForce.finalPosition + (0..(360 * 15)).random()
        }
    }

    var items: List<Item> = listOf()
        set(value) {
            totalValue = .0
            value.forEach { totalValue += it.value }
            field = value
            requestLayout() //fixme
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
        canvas.rotate(animatedValue.value, wheelCanvas!!.width / 2f, wheelCanvas!!.width / 2f)
        wheelBitmap?.let { canvas.drawBitmap(it, 0f, 0f, paint) }
    }

    private fun drawWheel(canvas: Canvas) = canvas.run {
        var sweepAngle = items.first().value.toSweepAngle()
        var startAngle = -(sweepAngle / 2f)
        items.forEachIndexed { index, it ->
            sweepAngle = it.value.toSweepAngle()
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

    private fun Double.toSweepAngle(reversed: Boolean = true): Float {
        val sweep = (this / totalValue) * 360.0
        return (if (reversed) -sweep else sweep).toFloat()
    }
}