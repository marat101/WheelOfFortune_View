package ru.marat.roulette.wheel_of_fortune

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
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import ru.marat.roulette.R
import ru.marat.roulette.wheel_of_fortune.measurements.checkOutOfBounds
import ru.marat.roulette.wheel_of_fortune.measurements.measureText
import ru.marat.roulette.wheel_of_fortune.measurements.measureWidth
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


class WheelOfFortune(
    private val context: Context,
    private val onMeasureItems: (wheelSize: Int) -> List<MeasuredItem>
) {

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
    }

    var center = 0f
        private set
    var size = 0
        private set(value) {
            center = value / 2f
            field = value
        }


    private var totalValue = 0L

    private var canvas: Canvas? = null
    var bitmap: Bitmap? = null
        private set

    private var items: List<MeasuredItem> = listOf()
        set(value) {
            totalValue = 0L
            value.forEach { totalValue += it.value }
            field = value
            canvas?.drawWheel()
        }


    private fun Canvas.drawWheel() = drawWithLayer {
        if (items.size == 1) rotate(-180f, center, center)
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

    private fun Canvas.drawItemArc(item: MeasuredItem, startAngle: Float, sweepAngle: Float) {
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

    private fun Canvas.drawItemContent(item: MeasuredItem, startAngle: Float, sweepAngle: Float) {
        if (!checkOutOfBounds(
                center - ((item.iconRect?.height() ?: 0) + item.edgePadding),
                item.iconRect?.width()?.toFloat() ?: 0f,
                sweepAngle.absoluteValue
            ) || item.icon == null
        ) drawWithLayer {
            rotate(startAngle + (sweepAngle / 2f), center, center)
            if (item.direction == ItemDirection.ACROSS && item.icon != null && item.iconRect != null) {
                drawWithLayer { // todo перенести в отдельную функцию
                    ResourcesCompat.getDrawable(context.resources, item.icon, context.theme)
                        ?.apply {
                            bounds = item.iconRect
                            translate(center - (item.iconRect.height() / 2f), item.edgePadding)
                            draw(this@drawItemContent)
                        }
                }
            }

            if (!item.text.isNullOrBlank())
                drawItemText(item, sweepAngle)
        }
    }


    private fun Canvas.drawItemText(item: MeasuredItem, sweepAngle: Float) {
        val textColor = getTextColor(item.color) // временно(или нет)
        val isAcross = item.direction == ItemDirection.ACROSS
        val edgeOffset =
            if (item.icon != null) (item.iconRect?.height() ?: 0) + item.spacing + item.edgePadding
            else item.edgePadding

        val textLayoutWidth = (if (isAcross)
            measureWidth(center, center - edgeOffset)
        else
            (center - (item.edgePadding + item.centerPadding))).coerceAtLeast(0f).roundToInt()

        var staticLayout: StaticLayout? = measureText(
            text = item.text!!,
            paint = textPaint.apply {
                color = textColor
                textSize = item.textSize ?: 0f
            },
            width = textLayoutWidth,
            align = if (isAcross) Layout.Alignment.ALIGN_CENTER else Layout.Alignment.ALIGN_NORMAL,
            maxLines = if (isAcross) Int.MAX_VALUE else 1
        )


        val outOfBound = if (isAcross) {
            staticLayout =
                staticLayout?.checkLinesOutOfBound(
                    item,
                    textLayoutWidth,
                    textColor,
                    sweepAngle.absoluteValue,
                    edgeOffset
                )
            staticLayout == null
        } else checkOutOfBounds(
            center - (staticLayout!!.width + item.edgePadding),
            (staticLayout.getLineBaseline(0)).toFloat(),
            sweepAngle.absoluteValue
        )

        if (!outOfBound)
            drawWithLayer {
                staticLayout!!
                if (isAcross)
                    translate(center - (staticLayout.width / 2f), edgeOffset)
                else {
                    rotate(-90f, center, 0f)
                    translate(
                        center - (staticLayout.width + item.edgePadding),
                        -(staticLayout.height / 2f)
                    )
                }
                staticLayout.draw(this)
            }
    }

    private fun StaticLayout.checkLinesOutOfBound(
        item: MeasuredItem,
        textLayoutWidth: Int,
        textColor: Int,
        sweepAngle: Float,
        edgeOffset: Float
    ): StaticLayout? {
        var maxLines: Int? = null
        if (sweepAngle >= 180f) {
            val availableHeight = center- edgeOffset
            if (height <= availableHeight) return this
            maxLines = getLineForVertical(availableHeight.roundToInt())
        } else for (i in 0..<lineCount) {
            val lineWidth = getLineWidth(i)
            val lineHeight = getLineBottom(i)
            if (
                checkOutOfBounds(
                    (center - (lineHeight + edgeOffset)).coerceAtLeast(0f),
                    lineWidth,
                    sweepAngle
                )
            ) break
            maxLines = i + 1
        }
        if (item.icon == R.drawable.android) Log.e("TAGTAG MAXLINES", maxLines.toString())
        if (maxLines == lineCount) return this
        return if (maxLines != null) measureText(
            text = item.text!!,
            paint = textPaint.apply {
                color = textColor
                textSize = item.textSize ?: 0f
            },
            width = textLayoutWidth,
            align = Layout.Alignment.ALIGN_CENTER,
            maxLines = maxLines
        ) else null
    }


    fun prepareBitmap(size: Int) {
        require(size > 0) { "bitmap size must be > 0" }
        this.size = size
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        items = onMeasureItems(size)
        canvas?.drawWheel()
    }

    fun Int.toSweepAngle(reversed: Boolean = false): Float {
        val sweep = (this.toDouble() / totalValue.toDouble()) * 360.0
        return (if (reversed) -sweep else sweep).toFloat()
    }

    fun invalidate() {
        items = onMeasureItems(size)
    }
}