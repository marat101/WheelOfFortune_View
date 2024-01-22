package ru.marat.roulette

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class WheelOfFortune {

    companion object {
        const val STROKE_WIDTH = 3f
    }

    private val arcPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    var center = 0f
        private set
    var size = 0
        private set(value) {
            center = value / 2f
            field = value
        }

    private var totalValue = 0L
        private set


    private var wheelCanvas: Canvas? = null
    var wheelBitmap: Bitmap? = null
        private set

    var items: List<Item> = listOf()
        set(value) {
            totalValue = 0L
            value.forEach { totalValue += it.value }
            field = value
            if (size > 0 && wheelCanvas != null)
                drawWheel(wheelCanvas!!)
        }


    private fun drawWheel(canvas: Canvas) = canvas.run {
        var startAngle = 0f
        items.forEach {
            val sweepAngle = it.value.toSweepAngle(true)
            drawArcs(it,startAngle, sweepAngle)
            startAngle += sweepAngle
        }
    }

    private fun Canvas.drawArcs(item: Item,startAngle: Float, sweepAngle: Float) {
        drawArc(
            RectF(0f, 0f, size.toFloat(), size.toFloat()),
            startAngle,
            sweepAngle,
            true,
            arcPaint.apply {
                color = item.color
                style = Paint.Style.FILL
            }
        )
        val padding = STROKE_WIDTH/2f
        drawArc(
            RectF(padding, padding, size - padding, size - padding),
            startAngle,
            sweepAngle,
            true,
            arcPaint.apply {
                color = Color.BLACK
                strokeWidth = STROKE_WIDTH
                style = Paint.Style.STROKE
            }
        )
    }

    fun prepareBitmap(size: Int = 0) {
        val msize = if (size > 0) {
            this.size = size
            size
        } else this.size
        if (msize < 1) return
        wheelBitmap = Bitmap.createBitmap(msize, msize, Bitmap.Config.ARGB_8888)
        wheelCanvas = Canvas(wheelBitmap!!)
        wheelCanvas!!.rotate(-90f, center, center)
        drawWheel(wheelCanvas!!)
    }

    fun Long.toSweepAngle(reversed: Boolean = false): Float {
        val sweep = (this.toDouble() / totalValue.toDouble()) * 360.0
        return (if (reversed) -sweep else sweep).toFloat()
    }
}