package ru.marat.roulette

import android.content.Context
import android.content.res.Resources
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
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


class WheelOfFortune(private val context: Context) {

    companion object {
        const val STROKE_WIDTH = 2.5f
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

    private var iconbm: Bitmap? = null //todo delete

    var iconsSize = 0f
        private set
    var edgePadding = 10f.dpToPx(context)
        set(value) {
            field = value
            canvas?.drawWheel()
        }
    private var defaultTextLayoutWidth = 0f

    var center = 0f
        private set
    var size = 0
        private set(value) {
            center = value / 2f
            iconsSize = value / 14f
            field = value
        }


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


    private fun Canvas.drawWheel() = drawWithLayer {
        if (items.size == 1) rotate(-180f, center, center)
        defaultTextLayoutWidth = measureWidth(center, center - edgePadding)
        var startAngle = 0f
        items.forEach {
            val sweepAngle = it.value.toSweepAngle(true)
            drawWithLayer {
                drawItemContent(it, startAngle, sweepAngle)
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

    private fun Canvas.drawItemContent(item: Item, startAngle: Float, sweepAngle: Float) {
        if (!checkOutOfBounds(
                center - (iconsSize + edgePadding),
                iconsSize,
                sweepAngle.absoluteValue
            ) || item.icon == null
        )
            drawWithLayer {
                rotate(startAngle + (sweepAngle / 2f), center, center)
                item.icon?.let {
                    drawWithLayer { // todo перенести в отдельную функцию
                        ResourcesCompat.getDrawable(context.resources, it, context.theme)?.apply {
                            setBounds(0, 0, iconsSize.roundToInt(), iconsSize.roundToInt())
                            translate(center - (iconsSize / 2f), edgePadding)
                            draw(this@drawItemContent)
                        }
                    }
                }

                if (item.name.isBlank()) return@drawWithLayer
                drawItemText(item, if (item.icon == null) 0f else iconsSize, sweepAngle)
            }
    }


    private fun Canvas.drawItemText(item: Item, iconHeight: Float, sweepAngle: Float) =
        drawWithLayer {
            val textColor = getTextColor(item.color) // временно
            val staticLayout = StaticLayout.Builder.obtain(
                item.name,
                0,
                item.name.length,
                textPaint.apply { color = textColor },
                defaultTextLayoutWidth.roundToInt()
            )
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .build()

            val outOfBound = checkOutOfBounds(
                center - (staticLayout.height + iconHeight + edgePadding),
                staticLayout.getMaxLineWidth(),
                sweepAngle.absoluteValue
            )

            if (outOfBound) return@drawWithLayer
            drawText( //fixme убрать потом
                item.value.toString(),
                center,
                edgePadding + edgePadding + staticLayout.height + iconHeight,
                paint.apply {
                    textSize = 8f.spToPx(context)
                    textAlign = Paint.Align.CENTER
                    color = textColor
                })
            translate(center - (staticLayout.width / 2f), edgePadding + iconHeight)
            staticLayout.draw(this)
        }

    fun getTextColor(color: Int): Int {
        return if (ColorUtils.calculateLuminance(color) > 0.5) {
            Color.BLACK // If it's a light color
        } else {
            Color.WHITE // If it's a dark color
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