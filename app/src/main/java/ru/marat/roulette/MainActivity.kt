package ru.marat.roulette

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val wheelOfFortune = findViewById<WheelOfFortuneView>(R.id.wheel)
        val addBtn = findViewById<Button>(R.id.add)
        val randomColorBtn = findViewById<Button>(R.id.random)
        val nameText = findViewById<EditText>(R.id.name)
        val valueText = findViewById<EditText>(R.id.value)
        val red = findViewById<EditText>(R.id.red)
        val green = findViewById<EditText>(R.id.green)
        val blue = findViewById<EditText>(R.id.blue)

//        wheelOfFortune.items = listOf(
//            Item(name = "красный", 2, Color.RED),
//            Item(name = "серый", 110, Color.GRAY),
//            Item(name = "хз", 130, Color.rgb(123, 1, 77)),
//            Item(name = "zelenskiy", 550, Color.GREEN),
//            Item(name = "хз", 190, Color.rgb(13, 91, 77)),
//            Item(name = "розовый", 220, Color.MAGENTA),
//            Item(name = "хз", 100, Color.rgb(190, 191, 43)),
//            Item(name = "синий", 430, Color.BLUE),
//            Item(name = "", 20, Color.YELLOW),
//            Item(name = "aaa", 200, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//            Item(name = "aaa", 320, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//            Item(name = "aaa", 290, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//            Item(name = "aaa", (0..255).random(), Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//            Item(name = "aaa", 77, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
//        )


        wheelOfFortune.setOnClickListener {
            wheelOfFortune.run {
                if (!animation.isRunning) {
                    springForce.finalPosition = animatedValue.value + (540..(360 * 15)).random()
                    animation.start()
                } else
                    animation.skipToEnd()
            }
        }

        randomColorBtn.setOnClickListener {
            val r = (0..255).random()
            val g = (0..255).random()
            val b = (0..255).random()
            randomColorBtn.setBackgroundColor(Color.rgb(r.toFloat(), g.toFloat(), b.toFloat()))
            red.text = Editable.Factory.getInstance().newEditable(r.toString())
            green.text = Editable.Factory.getInstance().newEditable(g.toString())
            blue.text = Editable.Factory.getInstance().newEditable(b.toString())
        }
        addBtn.setOnClickListener {
            wheelOfFortune.items += Item(
                nameText.text.toString(),
                valueText.text.toString().toLong(),
                Color.rgb(
                    red.text.toString().toFloat(),
                    green.text.toString().toFloat(),
                    blue.text.toString().toFloat()
                )
            )
        }
    }
}