package com.example.crash.games.ClassForGame

import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import com.example.crash.R

abstract class PoolAbstract(protected val parent: RelativeLayout,
                            protected  val blockList: ArrayList<Block>,
                            protected  val posX: Int,
                            val posY: Int,
                            protected  val size: Int,) {
    val actionPool: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val cellSize: Int = (size * 0.060).toInt()
    protected  val blocks: Array<Block?> = arrayOf(null, null, null, null, null, null)
    protected var colW = size /3
    protected var action = 2
    val tumanRerollOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    var tumanRerollIn=false
    var tumanRerollInenemy=false
    val tumanRerollOutenemy: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    var waterRerollIn=false
    val waterRerollOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    var iceTarget = 0
    var rerollIndex=0
    var firstMove = true
    var moveOfBottom = false
    val height=colW*2
    protected var bg: View = View(parent.context)

    open fun update() {
        if (iceTarget > 0) iceTarget--
        moveOfBottom = !moveOfBottom
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

   protected fun getBlockId(touchX: Int, touchY: Int): Int {
        return 3 * (touchY / colW) + touchX / colW
    }
}