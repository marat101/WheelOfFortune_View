package ru.marat.roulette.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.marat.roulette.Item
import ru.marat.roulette.R
import ru.marat.roulette.fragments.other.ItemsList

class EditDialogFragment : DialogFragment(R.layout.layout_item_creation) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt("pos")
        val addBtn = view.findViewById<Button>(R.id.add)
        val randomColorBtn = view.findViewById<Button>(R.id.random)
        val nameText = view.findViewById<EditText>(R.id.name)
        val valueText = view.findViewById<EditText>(R.id.value)
        val red = view.findViewById<EditText>(R.id.red)
        val green = view.findViewById<EditText>(R.id.green)
        val blue = view.findViewById<EditText>(R.id.blue)

        position?.let { position ->
            val item = ItemsList.flow.value[position]
            nameText.setText(item.name)
            valueText.setText(item.value.toString())
            red.setText(Color.red(item.color).toString())
            green.setText(Color.green(item.color).toString())
            blue.setText(Color.blue(item.color).toString())
            randomColorBtn.setBackgroundColor(item.color)
        }
        randomColorBtn.setOnClickListener {
            val r = (0..255).random()
            val g = (0..255).random()
            val b = (0..255).random()
            randomColorBtn.setBackgroundColor(Color.rgb(r, g, b))
            red.text = Editable.Factory.getInstance().newEditable(r.toString())
            green.text = Editable.Factory.getInstance().newEditable(g.toString())
            blue.text = Editable.Factory.getInstance().newEditable(b.toString())
        }
        addBtn.setOnClickListener {
            val items = ItemsList.flow.value.toMutableList()

            if (position != null) {
                val item = ItemsList.flow.value[position]
                items.removeAt(position)
                items.add(
                    position, item.copy(
                        name = nameText.text.toString(),
                        value = valueText.text.toString().toLong(),
                        color = Color.rgb(
                            red.text.toString().toInt(),
                            green.text.toString().toInt(),
                            blue.text.toString().toInt()
                        )
                    )
                )
            } else {
                items.add(
                    Item(
                        name = nameText.text.toString(),
                        value = valueText.text.toString().toLong(),
                        color = Color.rgb(
                            red.text.toString().toInt(),
                            green.text.toString().toInt(),
                            blue.text.toString().toInt()
                        )
                    )
                )
            }

            lifecycleScope.launch {
                ItemsList.flow.emit(items)
            }
        }
    }
}