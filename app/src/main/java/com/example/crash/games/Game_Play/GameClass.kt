package com.example.crash.games.Game_Play

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
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
    size:Int=30
) {
    private var blocks= arrayListOf<Block>()
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
    private val skillsWidht = (displaywidth - fieldwidth) / 2


    init {
        parentGame.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (block in blocks.reversed())
                        if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                            Log.d("Block", "${event.x.toInt()}")
                            downX = event.x.toInt()
                            downY = event.y.toInt()
                            lastX = downX
                            lastY = downY
                            capturedBlock = block
                            break
                        }
                }
                ///сделал логику move чтоб фиугра не выходила за пределы поля
                ///баг!!!!! если фигура на кнопке рюкзака ,то фигуру взять нельзя !!!!! надо пофиксить

                MotionEvent.ACTION_MOVE -> {
                    if (capturedBlock != null) {
                        if (capturedBlock!!.detective) {
                            playField.figureOutDetection(capturedBlock!!)
                        }

                        if (event.x.toInt() - capturedBlock!!.touch_x_left > 0 + skillsWidht
                            && event.x.toInt() - capturedBlock!!.touch_x_right < displaywidth - skillsWidht
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
                }
                MotionEvent.ACTION_UP -> {
                    if (event.x.toInt() == downX && event.y.toInt() == downY) {
                        playField.figureOutDetection(capturedBlock!!)
                        capturedBlock?.rotate(playField)
                    } else {
                        if (capturedBlock != null) {
                            capturedBlock!!.detective =
                                playField.figureDetection(capturedBlock!!)
                        }
                    }
                    capturedBlock = null
                }
            }
            true
        }
    }
}