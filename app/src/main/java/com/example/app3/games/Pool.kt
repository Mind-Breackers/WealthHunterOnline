package com.example.app3.games

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import com.example.app3.R

@SuppressLint("ClickableViewAccessibility")
class Pool(private val parent: RelativeLayout,
                private val blockList: ArrayList<Block>,
                private val posX: Int,
                private val posY: Int,
                val size: Int) {
    val blocks: Array<Block?> = arrayOf(null, null, null, null, null, null)
    val colW = (size * 0.27).toInt()
    val actionI: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    var action = 2

    init {
        val bg = View(parent.context)
        bg.setBackgroundColor(Color.argb(150, 0, 0, 0))
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
            actionI.value=action
            action--
        }
            true
        }
        var params = RelativeLayout.LayoutParams(size, colW * 2)
        params.leftMargin = posX
        params.topMargin = posY
        parent.addView(bg, params)

        val btn = Button(parent.context)
        btn.setBackgroundResource(R.drawable.btn_update)
        val btnAnim = RotateAnimation(0f, -360f,
            size * 0.08f, size * 0.08f)
        btnAnim.duration = 700
        btn.setOnTouchListener {view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> view.setBackgroundResource(R.drawable.btn_update_pressed)
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundResource(R.drawable.btn_update)
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
            val bl = Block(parent, posX, posY, (size * 0.06).toInt())
            bl.move(colW * (i % 3) + (colW - bl.width) / 2,
                colW * (i / 3) + (colW - bl.height) / 2)
            blockList.add(bl)
            blocks[i] = bl
            bl.startCreateAnimation()
        }
    }

    protected fun getBlockId(touchX: Int, touchY: Int): Int {
        return 3 * (touchY / colW) + touchX / colW
    }
}