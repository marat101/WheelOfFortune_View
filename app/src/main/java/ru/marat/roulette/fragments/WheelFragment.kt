package ru.marat.roulette.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.marat.roulette.R
import ru.marat.roulette.fragments.other.ItemsList
import ru.marat.roulette.wheel_of_fortune.WheelOfFortuneView

class WheelFragment : Fragment(R.layout.fragment_wheel) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wheelOfFortune = view.findViewById<WheelOfFortuneView>(R.id.wheel)
        val spinBtn = view.findViewById<Button>(R.id.spin_btn)
        val itemsBtn = view.findViewById<Button>(R.id.items_btn)
        itemsBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack("whl")
                .add(R.id.main_container, ItemsFragment::class.java, null)
                .hide(this)
                .commit()
        }
        lifecycleScope.launch {
            ItemsList.flow.collect {
                wheelOfFortune.items = it
            }
        }
        spinBtn.setOnClickListener {
            wheelOfFortune.animTest()
        }
    }
}