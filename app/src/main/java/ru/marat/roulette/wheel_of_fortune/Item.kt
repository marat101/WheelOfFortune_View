package ru.marat.roulette.wheel_of_fortune

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import ru.marat.roulette.wheel_of_fortune.measurements.Fraction
import ru.marat.roulette.wheel_of_fortune.measurements.Measurable

data class Item(
    val text: String? = null,
    val value: Long,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection = ItemDirection.ACROSS,
    val textSize: Measurable = Fraction(.03f)
) {
    fun WheelOfFortuneView.measureItem(wheelSize: Int): MeasuredItem = MeasuredItem(
        this@Item.text,
        this@Item.value,
        this@Item.color,
        this@Item.icon,
        this@Item.direction,
        this@Item.textSize.measure(context, wheelSize)
    )
}

data class MeasuredItem(
    val text: String? = null,
    val value: Long,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection,
    val textSize: Float
)

enum class ItemDirection {
    ALONG, ACROSS
}