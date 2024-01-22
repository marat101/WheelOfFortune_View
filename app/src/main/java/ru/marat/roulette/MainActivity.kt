package ru.marat.roulette

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<WheelOfFortuneView>(R.id.wheel)
        view.items = listOf(
            Item(name = "aaa", 2, Color.RED),
            Item(name = "aaa", 110, Color.GRAY),
            Item(name = "aaa", 130, Color.rgb(123, 1, 77)),
            Item(name = "aaa", 550, Color.GREEN),
            Item(name = "aaa", 190, Color.rgb(13, 91, 77)),
            Item(name = "aaa", 220, Color.MAGENTA),
            Item(name = "aaa", 100, Color.rgb(190, 191, 43)),
            Item(name = "aaa", 430, Color.BLUE),
            Item(name = "aaa", 20, Color.YELLOW),
            Item(name = "aaa", 200, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
            Item(name = "aaa", 320, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
            Item(name = "aaa", 290, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
            Item(name = "aaa", (0..255).random(), Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
            Item(name = "aaa", 77, Color.rgb((0..255).random(),(0..255).random(),(0..255).random())),
        )
        view.setOnClickListener {
            if (!view.animation.isRunning)
                view.animation.start()
            else
                view.animation.skipToEnd()
        }
    }
}