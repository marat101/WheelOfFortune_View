package ru.marat.roulette.wheel_of_fortune

import android.content.Context
import android.graphics.Rect
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import ru.marat.roulette.wheel_of_fortune.measurements.Fraction
import ru.marat.roulette.wheel_of_fortune.measurements.FractionSize
import ru.marat.roulette.wheel_of_fortune.measurements.Measurable
import ru.marat.roulette.wheel_of_fortune.measurements.MeasurableRect

data class Item(
    val text: String? = null,
    val weight: Long,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection = ItemDirection.ACROSS,
    val textSize: Measurable = Fraction(.03f),
    val iconSize: MeasurableRect = FractionSize(0.05f, 0.05f)
) {
    internal fun measureItem(context: Context, wheelSize: Int): MeasuredItem = MeasuredItem(
        text,
        weight,
        color,
        icon,
        direction,
        if (!text.isNullOrBlank()) textSize.measure(context, wheelSize) else null,
        if (icon != null) iconSize.measure(context, wheelSize) else null
    )
}

data class MeasuredItem(
    val text: String? = null,
    val value: Long,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection,
    val textSize: Float? = null,
    val iconRect: Rect? = null
)

enum class ItemDirection {
    ALONG, ACROSS
}