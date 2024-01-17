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
            Item(name = "aaa", 22, Color.RED),
            Item(name = "aaa", 22, Color.GRAY),
            Item(name = "aaa", 22, Color.GREEN),
            Item(name = "aaa", 22, Color.MAGENTA),
            Item(name = "aaa", 22, Color.BLUE),
            Item(name = "aaa", 22, Color.YELLOW)
        )
    }
}