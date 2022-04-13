package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

@SuppressLint("ClickableViewAccessibility")
class SkillsFire(fire: ImageView, deleteRelative: RelativeLayout, game: GameClass) {
    var action=true

    init {
        deleteRelative.setOnTouchListener { view, event ->
            if(event.action==MotionEvent.ACTION_DOWN){
                for (block in game.blocks.reversed())
                    if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                        block.startDestroyAnimation(game.blocks)
                        game.playField.figureOutDetection(block)                      //должно быть enemy field
                        deleteRelative.visibility=View.GONE
                        break
                    }
            }
            true
        }

        fire.setOnClickListener {
            deleteRelative.visibility=View.VISIBLE
        }

    }
}