package ru.marat.roulette

import androidx.annotation.ColorInt
import androidx.annotation.ColorLong
import androidx.annotation.ColorRes

data class Item(
    val name: String,
    val value: Double,
    @ColorInt val color: Int
) {
    constructor(name: String, value: Float, @ColorInt color: Int) : this(
        name,
        value.toDouble(),
        color
    )
    constructor(name: String, value: Int, @ColorInt color: Int) : this(
        name,
        value.toDouble(),
        color
    )
    constructor(name: String, value: Long, @ColorInt color: Int) : this(
        name,
        value.toDouble(),
        color
    )
}