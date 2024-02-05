package ru.marat.roulette

import androidx.annotation.ColorInt

data class Item(
    val name: String,
    val value: Long,
    @ColorInt val color: Int
) {
//    constructor(name: String, value: Int, @ColorInt color: Int) : this(
//        name,
//        value.toLong(),
//        color
//    )
}