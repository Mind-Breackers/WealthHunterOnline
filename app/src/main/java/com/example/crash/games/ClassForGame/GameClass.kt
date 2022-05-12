package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.crash.games.Game_Play.DataGames

@SuppressLint("ClickableViewAccessibility")
 class GameClass(
    parentGame: RelativeLayout, displayheight: Int, displaywidth: Int,
    parentPool: RelativeLayout, parentField1: RelativeLayout,
    parentField2: RelativeLayout, baggageBtn: ImageButton, dataPlayMenu: DataGames, size: Int,
    musorka: ImageView, blocks: ArrayList<Block> = arrayListOf<Block>(), blocksPool: PoolAbstract = PoolTrain(parentPool, blocks, 0, displayheight / 2 - displayheight / 5, displaywidth),
) : AbstractPlayField(
    parentGame,
    displayheight, displaywidth, parentPool, parentField1, parentField2, baggageBtn, dataPlayMenu,
    size, musorka, blocks, blocksPool,
) {


    init {

        enemyField = Field(
            parentField2,
            size, blocksPool.cellSize, field2Centerwidth, blocksPool.posY/2, fieldheight,
            fieldwidth
        )
    }

}