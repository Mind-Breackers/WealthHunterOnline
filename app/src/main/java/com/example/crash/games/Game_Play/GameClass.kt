package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.crash.games.ClassForGame.Block
import com.example.crash.games.ClassForGame.Field

@SuppressLint("ClickableViewAccessibility")
 class GameClass(
    parentGame: RelativeLayout, displayheight: Int, displaywidth: Int,
    parentPool: RelativeLayout, parentField1: RelativeLayout,
    parentField2: RelativeLayout, baggageBtn: ImageButton, dataPlayMenu: DataGames, size: Int,
    musorka: ImageView,tree:ImageView
) : AbstractPlayField(
    parentGame,
    displayheight, displaywidth, parentPool, parentField1, parentField2, baggageBtn, dataPlayMenu,
    size, musorka
) {


    init {

        enemyField = Field(
            parentField2,
            size, blocksPool.cellSize, field2Centerwidth, blocksPool.posY/2, fieldheight,
            fieldwidth
        )

        tree.setOnClickListener {
            val bl = Block(parentGame, field1Centerwidth, field1Centerheight, blocksPool.cellSize,block1x1 = true)
            blocks.add(bl)
        }

    }

}