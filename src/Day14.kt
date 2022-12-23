enum class Material(val char: Char) {
    SAND('o'),
    ROCK('#'),
    AIR('.')
}

// Cave type. Index of Map is x coordinate, index in each List is y coordinate
typealias CaveMap = MutableMap<Int, MutableList<Material>>

fun main() {
    fun CaveMap.print() {
        println()
        val maxDepth = values.maxOf { it.lastIndex }
        val sorted = this.toSortedMap()
        for (y in 0..maxDepth) {
            sorted.values.forEach {
                print((it.getOrNull(y) ?: Material.AIR).char)
            }
            println()
        }
    }

    fun readCaveMap(input: List<String>): CaveMap {
        val caveMap = mutableMapOf<Int, MutableList<Material>>()

        fun drawRockVertical(start: Pair<Int, Int>, end: Pair<Int, Int>) {
            val x = start.first
            val y1 = minOf(start.second, end.second)
            val y2 = maxOf(start.second, end.second)

            val col = caveMap.getOrPut(x, ::mutableListOf)
            if (col.lastIndex < y2) {
                for (y in col.size..y2) {
                    col.add(Material.AIR)
                }
            }
            for (y in y1..y2) {
                col[y] = Material.ROCK
            }
        }

        fun drawRockHorizontal(start: Pair<Int, Int>, end: Pair<Int, Int>) {
            val x1 = minOf(start.first, end.first)
            val x2 = maxOf(start.first, end.first)
            val y = start.second

            for (x in x1..x2) {
                val col = caveMap.getOrPut(x, ::mutableListOf)
                if (col.lastIndex < y) {
                    for (y in col.size..y) {
                        col.add(Material.AIR)
                    }
                }
                col[y] = Material.ROCK
            }
        }

        fun drawRock(start: Pair<Int, Int>, end: Pair<Int, Int>) =
            if (start.first == end.first) {
                drawRockVertical(start, end)
            } else if (start.second == end.second) {
                drawRockHorizontal(start, end)
            } else {
                throw RuntimeException("Cannot draw diagonal rock!")
            }

        input.map { line ->
            line.split(" -> ")
                .map { pair ->
                    pair.split(",").map { it.toInt() }
                }.map {
                    it[0] to it[1]
                }
        }.forEach { coords ->
            coords.windowed(2, 1).forEach {
                drawRock(it.first(), it.last())
            }
        }

        return caveMap
    }

    fun CaveMap.dropSand(getCol: (x: Int) -> MutableList<Material>?): Boolean {
        var x = 500
        var y = 0

        while (true) {
            val col = getCol(x) ?: return false  // No material here, falling into abyss

            if (col[y] != Material.AIR) {
                return false
            }

            val airBelow = col.drop(y).indexOfFirst { it != Material.AIR }
            if (airBelow > 1) {
                y += airBelow - 1
                continue
            }

            val leftCol = getCol(x - 1) ?: return false // No material to the left, abyss
            if (leftCol.lastIndex < y + 1) {
                // No rock below on the left side, fall into abyss
                return false
            } else if (leftCol[y + 1] == Material.AIR) {
                x--
                y++
                continue
            }

            val rightCol = getCol(x + 1) ?: return false // No material to the right, abyss
            if (rightCol.lastIndex < y + 1) {
                // No rock below on the right side, fall into abyss
                return false
            } else if (rightCol[y + 1] == Material.AIR) {
                x++
                y++
                continue
            }

            // Cannot fall further
            col[y] = Material.SAND
            return true
        }
    }

    fun part1(input: List<String>): Int {
        val caveMap = readCaveMap(input)
        caveMap.print()

        var count = 0
        while (caveMap.dropSand { caveMap[it] }) {
            count++
        }
        caveMap.print()
        return count
    }

    fun CaveMap.addFloor() {
        val floorDepth = values.maxOf { it.lastIndex } + 2
        values.forEach {
            for (y in it.size until floorDepth) {
                it.add(Material.AIR)
            }
            it.add(Material.ROCK)
        }
    }

    fun CaveMap.getOrExtendFloor(index: Int) =
        getOrPut(index) {
            val floorDepth = values.first().lastIndex
            buildList(floorDepth + 1) {
                for (y in 0 until floorDepth) {
                    add(Material.AIR)
                }
                add(Material.ROCK)
            }.toMutableList()
        }

    fun part2(input: List<String>): Int {
        val caveMap = readCaveMap(input)
        caveMap.addFloor()
        caveMap.print()

        var count = 0
        while (caveMap.dropSand { caveMap.getOrExtendFloor(it) }) {
            count++
        }
        caveMap.print()
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    check(part1(input) == 1330)
    println(part2(input))
}
