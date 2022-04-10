package com.example.crash.games.ClassForGame

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import com.example.crash.R

@SuppressLint("ClickableViewAccessibility")
class Pool(private val parent: RelativeLayout,
           private val blockList: ArrayList<Block>,
           private val posX: Int,
           val posY: Int,
           private val size: Int,) {
    val actionPool: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val cellSize: Int = (size * 0.074).toInt()
    private val blocks: Array<Block?> = arrayOf(null, null, null, null, null, null)
    private var colW = size / 3
    private var action = 2
    val height=colW*2
    private var bg:View = View(parent.context)

    init {
        bg.setBackgroundResource(R.drawable.pool_rl)
        bg.setOnTouchListener { v, event -> if (event.action == MotionEvent.ACTION_DOWN) {
            val id = getBlockId(event.x.toInt(), event.y.toInt())
            when (action) {
                2 -> {
                    blocks[id]?.startDestroyAnimation(blockList)
                    blocks[id] = null
                }
                1 -> {
                    blocks[id]?.startMoveAnimation(if (id < 3) colW * 2 else colW)
                    blocks[id] = null
                }
                0 -> update()
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

    private fun update() {
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

    fun clearBack(){
        colW=(size * 0.33).toInt()
        parent.removeView(bg)
        bg.setBackgroundResource(0)
        bg.setOnTouchListener(null)
        var params = RelativeLayout.LayoutParams(size, colW * 2)
        params.leftMargin = posX
        params.topMargin = posY
        parent.addView(bg, params)
        update()
    }

    fun destroy(){
        parent.removeView(bg)
        blocks.forEach { it?.startDestroyAnimation(blockList) }
    }

    private fun getBlockId(touchX: Int, touchY: Int): Int {
        return 3 * (touchY / colW) + touchX / colW
    }
}