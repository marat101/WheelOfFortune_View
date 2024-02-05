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
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import androidx.annotation.Px
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


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
        isAntiAlias = true
        this.textSize = 16f.spToPx(context)
    }

    var center = 0f
        private set
    var size = 0
        private set(value) {
            center = value / 2f
            field = value
        }
    @Px
    var edgePadding = 16f.dpToPx(context)
        set(value) {
            field = value
            canvas?.drawWheel()
        }
    private var defaultTextLayoutWidth = 0f

    private var totalValue = 0L

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
        if (items.size == 1) rotate(-180f, center, center)
        defaultTextLayoutWidth = measureWidth(center, center - edgePadding, 180f)
        var startAngle = 0f
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
        drawWithLayer(paint = paint.apply { xfermode = porterDuffXfermode }) {
            canvas?.rotate(-90f, center, center)
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
            rotate(startAngle + (sweepAngle / 2f), center, center)
            val text = item.name
            val staticLayout = StaticLayout.Builder.obtain(
                text,
                0,
                text.length,
                textPaint,
                defaultTextLayoutWidth.roundToInt())
//                    .setMaxLines(2)
//                    .setEllipsize(TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .build()
            val a = measureWidth(center, (center - (edgePadding + staticLayout.height/2)).coerceAtLeast(1f), sweepAngle.absoluteValue)
            drawText(
                item.value.toString(),
                center,
                edgePadding + edgePadding + staticLayout.height,
                paint.apply {
                    textSize = textPaint.textSize
                    textAlign = Paint.Align.CENTER
                })
            translate(center - (staticLayout.width / 2f), edgePadding)
            drawRect(0f, 0f,staticLayout.width.toFloat(), staticLayout.height.toFloat(), arcPaint.apply {
                color = Color.BLACK
                strokeWidth = STROKE_WIDTH
                style = Paint.Style.STROKE
            })
            if (a == 0f) return@drawWithLayer
            staticLayout.draw(this)
        }
    }

    fun prepareBitmap(size: Int) {
        require(size > 0) { "bitmap size must be greater than 0" }
        this.size = size
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        canvas?.drawWheel()
    }

    fun Long.toSweepAngle(reversed: Boolean = false): Float {
        val sweep = (this.toDouble() / totalValue.toDouble()) * 360.0
        return (if (reversed) -sweep else sweep).toFloat()
    }
}