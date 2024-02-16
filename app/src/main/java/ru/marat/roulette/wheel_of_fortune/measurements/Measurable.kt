package ru.marat.roulette.wheel_of_fortune.measurements

import android.content.Context
import android.graphics.Rect
import ru.marat.roulette.wheel_of_fortune.dpToPx
import ru.marat.roulette.wheel_of_fortune.roundDpToPx
import ru.marat.roulette.wheel_of_fortune.spToPx
import kotlin.math.roundToInt

interface Measurable {
    fun measure(context: Context, wheelSize: Int): Float
}

class Dp(private val value: Float) : Measurable {
    constructor(value: Int) : this(value.toFloat())

    override fun measure(context: Context, wheelSize: Int): Float = value.dpToPx(context)
}

class Sp(private val value: Float) : Measurable {
    constructor(value: Int) : this(value.toFloat())

    override fun measure(context: Context, wheelSize: Int): Float = value.spToPx(context)
}

class Px(private val value: Float) : Measurable {
    constructor(value: Int) : this(value.toFloat())

    override fun measure(context: Context, wheelSize: Int): Float = value
}

class Fraction(private val value: Float) : Measurable {
    override fun measure(context: Context, wheelSize: Int): Float = wheelSize * value
}

interface MeasurableRect {
    fun measure(context: Context, wheelSize: Int): Rect
}

class DpSize(val width: Float, val height: Float) : MeasurableRect {
    constructor(width: Int, height: Int) : this(width.toFloat(),height.toFloat())

    override fun measure(context: Context, wheelSize: Int): Rect = Rect(
        0,
        0,
        width.roundDpToPx(context),
        height.roundDpToPx(context)
    )
}

class PxSize(val width: Float, val height: Float) : MeasurableRect {
    constructor(width: Int, height: Int) : this(width.toFloat(),height.toFloat())

    override fun measure(context: Context, wheelSize: Int): Rect = Rect(
        0,
        0,
        width.roundToInt(),
        height.roundToInt()
    )
}

class FractionSize(val width: Float, val height: Float) : MeasurableRect {
    override fun measure(context: Context, wheelSize: Int): Rect = Rect(
        0,
        0,
        (wheelSize * width).roundToInt(),
        (wheelSize * height).roundToInt()
    )
}