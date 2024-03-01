package ru.marat.roulette.wheel_of_fortune

import android.view.GestureDetector
import android.view.MotionEvent

class WheelScroll: GestureDetector.SimpleOnGestureListener() {
    override fun onDown(e: MotionEvent): Boolean {
        val activePointerIndex = e.actionIndex

        when(activePointerIndex){
            MotionEvent.ACTION_MOVE ->{
                
            }
        }
        return true
    }
}