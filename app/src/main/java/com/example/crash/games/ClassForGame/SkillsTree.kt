package com.example.crash.games.ClassForGame

import android.widget.ImageView
import android.widget.RelativeLayout

class SkillsTree(parentGame:RelativeLayout, tree: ImageView, game:AbstractPlayField) {

    init {
        tree.setOnClickListener {
            val bl = Block(parentGame, game.field1Centerwidth, game.field1Centerheight, game.blocksPool.cellSize,block1x1 = true)
            game.blocks.add(bl)
        }
    }
}