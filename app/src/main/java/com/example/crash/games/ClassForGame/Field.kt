package com.example.crash.games.ClassForGame

import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.crash.R
import kotlin.math.pow

class Field(
    var parent: RelativeLayout,
    private val size: Int,
    private val cellSize: Int=10,
    private val cx: Int,
    private val cy:Int,
    heightrl:Int,
    widthrl:Int
) {
    private val cells = ArrayList<ImageView>()
    private var coordinats = arrayListOf<Point>()
    private var fulldetective=false
  //  private val cx: Int=widthrl/2// координаты АБСОЛЮТНОГО центра поля (не угла клетки!!!)
   // private val cy: Int= heightrl/2// координаты АБСОЛЮТНОГО центра поля (не угла клетки!!!)
    private var detective_coordinats = arrayListOf<Point>()

    init {

        val genCells = RandomCells(size, 11, 7)
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
            img.setImageResource(R.drawable.field1)

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

    fun figureOutDetection(figure: Block): Boolean {
       // Log.d("Field1","${detective_coordinats.size}")
        if (figure.detective) {
            for (block in figure.coordinatsBlock) {
                //Log.d("Field1","${block.x}+${block.y}")
                if (block.getDetectivePos(detective_coordinats) != -1) {
                    detective_coordinats.removeAt(block.getDetectivePos(detective_coordinats))
                }
            }
            figure.detective = false
        }
       // Log.d("Field1","${detective_coordinats.size}")
        return true
    }

    fun figureDetection(figure: Block): Boolean {
        var detective_field1 = arrayListOf<Point>()
        var detective_field2 = arrayListOf<Point>()
        var detective_field3 = arrayListOf<Point>()
        var detective_field4 = arrayListOf<Point>()
        //println("start")
        var flag_side = 0;
        for (block in figure.coordinatsBlock) {
            for (field in coordinats) {
                if (block.y >= field.y && block.y < field.y + cellSize
                    && block.x < field.x + cellSize  && block.x >= field.x && !block.inDetectivePos(detective_coordinats,fieldSize = cellSize)
                    && !block.inDetectivePos(detective_field1,fieldSize = cellSize)) {
                    detective_field1.add(field)
                }
            }

            for (field in coordinats) {
                if (block.y + cellSize  >= field.y && block.y + cellSize  < field.y + cellSize
                    && block.x + cellSize < field.x + cellSize
                    && block.x + cellSize >= field.x
                    && !block.inDetectivePos(detective_coordinats, cellSize, cellSize ,fieldSize = cellSize)
                    && !block.inDetectivePos(detective_field2, cellSize , cellSize,fieldSize = cellSize )
                ) {
                    detective_field2.add(field)
                }
            }

            for (field in coordinats) {
                if (block.y + cellSize  >= field.y && block.y + cellSize < field.y + cellSize
                    && block.x < field.x + cellSize
                    && block.x >= field.x && !block.inDetectivePos(detective_coordinats, 0, cellSize,fieldSize = cellSize)
                    && !block.inDetectivePos(detective_field3, 0, cellSize ,fieldSize = cellSize)
                ) {
                    detective_field3.add(field)
                }
            }

            for (field in coordinats) {
                if (block.y >= field.y && block.y < field.y + cellSize
                    && block.x + cellSize < field.x + cellSize
                    && block.x + cellSize >= field.x && !block.inDetectivePos(detective_coordinats, cellSize ,fieldSize = cellSize)
                    && !block.inDetectivePos( detective_field4,cellSize,fieldSize = cellSize)
                ) {
                    detective_field4.add(field)
                }
            }

        }

        var vectors = arrayListOf<Double>()
        vectors.add(VectoreAdd(detective_field1, figure))
        vectors.add(VectoreAdd(detective_field2, figure))
        vectors.add(VectoreAdd(detective_field3, figure))
        vectors.add(VectoreAdd(detective_field4, figure))

        var min = vectors.minByOrNull { it }
        if (min != Double.MAX_VALUE) {
            flag_side = vectors.indexOf(min) + 1
        }

        if (detective_field1.size == figure.coordinatsBlock.size && flag_side == 1) {
            for (block in detective_field1) {
                detective_coordinats.add(block)
            }
            figure.move(
                Center(detective_field1).x - Center(figure.coordinatsBlock).x ,
                Center(detective_field1).y - Center(figure.coordinatsBlock).y
            )

            return true
        }
        if (detective_field2.size == figure.coordinatsBlock.size && flag_side == 2) {
            for (block in detective_field2) {
                detective_coordinats.add(block)
            }
            figure.move(
                Center(detective_field2).x - Center(figure.coordinatsBlock).x ,
                Center(detective_field2).y - Center(figure.coordinatsBlock).y
            )

            return true
        }
        if (detective_field3.size == figure.coordinatsBlock.size && flag_side == 3) {
            for (block in detective_field3) {
                detective_coordinats.add(block)
            }
            figure.move(
                Center(detective_field3).x - Center(figure.coordinatsBlock).x ,
                Center(detective_field3).y - Center(figure.coordinatsBlock).y
            )

            return true
        }
        if (detective_field4.size == figure.coordinatsBlock.size && flag_side == 4) {
            for (block in detective_field4) {
                detective_coordinats.add(block)
            }
            figure.move(
                Center(detective_field4).x - Center(figure.coordinatsBlock).x ,
                Center(detective_field4).y - Center(figure.coordinatsBlock).y
            )

            return true
        }
        return false
    }

    private fun Center(figur: ArrayList<Point>): Point {
        var top = Int.MAX_VALUE   // крайние точки всего блока
        var right = Int.MIN_VALUE // ...
        var bot = Int.MIN_VALUE   // ...
        var left = Int.MAX_VALUE  // крайние точки всего блока

        figur.forEach() {
            if (it.y < top) top = it.y
            if (it.x + size > right) right = it.x + size
            if (it.y + size > bot) bot = it.y + size
            if (it.x < left) left = it.x
        }
        var cx = left + (right - left) / 2
        var cy = top + (bot - top) / 2
        return Point(cx, cy)
    }

    private fun VectoreAdd(detective_field: ArrayList<Point>, figure: Block): Double {
        if (detective_field.size == figure.coordinatsBlock.size) {
            return kotlin.math.sqrt(
                (Center(detective_field).x - figure.cx ).toDouble().pow(2.0) + (Center(detective_field).y - figure.cy ).toDouble().pow(2.0)
            )
        } else return Double.MAX_VALUE
    }

    fun checkWin():Boolean{
        fulldetective = detective_coordinats.size==coordinats.size
        return fulldetective
    }

}
