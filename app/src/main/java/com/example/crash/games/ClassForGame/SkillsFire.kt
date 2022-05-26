package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

@SuppressLint("ClickableViewAccessibility")
class SkillsFire(fire: ImageView, deleteRelative: RelativeLayout, game: GameClass,topOrBottom:Boolean,displayheight:Int,displaywidth:Int) {
    var action=true
    var blockForactive=0
    init {
        if (topOrBottom) {
            deleteRelative.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (event.x.toInt() in 0..displaywidth && event.y.toInt() in 0..game.fieldheight) {
                        for (block in game.blocks.reversed())
                            if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                                block.startDestroyAnimation(game.blocks)
                                game.enemyField.figureOutDetection(block)                      //должно быть enemy field
                                deleteRelative.visibility = View.GONE
                                break
                            }
                    }
                }
                true
            }
        }else{
            deleteRelative.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (event.x.toInt() in 0..displaywidth && event.y.toInt() in displayheight-game.fieldheight..displayheight) {
                        for (block in game.blocks.reversed())
                            if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                                block.startDestroyAnimation(game.blocks)
                                game.playField.figureOutDetection(block)                      //должно быть enemy field
                                deleteRelative.visibility = View.GONE
                                break
                            }
                    }
                }
                true
            }
        }
       fire.setColorFilter(Color.argb(80, 0, 0, 0))
        fire.setOnClickListener {
            if(blockForactive==2) {
                it.isEnabled=false
                fire.setColorFilter(Color.argb(80, 0, 0, 0))
                blockForactive=0
                deleteRelative.visibility = View.VISIBLE
            }
        }

    }
}