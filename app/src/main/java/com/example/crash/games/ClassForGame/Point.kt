package com.example.crash.games.ClassForGame

import kotlin.collections.ArrayList

class Point(var x: Int = 0, var y: Int = 0) {
    fun inArray(array: ArrayList<Point>): Boolean {
        for (index in array) {

            if (index.x == this.x && index.y == this.y) {
                return false
            }
        }
        return true
    }

    fun inDetectivePos(array: ArrayList<Point>, coordinate_offsetx :Int =0, coordinate_offsety:Int =0, fieldSize:Int): Boolean {
        for (field in array) {
            if (this.y +coordinate_offsety >= field.y && this.y + coordinate_offsety < field.y + fieldSize
                && this.x +coordinate_offsetx < field.x + fieldSize && this.x + coordinate_offsetx >= field.x
            ) {
                return true
            }
        }
        return false
    }

    fun getDetectivePos(array: ArrayList<Point>): Int {
        for (index in 0 until array.size) {
            //костыль должно просто по равентству кординат
            if (array[index].x == this.x && array[index].y == this.y) {
                return index
            }
            /*if (array[index].x <= this.x+1 && array[index].x >= this.x-1 &&
                array[index].y <= this.y+1 && array[index].y >= this.y-1) {
                return index
            }*/
        }
        return -1
    }


    fun inDekstope(centeX: Int, centeY: Int,size_Img:Int): Boolean {
        return this.x - size_Img > 0 && this.y > 0 && this.x + 50 < centeX * 2 && this.y + size_Img < centeY * 2
    }

}