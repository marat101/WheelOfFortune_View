package ru.marat.roulette.fragments.other

import kotlinx.coroutines.flow.MutableStateFlow
import ru.marat.roulette.wheel_of_fortune.Item

object ItemsList {
    var flow = MutableStateFlow<List<Item>>(listOf())
}