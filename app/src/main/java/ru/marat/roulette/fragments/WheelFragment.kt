package ru.marat.roulette.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.marat.roulette.R
import ru.marat.roulette.wheel_of_fortune.WheelOfFortuneView
import ru.marat.roulette.fragments.other.ItemsList

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

        //        wheelOfFortune.items = listOf(
//            Item(name = "красный", 2, Color.RED),
////            Item(name = "серый", 110, Color.GRAY),
//            Item(name = "хз", 130, Color.rgb(123, 1, 77)),
////            Item(name = "zelenskiy", 550, Color.GREEN),
////            Item(name = "хз", 190, Color.rgb(13, 91, 77)),
//            Item(name = "розовый", 120, Color.MAGENTA),
////            Item(name = "хз", 100, Color.rgb(190, 191, 43)),
////            Item(name = "синий", 430, Color.BLUE),
////            Item(name = "", 20, Color.YELLOW),
////            Item(name = "aaa", 200, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
////            Item(name = "aaa", 320, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
////            Item(name = "aaa", 290, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
////            Item(name = "aaa", (0..255).random(), Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//            Item(name = "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", 77, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//        )

//        wheelOfFortune.items = listOf(
//            Item("Android", 320, Color.WHITE, R.drawable.android),
//            Item(
//                "какой-то длинный текст",
//                320,
//                Color.rgb((0..255).random(), (0..255).random(), (0..255).random()),
//                direction = ItemDirection.ALONG
//            ),
//            Item("Apple", 320, Color.LTGRAY, R.drawable.apple),
//        )

        spinBtn.setOnClickListener {
            wheelOfFortune.run {
                if (!animation.isRunning) {
                    springForce.finalPosition = animatedValue.value + (540..(360 * 15)).random()
                    animation.start()
                } else
                    animation.skipToEnd()
            }
        }
    }
}