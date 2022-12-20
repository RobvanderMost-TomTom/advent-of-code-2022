data class Day12Pos(
    val x: Int,
    val y: Int,
) {
    fun up() = Day12Pos(x, y - 1)
    fun down() = Day12Pos(x, y + 1)
    fun left() = Day12Pos(x - 1, y)
    fun right() = Day12Pos(x + 1, y)
}

data class Hill(
    val height: Char,
    var minDistance: Int = Int.MAX_VALUE
) {
    fun reachableFrom(origin: Hill) =
        when {
            (origin.height == 'z' || origin.height == 'y') && height == 'E' -> true
            height == origin.height -> true
            height - origin.height == 1 -> true
            height != 'E' && height != 'S' && height - origin.height < 0 -> true
            height == 'a' && origin.height == 'S' -> true
            else -> false
        }
}

fun main() {
    fun List<List<Hill>>.at(pos: Day12Pos) = this[pos.y][pos.x]

    fun List<List<Hill>>.validPos(pos: Day12Pos) =
        (pos.x >= 0) && (pos.y >= 0) && (pos.x <= first().lastIndex) && (pos.y <= lastIndex)

    fun List<List<Hill>>.findPath(origin: Day12Pos, distance: Int = 0): Int? {
        val newDistance = distance + 1
        return listOf(origin.down(), origin.up(), origin.left(), origin.right())
            .filter { validPos(it) }
            .filter { at(it).reachableFrom(at(origin)) }
            .also {
                if(it.any { dest -> at(dest).height == 'E'}) {
                    // short circuit
                    return newDistance
                }
            }
            .filter { at(it).minDistance > newDistance }
            .sortedByDescending {
                at(it).height
            }
            .onEach {
                at(it).minDistance = newDistance
            }
            .mapNotNull { findPath(it, newDistance) }
            .minOrNull()
    }

    fun findStartPos(input: List<String>): Day12Pos {
        input.forEachIndexed { y, row ->
            val x = row.indexOf('S')
            if (x > -1) {
                return Day12Pos(x, y)
            }
        }
        check(false)
        return Day12Pos(-1, -1)
    }

    fun loadMap(input: List<String>) =
        input.map { row ->
            row.map { height ->
                Hill(height)
            }
        }

    fun part1(input: List<String>): Int? {
        val hillMap = loadMap(input)
        val startPos = findStartPos(input)
        return hillMap.findPath(startPos)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(findStartPos(testInput) == Day12Pos(0, 0))
    check(part1(testInput) == 31)

    val input = readInput("Day12")
    check(findStartPos(input) == Day12Pos(0, 20))
    println(part1(input))
    println(part2(input))
}
