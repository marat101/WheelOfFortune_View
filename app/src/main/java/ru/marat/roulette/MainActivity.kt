package ru.marat.roulette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.marat.roulette.fragments.WheelFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, WheelFragment::class.java, null)
            .commit()

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