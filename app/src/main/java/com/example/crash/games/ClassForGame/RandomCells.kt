package com.example.crash.games.ClassForGame

import kotlin.random.Random

class RandomCells (count: Int,
                   private val max_width: Int = count,
                   private val max_height: Int = max_width) {
    val cells: ArrayList<Array<Int>> = arrayListOf(arrayOf(0, 0))
    var left = 0
        private set
    var top = 0
        private set
    var right = 0
        private set
    var bot = 0
        private set

    init {
        println("##############################")
        assert(max_width * max_height >= count)

        val pool = arrayListOf(arrayOf(-1, 0), arrayOf(0, -1), arrayOf(1, 0), arrayOf(0, 1))
        val allCell = ArrayList<Array<Int>>()
        for (n in (2..count)) {
            cells.add(pool.removeAt(Random.nextInt(pool.size)))
            val cell = cells.last()

            if (cell[0] < left) left = cell[0]
            if (cell[1] < top) top = cell[1]
            if (cell[0] > right) right = cell[0]
            if (cell[1] > bot) bot = cell[1]

            for (c in ArrayList<Array<Int>>(pool)) if (!inBorder(c)) pool.remove(c)

            val p: Array<Array<Int>?> = arrayOf(arrayOf(cell[0] - 1, cell[1]),
                arrayOf(cell[0], cell[1] - 1), arrayOf(cell[0] + 1, cell[1]),
                arrayOf(cell[0], cell[1] + 1))

            allCell.clear()
            allCell.addAll(cells)
            allCell.addAll(pool)
            for (c in allCell) for (i in (p.indices))
                if (c[0] == p[i]?.get(0) && c[1] == p[i]?.get(1)) p[i] = null

            for (c in p) if (c != null && inBorder(c)) { pool.add(c) }
        }
    }

    private fun inBorder(cell: Array<Int>): Boolean {
        return cell[0] - left + 1 <= max_width &&
                right - cell[0] + 1 <= max_width &&
                cell[1] - top + 1 <= max_height &&
                bot - cell[1] + 1 <= max_height
    }
}