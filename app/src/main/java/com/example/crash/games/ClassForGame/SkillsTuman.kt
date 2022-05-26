package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.DrawableCompat
import com.example.crash.R

//переделать как будет связь с сервером
@SuppressLint("ResourceAsColor")
class SkillsTuman(tuman:ImageView, game: GameClass, field: Field, enemyOrplay :Boolean) {
    var flagSkiils=true
    var blockForactive=0

    init {
        tuman.setColorFilter(Color.argb(80, 0, 0, 0))
        tuman.setOnClickListener {
            if(blockForactive==2) {
                it.isEnabled=false
                tuman.setColorFilter(Color.argb(80, 0, 0, 0))
                blockForactive=0
                if (!enemyOrplay) {
                    game.blocksPool.tumanRerollIn = false
                } else {
                    game.blocksPool.tumanRerollInenemy = false
                }
                if (flagSkiils) {
                    field.visible(false)
                    flagSkiils = false
                }
            }
        }
    }
}