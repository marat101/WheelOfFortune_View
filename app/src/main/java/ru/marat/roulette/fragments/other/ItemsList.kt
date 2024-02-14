package ru.marat.roulette.fragments.other

import kotlinx.coroutines.flow.MutableStateFlow
import ru.marat.roulette.Item

object ItemsList {
    var flow = MutableStateFlow<List<Item>>(listOf())
}