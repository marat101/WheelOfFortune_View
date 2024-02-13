package ru.marat.roulette

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class Item(
    val name: String,
    val value: Long,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection = ItemDirection.ACROSS
) {
}
enum class ItemDirection{
    ALONG, ACROSS
}