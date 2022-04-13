package com.example.crash.games.ClassForGame

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

//переделать как будет связь с сервером
class SkillsTuman(tuman:ImageView,game: GameClass,tumanField:RelativeLayout) {
    var flagSkiils=true

    init {

        tuman.setOnClickListener {
            game.blocksPool.tumanRerollIn=false
            if(flagSkiils) {
                tumanField.visibility = View.VISIBLE
                flagSkiils=false
            }
        }
    }
}