package ru.marat.roulette

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<WheelOfFortune>(R.id.wheel)
        view.items = listOf(
            Item(name = "aaa", 1, Color.RED),
            Item(name = "aaa", 11, Color.GRAY),
            Item(name = "aaa", 13, Color.rgb(123,1,77)),
            Item(name = "aaa", 66, Color.GREEN),
            Item(name = "aaa", 22, Color.MAGENTA),
            Item(name = "aaa", 43, Color.BLUE),
            Item(name = "aaa", 2, Color.YELLOW)
        )
        view.setOnClickListener {
            if (!view.animation.isRunning)
                view.animation.start()
        }
    }
}