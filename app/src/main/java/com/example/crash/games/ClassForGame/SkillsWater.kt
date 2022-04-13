package com.example.crash.games.ClassForGame

import android.widget.ImageView

class SkillsWater(water:ImageView,game:AbstractPlayField) {
    var action=0


    init {
        water.setOnClickListener {
            when(action){
                0->{
                    action++
                    game.blocksPool.waterRerollIn=true
                }

                1->{
                    action=0
                    game.blocksPool.waterRerollIn=false
                }
            }
        }
    }
}