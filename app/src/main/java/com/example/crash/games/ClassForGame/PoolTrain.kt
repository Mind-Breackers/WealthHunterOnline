package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.example.crash.R

@SuppressLint("ClickableViewAccessibility")
class PoolTrain(parent: RelativeLayout, blockList: ArrayList<Block>, posX: Int, posY: Int,
                size: Int
) :PoolAbstract(parent,
    blockList, posX, posY, size) {

    init {
        bg.setBackgroundResource(R.drawable.pool_rl)
        bg.setOnTouchListener { v, event -> if (event.action == MotionEvent.ACTION_DOWN) {
            val id = getBlockId(event.x.toInt(), event.y.toInt())
            when (action) {
                2 -> {
                    blocks[id]?.startDestroyAnimation(blockList)
                    blocks[id] = null
                    //---------для способности туман
                    var flagNullMas=true
                    for (block in blocks){
                        if(block!=null){
                            flagNullMas=false
                            break
                        }
                    }
                    if (flagNullMas){
                        waterRerollOut.value=true
                        waterRerollIn = false
                        //---туман перед каждым рероллом ставить надо этот блок
                        if(!tumanRerollIn) {
                            rerollIndex++
                            if (rerollIndex == 5) {
                                tumanRerollOut.value = true
                                rerollIndex = 0
                            }
                        }
                        //------------------------------------------------------------
                        update()
                        action=3
                    }
                    //---------------------------
                }
                1 -> {
                    blocks[id]?.startMoveAnimation(if (id < 3) colW * 2 else colW)
                    blocks[id] = null
                    //---------для способности туман
                    var flagNullMas=true
                    for (block in blocks){
                        if(block!=null){
                            flagNullMas=false
                            break
                        }
                    }
                    if (flagNullMas){
                        waterRerollOut.value=true
                        waterRerollIn = false
                        //---туман перед каждым рероллом ставить надо этот блок
                        if(!tumanRerollIn) {
                            rerollIndex++
                            if (rerollIndex == 5) {
                                tumanRerollOut.value = true
                                rerollIndex = 0
                            }
                        }
                        //------------------------------------------------------------
                        update()
                        action=3
                    }
                    //---------------------------
                }
                0 -> {
                    if(!waterRerollIn) {
                        //---туман перед каждым рероллом ставить надо этот блок
                        if(!tumanRerollIn) {
                            rerollIndex++
                            if (rerollIndex == 5) {
                                tumanRerollOut.value = true
                                rerollIndex = 0
                            }
                        }
                        //------------------------------------------------------------
                        update()
                    }
                    else{
                        blocks[id]?.startDestroyAnimation(blockList)
                        blocks[id] = null
                        action=2
                    }
                }
            }
            actionPool.value=action
            action--
        }
            true
        }

        var params = RelativeLayout.LayoutParams(size, colW * 2)
        params.leftMargin = posX
        params.topMargin = posY
        parent.addView(bg, params)

        update()
    }


    override fun update() {
        action = 3
        for (i in blocks.indices) if (blocks[i] != null) blocks[i]?.destroy(blockList)
        for (i in 0..5) {
            val bl = Block(parent, posX, posY, cellSize)
            bl.move(colW * (i % 3) + (colW - bl.width) / 2,
                colW * (i / 3) + (colW - bl.height) / 2)
            blockList.add(bl)
            blocks[i] = bl
            bl.startCreateAnimation()
        }
    }
}