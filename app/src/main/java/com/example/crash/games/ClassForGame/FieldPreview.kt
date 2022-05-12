package com.example.crash.games.ClassForGame

import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.crash.R
import kotlin.random.Random

class FieldPreview(
    parent: RelativeLayout, size: Int, cellSize: Int, cx: Int, cy: Int,
    heightrl: Int, widthrl: Int
)  {

    private val cells = ArrayList<ImageView>()
    private var coordinats = arrayListOf<Point>()
    private var fulldetective=false
    private var detective_coordinats = arrayListOf<Point>()

    init {

        val genCells = RandomCells(((heightrl/cellSize*widthrl/cellSize)/2), widthrl/cellSize, heightrl/cellSize)
        for (cell in genCells.cells) {
            println("${cell[0]} ${cell[1]}")
            coordinats.add(
                Point(
                    cx + cell[0] * cellSize - (genCells.left + genCells.right + 1) * cellSize / 2,
                    cy + cell[1] * cellSize - (genCells.top + genCells.bot + 1) * cellSize / 2
                )
            ) }

        for (i in 0 until coordinats.size) {
            val img = ImageView(parent.context)
            cells.add(img)

            img.setImageResource(IMAGE_RESOURCES[Random.nextInt(IMAGE_RESOURCES.size)])

            val params = RelativeLayout.LayoutParams(
                cellSize,
                cellSize
            )
            params.leftMargin = coordinats[i].x
            params.topMargin = coordinats[i].y
            //img.layoutParams = params // второй вариант

            parent.addView(img, params) // parent.addView(img) // второй вариант

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