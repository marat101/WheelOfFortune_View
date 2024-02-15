package ru.marat.roulette.wheel_of_fortune.measurements

import android.content.Context
import ru.marat.roulette.wheel_of_fortune.dpToPx
import ru.marat.roulette.wheel_of_fortune.spToPx

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