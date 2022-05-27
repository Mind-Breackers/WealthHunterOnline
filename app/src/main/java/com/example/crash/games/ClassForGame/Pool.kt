package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.graphics.Color
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
    var bannerTop: View = View(parent.context)
    var bannerBot: View = View(parent.context)

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
                    if (action == -1) action = 2
                    firstMove = true
                } else firstMove = false

                if (action == 0) {
                    bannerTop.setBackgroundColor(
                        Color.argb(
                        if (iceTarget > 0) 255 else 0, 86,49,32))
                    bannerBot.setBackgroundColor(Color.argb(0, 86,49,32))
                } else {
                    bannerTop.setBackgroundColor(
                        Color.argb(
                        if (moveOfBottom == firstMove) 255 else 0,86,49,32))
                    bannerBot.setBackgroundColor(
                        Color.argb(
                        if (!moveOfBottom == firstMove) 255 else 0,86,49,32))
                }
            }
            true
        }

        var params = RelativeLayout.LayoutParams(size, colW * 2)
        params.leftMargin = posX
        params.topMargin = posY
        parent.addView(bg, params)

        bannerTop.setBackgroundColor(Color.argb(255, 86,49,32))

        params = RelativeLayout.LayoutParams(size, colW - 2)
        params.leftMargin = posX
        params.topMargin = posY
        parent.addView(bannerTop, params)
        params = RelativeLayout.LayoutParams(size, colW)
        params.leftMargin = posX
        params.topMargin = posY + colW
        parent.addView(bannerBot, params)


        update()
    }
}