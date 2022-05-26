package com.example.crash.games.ClassForGame

import android.graphics.Color
import android.widget.ImageView
import android.widget.RelativeLayout

class SkillsTree(parentGame:RelativeLayout, tree: ImageView, game:AbstractPlayField,fieldCenterheight:Int) {
    var blockForactive=0
    init {
        tree.setColorFilter(Color.argb(80, 0, 0, 0))
        tree.setOnClickListener {
            if(blockForactive==2) {
                it.isEnabled=false
                tree.setColorFilter(Color.argb(80, 0, 0, 0))
                blockForactive=0
                val bl = Block(
                    parentGame,
                    game.field1Centerwidth,
                    fieldCenterheight,
                    game.blocksPool.cellSize,
                    block1x1 = true
                )
                game.blocks.add(bl)
            }
        }
    }
}