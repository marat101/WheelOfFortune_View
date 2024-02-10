package ru.marat.roulette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.ColorInt
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
        drawWithLayer {
            if (items.size == 1) rotate(-180f, center, center)
            defaultTextLayoutWidth = measureWidth(center, center - edgePadding)
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
        val text = item.name
        if (text.isBlank()) return
        val textColor = getTextColor(item.color) // временно
        drawWithLayer {
            rotate(startAngle + (sweepAngle / 2f), center, center)
            val staticLayout = StaticLayout.Builder.obtain(
                text,
                0,
                text.length,
                textPaint.apply { color = textColor },
                defaultTextLayoutWidth.roundToInt()
            )
//                    .setMaxLines(2)
//                    .setEllipsize(TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .build()

            if (checkOutOfBounds(
                    staticLayout.height.toFloat(),
                    staticLayout.getMaxLineWidth(),
                    sweepAngle.absoluteValue
                )
            ) return@drawWithLayer
            drawText(
                item.value.toString(),
                center,
                edgePadding + edgePadding + staticLayout.height,
                paint.apply {
                    textSize = 8f.spToPx(context)
                    textAlign = Paint.Align.CENTER
                    color = textColor
                })
            translate(center - (staticLayout.width / 2f), edgePadding)
            staticLayout.draw(this)
        }
    }

    fun getTextColor(color: Int): Int {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return if (darkness < 0.5) {
            Color.BLACK // It's a light color
        } else {
            Color.WHITE // It's a dark color
        }
    }

    fun StaticLayout.getMaxLineWidth(): Float {
        val linesWidth = mutableListOf<Float>()
        repeat(lineCount) {
            linesWidth.add(getLineWidth(it))
        }
        return linesWidth.max()
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