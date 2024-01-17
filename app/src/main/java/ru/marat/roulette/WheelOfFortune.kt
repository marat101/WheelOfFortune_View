package ru.marat.roulette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
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
        val wheelCanvas = Canvas(wheelBitmap!!)
        drawWheel(wheelCanvas)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        wheelBitmap?.let { canvas.drawBitmap(it, 0f, 0f, paint) }
    }

    fun drawWheel(canvas: Canvas) = canvas.run {
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