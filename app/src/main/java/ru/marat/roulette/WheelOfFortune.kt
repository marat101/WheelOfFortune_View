package ru.marat.roulette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.text.StaticLayout
import android.text.TextPaint


class WheelOfFortune(private val context: Context) {

    companion object {
        const val STROKE_WIDTH = 3f
    }

    private val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    private val paint = Paint()
    private val arcPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val textPaint = TextPaint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER

        this.textSize = 16f.spToPx(context)
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


    private var canvas: Canvas? = null
    var bitmap: Bitmap? = null
        private set

    var items: List<Item> = listOf()
        set(value) {
            totalValue = 0L
            value.forEach { totalValue += it.value }
            field = value
            canvas?.drawWheel()
        }


    private fun Canvas.drawWheel() {
        var startAngle = 0f
        if (items.size == 1) rotate(-180f, center, center)
        items.forEach {
            val sweepAngle = it.value.toSweepAngle(true)
            drawWithLayer {
                drawItemText(it, startAngle, sweepAngle)
                drawItemArc(it, startAngle, sweepAngle)
            }
            startAngle += sweepAngle
        }
    }

    private fun Canvas.drawItemArc(item: Item, startAngle: Float, sweepAngle: Float) {
        drawWithLayer(paint.apply { xfermode = porterDuffXfermode }) {
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
            val padding = STROKE_WIDTH / 2f
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
    }

    private fun Canvas.drawItemText(item: Item, startAngle: Float, sweepAngle: Float) {
        drawWithLayer {
            rotate(startAngle + sweepAngle / 2f, center, center)
            this.translate(center / 1.2f, 0f)
            drawWithLayer {
                rotate(90f, center, center)
                val bounds = Rect()

                textPaint.getTextBounds(item.name, 0, item.name.length, bounds)
                drawText(item.name, center, center, textPaint)
                drawText(item.value.toString(), center, center + bounds.height() + 10f, textPaint)
            }
        }
    }

    fun prepareBitmap(size: Int = 0) {
        val msize = if (size > 0) {
            this.size = size
            size
        } else this.size
        if (msize < 1) return
        bitmap = Bitmap.createBitmap(msize, msize, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        canvas?.rotate(-90f, center, center)
        canvas?.drawWheel()
    }

    fun Long.toSweepAngle(reversed: Boolean = false): Float {
        val sweep = (this.toDouble() / totalValue.toDouble()) * 360.0
        return (if (reversed) -sweep else sweep).toFloat()
    }
}