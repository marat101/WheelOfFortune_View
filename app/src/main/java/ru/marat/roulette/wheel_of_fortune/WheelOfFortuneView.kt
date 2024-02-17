package ru.marat.roulette.wheel_of_fortune

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.math.absoluteValue
import kotlin.math.min

class WheelOfFortuneView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = false
    }
    private val pointerPaint = Paint()

    private val scope = CoroutineScope(Dispatchers.Main)
    private var pointerPath: Path? = null

    val springForce = SpringForce(166f).apply {//fixme private
        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        stiffness = 10f
    }
    val animatedValue = FloatValueHolder(0f) //fixme private

    @ColorInt
    private var currentColor = Color.TRANSPARENT

    private var spinCount = 0
    val animation = SpringAnimation(animatedValue).apply { //fixme private
        spring = springForce
        minimumVisibleChange = 0.05f
        addUpdateListener { _, _, _ ->
            invalidate()
        }
        addEndListener { _, _, _, _ ->
            spinCount = (springForce.finalPosition / 360.0).toInt()
            currentColor = (animatedValue.value - (360f * spinCount)).getColor()
            animatedValue.value = springForce.finalPosition - (360f * spinCount)
        }
    }

    var items: List<WheelItem> = listOf()
        set(value) {
            field = value
            if (wheel.bitmap != null) wheel.setItems(value)
            invalidate()
        }

    private val wheel = WheelOfFortune(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val maxValue = min(w, h) // max(w, h)
        wheel.prepareBitmap(maxValue)
        wheel.setItems(items)
        pointerPath = drawPointerPath(maxValue)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (items.isNotEmpty()) {
            canvas.drawWithLayer {
                rotate(animatedValue.value, wheel.center, wheel.center)
                wheel.bitmap?.let { drawBitmap(it, 0f, 0f, paint) }
            }
            canvas.drawPointer()
        }
    }

    private fun Canvas.drawPointer() {
        drawWithLayer {
            pointerPath?.let {
                drawPath(it, pointerPaint.apply {
                    color = Color.BLACK
                })
                scale(0.8f, 0.8f, wheel.center, 0f)
                drawPath(
                    it,
                    pointerPaint.apply {
                        color = currentColor
                        strokeWidth = 10f
                        style = Paint.Style.FILL
                    })
            }
        }
    }

    private fun Float.getColor(): Int { //fixme delete
        wheel.items.forEach {
            if (this@getColor in it.startAngle.absoluteValue..it.endAngle.absoluteValue) return it.color
        }
        return Color.TRANSPARENT
    }
    fun getWheelBitmap() = wheel.bitmap?.copy(Bitmap.Config.ARGB_8888, false)
}