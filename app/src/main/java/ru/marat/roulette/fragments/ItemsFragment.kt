package ru.marat.roulette.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.marat.roulette.Item
import ru.marat.roulette.ItemDirection
import ru.marat.roulette.R

class ItemsFragment: Fragment(R.layout.fragment_items_list) {

    private var adapter: ItemsRV? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        adapter = ItemsRV({})
        recyclerView.adapter = adapter
        adapter?.list = listOf(
            Item("Android", 320, Color.WHITE, R.drawable.android),
            Item(
                "какой-то длинный текст",
                320,
                Color.rgb((0..255).random(), (0..255).random(), (0..255).random()),
                direction = ItemDirection.ALONG
            ),
            Item("Apple", 320, Color.LTGRAY, R.drawable.apple),
        )
    }
}