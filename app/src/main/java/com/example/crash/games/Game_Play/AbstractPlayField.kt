package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import com.example.crash.R
import com.example.crash.games.ClassForGame.Block
import com.example.crash.games.ClassForGame.Field
import com.example.crash.games.ClassForGame.Pool

@SuppressLint("ClickableViewAccessibility")
abstract class AbstractPlayField(
    private val parentGame: RelativeLayout,
    displayheight: Int,
    displaywidth: Int,
    parentPool:RelativeLayout,
    parentField1: RelativeLayout,
    parentField2: RelativeLayout,
    private val baggageBtn: ImageButton,
    dataPlayMenu: DataGames,
    size:Int,
    private val musorka: ImageView
) {
    val baggaeBlock= arrayListOf<Block>()
    val blocks= arrayListOf<Block>()
    val newblockInBaggage=arrayListOf<Block>()
    val blocksPool: Pool = Pool(parentPool, blocks, 0, displayheight / 2 - displayheight / 5, displaywidth)
    val cellsSize=blocksPool.cellSize
    val fieldheight = (displayheight - blocksPool.height) / 2 + 50
    val fieldwidth = displaywidth - (displaywidth / 6) * 2
    protected val field1Centerheight = displayheight / 2 + displayheight / 3
    protected val field1Centerwidth = displaywidth / 2
    protected val field2Centerheight = displayheight / 4
    protected val field2Centerwidth = displaywidth / 2
    private var downX = 0
    private var downY = 0
    private var lastX = 0
    private lateinit var  playField: Field
    protected lateinit var enemyField: Field
    private var capturedBlock: Block? = null
    private var lastY = 0
    private var action=3
    val actionPlay: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


    init {




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
                            sendHint(3)
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
                                    sendHint(5)
                                }

                            } else {
                                //МУСОРКА---------------------------------------------------------------
                                if(capturedBlock!!.cx in 0..musorka.marginLeft+musorka.width*2
                                    && capturedBlock!!.cy in musorka.marginTop-blocksPool.cellSize..musorka.marginTop+musorka.height)
                                {
                                    capturedBlock?.destroy(blocks)
                                    capturedBlock=null
                                    sendHint(6)
                                }else {
                                    capturedBlock!!.detective = playField.figureDetection(capturedBlock!!)
                                    if( capturedBlock!!.detective) {
                                        sendHint(4)
                                    }
                                    if (playField.checkWin()) {
                                        sendHint(7)
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


    private fun sendHint(numberHint:Int){
        if(numberHint==action &&numberHint==7) {
            actionPlay.value = 7
            action++
        }
        if(numberHint==action &&numberHint==6) {
            actionPlay.value = 9
            action++
        }
        if(numberHint==action &&numberHint==5) {
            actionPlay.value = 5
            action++
        }
        if(numberHint==action &&numberHint==4) {
            actionPlay.value = 4
            action++
        }
        if(numberHint==action &&numberHint==3) {
            actionPlay.value = 3
            action++
        }
    }

   private fun checkBaggageBtn(baggageSize:Int, capturedBlock: Block?){
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