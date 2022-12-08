fun main() {
    fun readMatrix(input: List<String>) =
        input
            .map { row ->
                row.map { it.digitToInt() }
            }

    fun List<List<Int>>.leftOf(x: Int, y: Int) =
        this[y].subList(0, x)

    fun List<List<Int>>.rightOf(x: Int, y: Int) =
        this[y].subList(x+1, this[y].size)

    fun List<List<Int>>.above(x: Int, y: Int) =
        subList(0, y).map { it[x] }

    fun List<List<Int>>.below(x: Int, y: Int) =
        subList(y+1, size).map { it[x] }

    fun List<List<Int>>.isVisible(x: Int, y: Int) =
        leftOf(x, y).all { it < this[y][x] } ||
                rightOf(x, y).all { it < this[y][x] } ||
                above(x, y).all { it < this[y][x] } ||
                below(x, y).all { it < this[y][x] }

    fun List<Int>.visibleCount(height: Int) =
        indexOfFirst { it >= height }
            .let {
                if (it == -1) size else it+1
            }

    fun List<List<Int>>.scenicScore(x: Int, y: Int) =
        leftOf(x, y).reversed().visibleCount(this[y][x]) *
                rightOf(x, y).visibleCount(this[y][x]) *
                above(x, y).reversed().visibleCount(this[y][x]) *
                below(x, y).visibleCount(this[y][x])

    fun part1(input: List<String>): Int {
        val trees = readMatrix(input)
        return (1 until trees.lastIndex)
            .sumOf {y ->
                (1 until trees[y].lastIndex)
                    .count {x ->
                        trees.isVisible(x, y)
                    }
            } + trees.size * 2 + trees.first().size + trees.last().size - 4
    }

    fun part2(input: List<String>): Int {
        val trees = readMatrix(input)
        return (0 .. trees.lastIndex)
            .maxOf {y ->
                (0..trees[y].lastIndex)
                    .maxOf {x ->
                        trees.scenicScore(x, y)
                    }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val trees = readMatrix(testInput)
    check(trees.isVisible(1, 1))
    check(trees.isVisible(2, 1))
    check(!trees.isVisible(3, 1))
    check(trees.isVisible(1, 2))
    check(!trees.isVisible(2, 2))
    check(trees.isVisible(3, 2))
    check(!trees.isVisible(1, 3))
    check(trees.isVisible(2, 3))
    check(!trees.isVisible(3, 3))
    check(part1(testInput) == 21)
    check(trees.scenicScore(2, 1) == 4)
    check(trees.scenicScore(2, 3) == 8)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
