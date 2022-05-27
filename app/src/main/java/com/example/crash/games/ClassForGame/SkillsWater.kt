package com.example.crash.games.ClassForGame

import android.graphics.Color
import android.widget.ImageView

class SkillsWater(water:ImageView,game:AbstractPlayField) {
    var action=0
    var blockForactive=0

    init {
        water.setColorFilter(Color.argb(80, 0, 0, 0))
        water.setOnClickListener {
            if(blockForactive==2) {
                blockForactive=0
                it.isEnabled=false
                water.setColorFilter(Color.argb(80, 0, 0, 0))
                game.blocksPool.iceTarget = 3
            }
        }
    }
}