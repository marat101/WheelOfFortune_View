package ru.marat.roulette.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.marat.roulette.wheel_of_fortune.WheelItem
import ru.marat.roulette.R
import ru.marat.roulette.fragments.other.ItemsList
import ru.marat.roulette.fragments.recycler_view.ItemsRV

class ItemsFragment : Fragment(R.layout.fragment_items_list) {

    private var adapter: ItemsRV? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val addBtn = view.findViewById<Button>(R.id.addBtn)
        adapter = ItemsRV(
            onEditClick = { pos ->
                val dialog = EditDialogFragment()
                dialog.arguments = bundleOf("pos" to pos)
                dialog.show(parentFragmentManager,"dddllglglsdfsdf")
            },
            onDeleteClick = ::onDelete
        )
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            ItemsList.flow.collect {
                adapter?.list = it
            }
        }
        addBtn.setOnClickListener {
            val dialog = EditDialogFragment()
            dialog.show(parentFragmentManager,"dddllglglsdfsdf")
        }
    }

    fun onDelete(item: WheelItem) {
        lifecycleScope.launch {
            ItemsList.flow.emit(ItemsList.flow.value.filter { it != item })
        }
    }
}