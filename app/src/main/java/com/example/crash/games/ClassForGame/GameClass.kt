package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import com.example.crash.games.Game_Play.DataGames
import com.example.crash.sigInUp.Server.User

@SuppressLint("ClickableViewAccessibility")
 class GameClass(
    parentGame: RelativeLayout,
    displayheight: Int,
    displaywidth: Int,
    parentPool: RelativeLayout,
    parentField1: RelativeLayout,
    parentField2: RelativeLayout,
    baggageBtn: ImageButton,
    dataPlayMenu: DataGames,
    size: Int,
    musorka: ImageView,
    blocks: ArrayList<Block> = arrayListOf<Block>(),
    blocksPool: PoolAbstract = Pool(parentPool, blocks, 0, displayheight / 2 - displayheight / 5, displaywidth),
    musorkaEnemy: ImageView,
    baggageBtnEnemy: ImageButton,
    userbottom:User,
    userTop:User
) : AbstractPlayField(
    parentGame,
    displayheight, displaywidth, parentPool, parentField1, parentField2, baggageBtn, dataPlayMenu,
    size, musorka, blocks, blocksPool,
) {

    val winAction: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {

        enemyField = Field(
            parentField2,
            size, blocksPool.cellSize, field2Centerwidth, blocksPool.posY/2, fieldheight,
            fieldwidth
        )

        playField = Field(
            parentField1,
            size, blocksPool.cellSize, field1Centerwidth, field1Centerheight, fieldheight,
            fieldwidth
        )
        parentGame.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (block in blocks.reversed())
                        if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                            downX = event.x.toInt()
                            downY = event.y.toInt()
                            lastX = downX
                            lastY = downY
                            capturedBlock = block
                            break
                        }
                    checkBaggageBtn((baggaeBlock.size+newblockInBaggage.size),capturedBlock)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (capturedBlock != null) {
                        if (capturedBlock!!.detective) {
                            if(event.y.toInt() - capturedBlock!!.touch_y_top < blocksPool.posY-50
                                && event.y.toInt() - capturedBlock!!.touch_y_top >0) {
                                enemyField.figureOutDetection(capturedBlock!!)
                            }else{
                                playField.figureOutDetection(capturedBlock!!)
                            }
                        }

                        if (event.x.toInt() - capturedBlock!!.touch_x_left > 0
                            && event.x.toInt() - capturedBlock!!.touch_x_right < displaywidth
                        ) {
                            if ((event.y.toInt() - capturedBlock!!.touch_y_top > displayheight - fieldheight
                                && event.y.toInt() - capturedBlock!!.touch_y_bottom < displayheight)
                                || (event.y.toInt() - capturedBlock!!.touch_y_top < blocksPool.posY
                                && event.y.toInt() - capturedBlock!!.touch_y_top >0)
                            ) {
                                capturedBlock!!.move(
                                    event.x.toInt() - lastX,
                                    event.y.toInt() - lastY
                                )
                                lastX = event.x.toInt()
                                lastY = event.y.toInt()
                            } else {
                                capturedBlock!!.move(
                                    event.x.toInt() - lastX,
                                    0
                                )
                                lastX = event.x.toInt()
                            }
                        } else if (event.y.toInt() - capturedBlock!!.touch_y_top > displayheight - fieldheight
                            && event.y.toInt() - capturedBlock!!.touch_y_bottom < displayheight
                        ) {

                            capturedBlock!!.move(
                                0,
                                event.y.toInt() - lastY
                            )
                            lastY = event.y.toInt()
                        }

                    }
                    checkBaggageBtn((baggaeBlock.size+newblockInBaggage.size),capturedBlock)
                }
                MotionEvent.ACTION_UP -> {
                    if (capturedBlock != null) {
                        if (event.x.toInt() == downX && event.y.toInt() == downY) {
                            if(event.y.toInt() - capturedBlock!!.touch_y_top < blocksPool.posY-50
                                && event.y.toInt() - capturedBlock!!.touch_y_top >0) {
                                enemyField .figureOutDetection(capturedBlock!!)
                                capturedBlock?.rotate(enemyField )
                            }else{
                                playField.figureOutDetection(capturedBlock!!)
                                capturedBlock?.rotate(playField)
                            }
                        } else {
                            //Рюкзак---------------------------------------------------------------
                            var delta=if(capturedBlock!!.width/3> capturedBlock!!.height/3)capturedBlock!!.width/3 else capturedBlock!!.height/3
                            if (capturedBlock!!.cx in baggageBtn.marginLeft-delta..baggageBtn.marginLeft+baggageBtn.width
                                && capturedBlock!!.cy in baggageBtn.marginTop-blocksPool.cellSize..baggageBtn.marginTop+baggageBtn.height
                            ) {
                                if(baggaeBlock.size+newblockInBaggage.size<6) {
                                    newblockInBaggage.add(capturedBlock!!)
                                    capturedBlock?.destroy(blocks)
                                    capturedBlock=null
                                }

                            } else {
                                if (capturedBlock!!.cx in baggageBtnEnemy.marginLeft-delta..baggageBtnEnemy.marginLeft+baggageBtnEnemy.width
                                    && capturedBlock!!.cy in baggageBtnEnemy.marginTop..baggageBtnEnemy.marginBottom+baggageBtnEnemy.height)
                                {
                                    if(baggaeBlockEnemy.size+newblockInBaggageEnemy.size<6) {
                                        newblockInBaggageEnemy.add(capturedBlock!!)
                                        capturedBlock?.destroy(blocks)
                                        capturedBlock=null
                                    }
                                } else {
                                    //МУСОРКА---------------------------------------------------------------
                                    if (capturedBlock!!.cx in 0..musorka.marginLeft + musorka.width
                                        && capturedBlock!!.cy in musorka.marginTop - blocksPool.cellSize..musorka.marginTop + musorka.height
                                    ) {
                                        capturedBlock?.destroy(blocks)
                                        capturedBlock = null
                                    } else {
                                        if (capturedBlock!!.cx in 0..musorkaEnemy.marginLeft + musorkaEnemy.width
                                            && capturedBlock!!.cy in musorkaEnemy.marginTop - blocksPool.cellSize..musorkaEnemy.marginTop + musorkaEnemy.height
                                        ) {
                                            capturedBlock?.destroy(blocks)
                                            capturedBlock = null
                                        } else {
                                            if (event.y.toInt() - capturedBlock!!.touch_y_top < blocksPool.posY - 50
                                                && event.y.toInt() - capturedBlock!!.touch_y_top > 0
                                            ) {
                                                capturedBlock!!.detective =
                                                    enemyField.figureDetection(capturedBlock!!)
                                            } else {
                                                capturedBlock!!.detective =
                                                    playField.figureDetection(capturedBlock!!)
                                            }
                                            if (playField.checkWin()) {
                                                userbottom.rating+=10
                                                winAction.value=true
                                            }
                                            if (enemyField.checkWin()) {
                                                userTop.rating+=10
                                                winAction.value=false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    capturedBlock = null
                    checkBaggageBtn((baggaeBlock.size+newblockInBaggage.size),null)
                }
            }
            true
        }
    }

}