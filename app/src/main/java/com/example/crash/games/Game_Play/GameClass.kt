package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.crash.R
import com.example.crash.basic_menu.DataPlayMenu
import com.example.crash.databinding.Games2Binding
import com.example.crash.games.Block
import com.example.crash.games.Field
import com.example.crash.games.Pool

@SuppressLint("ClickableViewAccessibility")
class GameClass(
    private val parentGame: RelativeLayout,
    displayheight: Int,
    displaywidth: Int,
    parentPool:RelativeLayout,
    parentField1: RelativeLayout,
    parentField2: RelativeLayout,
    private val baggageBtn: ImageButton,
    dataPlayMenu: DataGames,
    size:Int=30
) {
    val baggaeBlock= arrayListOf<Block>()
    val blocks= arrayListOf<Block>()
    val newblockInBaggage=arrayListOf<Block>()
    private val blocksPool:Pool=Pool(parentPool, blocks, 0, displayheight / 2 - displayheight / 5, displaywidth)
    private val fieldheight = (displayheight - blocksPool.height) / 2 + 50
    private val fieldwidth = displaywidth - (displaywidth / 6) * 2
    private val field1Centerheight = displayheight / 2 + displayheight / 3
    private val field1Centerwidth = displaywidth / 2
    private val field2Centerheight = displaywidth / 4
    private val field2Centerwidth = displaywidth / 2
    private var downX = 0
    private var downY = 0
    private var lastX = 0
    private val playField: Field = Field(
        parentField1,
        size, blocksPool.cellSize, field1Centerwidth, field1Centerheight, fieldheight,
        fieldwidth
    )
    private val enemyField:Field=Field(
        parentField2,
        size, blocksPool.cellSize, field2Centerwidth, field2Centerheight, fieldheight,
        fieldwidth
    )
    private var capturedBlock: Block? = null
    private var lastY = 0


    init {



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
                            playField.figureOutDetection(capturedBlock!!)
                        }

                        if (event.x.toInt() - capturedBlock!!.touch_x_left > 0
                            && event.x.toInt() - capturedBlock!!.touch_x_right < displaywidth
                        ) {
                            if (event.y.toInt() - capturedBlock!!.touch_y_top > displayheight - fieldheight
                                && event.y.toInt() - capturedBlock!!.touch_y_bottom < displayheight
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
                        playField.figureOutDetection(capturedBlock!!)
                        capturedBlock?.rotate(playField)
                    } else {
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
                            capturedBlock!!.detective = playField.figureDetection(capturedBlock!!)
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

    private fun checkBaggageBtn(baggageSize:Int,capturedBlock:Block?){
        if(baggageSize>0){
            if(capturedBlock==null){
                baggageBtn.setImageResource(R.drawable.ic_zakryty_sunduk)
            }else{
                baggageBtn.setImageResource(R.drawable.ic_polny_ryukzak)
            }
        }
        else{
            if(capturedBlock==null){
                baggageBtn.setImageResource(R.drawable.ic_zakryty_sunduk)
            }else{
                baggageBtn.setImageResource(R.drawable.ic_otkryty_sunduk)
            }
        }
    }

}