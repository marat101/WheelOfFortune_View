package ru.marat.roulette

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.marat.roulette.fragments.WheelFragment
import ru.marat.roulette.fragments.other.ItemsList

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
                    Item("Android", 320, Color.WHITE, R.drawable.android),
                    Item(
                        "какой-то длинный текст",
                        320,
                        Color.rgb((0..255).random(), (0..255).random(), (0..255).random()),
                        direction = ItemDirection.ALONG
                    ),
                    Item("Apple", 320, Color.LTGRAY, R.drawable.apple),
                )
            )
        }
//        randomColorBtn.setOnClickListener {
//            val r = (0..255).random()
//            val g = (0..255).random()
//            val b = (0..255).random()
//            randomColorBtn.setBackgroundColor(Color.rgb(r, g, b))
//            red.text = Editable.Factory.getInstance().newEditable(r.toString())
//            green.text = Editable.Factory.getInstance().newEditable(g.toString())
//            blue.text = Editable.Factory.getInstance().newEditable(b.toString())
//        }
//        addBtn.setOnClickListener {
//            wheelOfFortune.items += Item(
//                nameText.text.toString(),
//                valueText.text.toString().toLong(),
//                Color.rgb(
//                    red.text.toString().toInt(),
//                    green.text.toString().toInt(),
//                    blue.text.toString().toInt()
//                )
//            )
//        }
    }
}