package com.example.crash.games.ClassForGame

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.crash.R
import kotlin.random.Random

open class Block(private var parent: RelativeLayout, posX: Int, posY: Int, private var size: Int,
                 copy: Block? =null, block1x1:Boolean=false) {

    val cells = ArrayList<ImageView>()
    var width: Int
    var height: Int
    var coordinatsBlock = arrayListOf<Point>()
    protected var lockAnimated = 0
    lateinit var cellsPos : RandomCells
    var detective = false
    var cx: Int // координаты центра
    var cy: Int // координаты центра
    var touch_x_left:Int=0
    var touch_x_right:Int=0
    var touch_y_top:Int=0
    var touch_y_bottom:Int=0
    var type: Type

    init {
        if(copy==null) {
            if(block1x1){
                val t = Random.nextInt(8)
                type = Type.values()[if (t > 4) 0 else t]
                cellsPos = RandomCells(1)

                for (point in cellsPos.cells) {
                    cells.add(ImageView(parent.context))
                    cells.last().setImageResource(IMAGE_RESOURCES[type.ordinal * 4 + Random.nextInt(4)])
                    cells.last().rotation = 90f * Random.nextInt(4)
                    val params = RelativeLayout.LayoutParams(size, size)
                    params.leftMargin = posX + (point[0] - cellsPos.left) * size
                    params.topMargin = posY + (point[1] - cellsPos.top) * size
                    coordinatsBlock.add(Point(params.leftMargin,params.topMargin))
                    parent.addView(cells.last(), params)
                }
                width = (cellsPos.right - cellsPos.left + 1) * size
                height = (cellsPos.bot - cellsPos.top + 1) * size
                cx = posX + width / 2
                cy = posY + height / 2
            }
            else {
                val t = Random.nextInt(8)
                type = Type.values()[if (t > 4) 0 else t]
                cellsPos = RandomCells(Random.nextInt(2, 6), 4)

                for (point in cellsPos.cells) {
                    cells.add(ImageView(parent.context))
                    cells.last().setImageResource(IMAGE_RESOURCES[type.ordinal * 4 + Random.nextInt(4)])
                    cells.last().rotation = 90f * Random.nextInt(4)
                    val params = RelativeLayout.LayoutParams(size, size)
                    params.leftMargin = posX + (point[0] - cellsPos.left) * size
                    params.topMargin = posY + (point[1] - cellsPos.top) * size
                    parent.addView(cells.last(), params)
                }
                width = (cellsPos.right - cellsPos.left + 1) * size
                height = (cellsPos.bot - cellsPos.top + 1) * size
                cx = posX + width / 2
                cy = posY + height / 2
            }
        }
        else{
            type = copy.type
            cellsPos = copy.cellsPos
            size=copy.size


            for (point in cellsPos.cells) {
                cells.add(ImageView(parent.context))
                cells.last().setImageResource(IMAGE_RESOURCES[type.ordinal * 4 + Random.nextInt(4)])
                cells.last().rotation = 90f * Random.nextInt(4)
                val params = RelativeLayout.LayoutParams(size, size)
                params.leftMargin = posX + (point[0] - cellsPos.left) * size
                params.topMargin = posY + (point[1] - cellsPos.top) * size
                coordinatsBlock.add(Point(params.leftMargin,params.topMargin))
                parent.addView(cells.last(), params)
            }
            width = (cellsPos.right - cellsPos.left + 1) * size
            height = (cellsPos.bot - cellsPos.top + 1) * size
            cx = posX + width / 2
            cy = posY + height / 2
        }
    }

    fun containsPoint(x: Int, y: Int): Boolean {
        for (cell in cells)
            if (x in (cell.left..cell.right) && y in (cell.top..cell.bottom)) {
                touch_x_left=((x-cells.minOf{it.left}))
                touch_x_right=((x-cells.maxOf{it.right}))
                touch_y_top=((y-cells.minOf{it.top}))
                touch_y_bottom=((y-cells.maxOf{it.bottom}))
                return true
            }
        return false
    }

    fun move(deltaX: Int, deltaY: Int) {

        for(block in coordinatsBlock){
            block.x+=deltaX
            block.y+=deltaY
        }
        cx += deltaX
        cy += deltaY
        for (cell in cells) {
            val params = cell.layoutParams as RelativeLayout.LayoutParams
            params.leftMargin += deltaX
            params.topMargin += deltaY
            cell.layoutParams = params
        }
    }


    fun startCreateAnimation() {
        for (cell in cells) {
            cell.startAnimation(AnimationUtils.loadAnimation(parent.context, R.anim.create_block))
        }
    }

    fun rotate(field: Field) {
        if (lockAnimated > 0) return
        lockAnimated = cells.size
        var i=0
       for (cell in cells) {
            val p = cell.layoutParams as RelativeLayout.LayoutParams
            val anim = RotateAnimation(0f, 90f, (cx - p.leftMargin).toFloat(), (cy - p.topMargin).toFloat())
            anim.duration = 400 // длительность
            anim.isFillEnabled = true // хз зачем но надо
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    // всё это выполняетя после окончания анимации
                    val params = cell.layoutParams as RelativeLayout.LayoutParams
                    val temp = cx + cy - params.topMargin - size
                    params.topMargin = cy - cx + params.leftMargin
                    params.leftMargin = temp
                    cell.layoutParams = params
                    cell.rotation = (cell.rotation + 90) % 360

                    lockAnimated -= 1
                    coordinatsBlock[i].x=params.leftMargin
                    coordinatsBlock[i].y=params.topMargin
                    i++
                    if(lockAnimated==0){
                       detective= field.figureDetection(this@Block)
                    }
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })

            cell.startAnimation(anim)

        }

    }


    fun startDestroyAnimation(blockList: ArrayList<Block>) {
        for (cell in cells) {
            val p = cell.layoutParams as RelativeLayout.LayoutParams
            val animSet = AnimationSet(parent.context, null)
            animSet.duration = 400
            animSet.fillAfter = true
            val anim1 = RotateAnimation(
                0f, 180f,
                (p.leftMargin - cx + size).toFloat(), (p.topMargin - cy + size).toFloat()
            )
            val anim2 = AlphaAnimation(1f, 0f)
            animSet.addAnimation(anim1)
            animSet.addAnimation(anim2)
            if (cell == cells[0]) {
                animSet.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        Handler(Looper.getMainLooper()).postDelayed({ destroy(blockList) }, 100)
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                })
            }
            cell.startAnimation(animSet)
        }
    }

    fun destroy(blockList: ArrayList<Block>){
        blockList.remove(this)
        for (c in cells) parent.removeView(c)
    }

    fun startMoveAnimation(distance: Int) {
        for (cell in cells) {
            val anim = TranslateAnimation(0f, 0f,
                0f, distance.toFloat())
            anim.duration = 500
            anim.isFillEnabled = true
            if (cell == cells[0]) {
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        move(0, distance)
                    }
                    override fun onAnimationRepeat(p0: Animation?) {}
                })
            }
            cell.startAnimation(anim)
            var pointb = Point(cell.left, cell.top)
            Log.d("Field1","${pointb.x}+ ${pointb.y}")
            coordinatsBlock.add(pointb)
        }
    }

    fun delete(){
        for (index in cells){
            index.visibility = View.GONE
        }
    }

    enum class Type { DEFAULT, EARTH, WATER, FIRE, AIR }
    companion object {
        val IMAGE_RESOURCES = arrayOf(
            R.drawable.blok01, R.drawable.blok02, R.drawable.blok03, R.drawable.blok04,
            R.drawable.blok11, R.drawable.blok12, R.drawable.blok13, R.drawable.blok14,
            R.drawable.blok21, R.drawable.blok22, R.drawable.blok23, R.drawable.blok24,
            R.drawable.blok31, R.drawable.blok32, R.drawable.blok33, R.drawable.blok34,
            R.drawable.blok41, R.drawable.blok42, R.drawable.blok43, R.drawable.blok44)
    }
}


