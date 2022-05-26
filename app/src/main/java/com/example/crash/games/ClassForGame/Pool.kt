package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import com.example.crash.R

@SuppressLint("ClickableViewAccessibility")
class Pool(parent: RelativeLayout, blockList: ArrayList<Block>, posX: Int, posY: Int, size: Int) :
    PoolAbstract(
        parent, blockList,
        posX,
        posY,
        size
    ) {

var tumanCount=0
var tumanCountenemy=0

    init{
        bg.setBackgroundResource(R.drawable.pool_rl)
        bg.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val id = getBlockId(event.x.toInt(), event.y.toInt())
                when (action) {
                    2 -> {
                        blocks[id]?.startDestroyAnimation(blockList)
                        blocks[id] = null
                    }
                    1 -> {
                        if (moveOfBottom == firstMove)
                            blocks[id]?.startMoveAnimation(if (id < 3) colW * 2 else colW)
                        else blocks[id]?.startMoveAnimation(if (id < 3) -colW else -colW * 2)
                        blocks[id] = null
                    }
                    0 -> {
                        update()
                        if(!tumanRerollIn){
                            tumanCount++
                            if(tumanCount==4) {
                                tumanCount=0
                                tumanRerollOut.value = true
                            }
                        }
                        if(!tumanRerollInenemy){
                            tumanCountenemy++
                            if(tumanCountenemy==4) {
                                tumanCountenemy=0
                                tumanRerollOutenemy.value = true
                            }
                        }
                        firstMove = false
                    }
                }
                if (!firstMove) {
                    actionPool.value = action
                    action--
                    firstMove = true
                } else firstMove = false

            }
            true
        }

        var params = RelativeLayout.LayoutParams(size, colW * 2)
        params.leftMargin = posX
        params.topMargin = posY
        parent.addView(bg, params)

        update()
    }
}