package ru.marat.roulette

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class Item <K>(
    val key: K,
    val name: String,
    val value: Long,
    @ColorInt val color: Int,
    @DrawableRes val icon: Int? = null

) {

}