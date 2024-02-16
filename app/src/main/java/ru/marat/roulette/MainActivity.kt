package ru.marat.roulette

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.marat.roulette.fragments.WheelFragment
import ru.marat.roulette.fragments.other.ItemsList
import ru.marat.roulette.wheel_of_fortune.WheelItem
import ru.marat.roulette.wheel_of_fortune.ItemDirection
import ru.marat.roulette.wheel_of_fortune.measurements.asDp
import ru.marat.roulette.wheel_of_fortune.measurements.asFraction
import ru.marat.roulette.wheel_of_fortune.measurements.asPx
import ru.marat.roulette.wheel_of_fortune.measurements.asSp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, WheelFragment::class.java, null)
            .commit()

        lifecycleScope.launch {
            ItemsList.flow.emit(
                listOf(
                    WheelItem("Androidddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
                        3200, Color.WHITE, R.drawable.android,
                        textSize = 0.02f.asFraction()),
                    WheelItem(
                        "какой-то длинный текст",
                        320,
//                        Color.rgb((0..255).random(), (0..255).random(), (0..255).random()),
                        Color.DKGRAY,
                        direction = ItemDirection.ALONG
                    ),
                    WheelItem("Apple", 320, Color.LTGRAY, R.drawable.apple),
                )
            )
        }
    }
}