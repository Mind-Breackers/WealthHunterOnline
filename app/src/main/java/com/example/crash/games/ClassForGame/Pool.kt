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
    val cellSize: Int = (size * 0.06).toInt()
    private val blocks: Array<Block?> = arrayOf(null, null, null, null, null, null)
    private var colW = (size * 0.28).toInt()
    private var action = 2
    private lateinit var btn:View
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


        btn = Button(parent.context)
        btn.setBackgroundResource(R.drawable.update_btn)
        val btnAnim = RotateAnimation(0f, -360f,
            size * 0.08f, size * 0.08f)
        btnAnim.duration = 700
        btn.setOnTouchListener {view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> view.setBackgroundResource(R.drawable.update_btn)
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundResource(R.drawable.update_btn)
                    view.startAnimation(btnAnim)
                    update()
                }
            }
            true
        }
        params = RelativeLayout.LayoutParams((size * 0.16).toInt(), (size * 0.16).toInt())
        params.leftMargin = posX + (size * 0.825).toInt()
        params.topMargin = posY + (size * 0.17).toInt()
        parent.addView(btn, params)


        update()
    }

    protected fun update() {
        action = 2
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
        parent.removeView(btn)
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
        parent.removeView(btn)
        blocks.forEach { it?.startDestroyAnimation(blockList) }
    }

    protected fun getBlockId(touchX: Int, touchY: Int): Int {
        return 3 * (touchY / colW) + touchX / colW
    }
}