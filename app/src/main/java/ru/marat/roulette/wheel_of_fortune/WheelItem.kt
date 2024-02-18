package ru.marat.roulette.wheel_of_fortune

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import ru.marat.roulette.R
import ru.marat.roulette.wheel_of_fortune.measurements.Fraction
import ru.marat.roulette.wheel_of_fortune.measurements.FractionSize
import ru.marat.roulette.wheel_of_fortune.measurements.Measurable
import ru.marat.roulette.wheel_of_fortune.measurements.MeasurableRect

data class WheelItem(
    val text: String? = null,
    val weight: Int,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection = ItemDirection.ACROSS,
    val textSize: Measurable = Fraction(.03f),
    @ColorInt val textColor: Int = getTextColor(color),
    val iconSize: MeasurableRect = FractionSize(0.05f, 0.05f),
    val edgePadding: Measurable = Fraction(0.05f),
    val centerPadding: Measurable = Fraction(0.1f),
    val spacing: Measurable? = null
) {
    internal fun measureItem(
        context: Context,
        wheelSize: Int,
        startAngle: Float,
        sweepAngle: Float
    ): MeasuredItem = MeasuredItem(
        text,
        weight,
        color,
        icon,
        direction,
        if (!text.isNullOrBlank()) textSize.measure(context, wheelSize) else null,
        textColor,
        if (icon != null) iconSize.measure(context, wheelSize) else null,
        edgePadding.measure(context, wheelSize),
        centerPadding.measure(context, wheelSize),
        spacing?.measure(context, wheelSize) ?: 0f,
        startAngle,
        sweepAngle
    )
}

data class MeasuredItem internal constructor(
    val text: String? = null,
    val value: Int,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null,
    val direction: ItemDirection,
    val textSize: Float? = null,
    @ColorInt val textColor: Int,
    val iconRect: Rect? = null,
    val edgePadding: Float,
    val centerPadding: Float,
    val spacing: Float,
    val startAngle: Float,
    val sweepAngle: Float,
    val endAngle: Float = startAngle + sweepAngle
)

enum class ItemDirection {
    ALONG, ACROSS
}