import kotlin.math.abs

private data class Day09Instruction(
    val direction: Char,
    val amount: Int
) {
    companion object {
        fun fromString(input: String) =
            Day09Instruction(input[0], input.substring(2).toInt())
    }
}
fun main() {
    fun Coordinate.moveUp() = Coordinate(x, y+1)
    fun Coordinate.moveDown() = Coordinate(x, y-1)
    fun Coordinate.moveLeft() = Coordinate(x-1, y)
    fun Coordinate.moveRight() = Coordinate(x+1, y)

    fun Coordinate.isTouching(other: Coordinate): Boolean {
        val deltaX = abs(other.x - x)
        val deltaY = abs(other.y - y)
        return (deltaY < 2 && deltaX < 2)
    }
    fun Coordinate.follow(other: Coordinate) =
        if (isTouching(other)) {
            this
        } else {
            let {
                when  {
                    other.x > it.x -> it.moveRight()
                    other.x < it.x -> it.moveLeft()
                    else -> it
                }
            }
                .let {
                    when {
                        other.y > it.y -> it.moveUp()
                        other.y < it.y -> it.moveDown()
                        else -> it
                    }
                }
        }

    fun part1(input: List<String>): Int {
        var head = Coordinate(0, 0)
        var tail = Coordinate(0, 0)
        val tailPositions = mutableSetOf(tail)

        input
            .map { Day09Instruction.fromString(it) }
            .forEach {
                (1..it.amount).forEach { _ ->
                    head = when (it.direction) {
                        'R' -> head.moveRight()
                        'L' -> head.moveLeft()
                        'U' -> head.moveUp()
                        'D' -> head.moveDown()
                        else -> throw RuntimeException("Invalid direction")
                    }
                    tail = tail.follow(head)
                    tailPositions.add(tail)
                }
            }
        return tailPositions.size
    }

    fun part2(input: List<String>): Int {
        var rope = buildList {
            (0..9).forEach { _ -> add(Coordinate(0,0))}
        }
        return input
            .map { Day09Instruction.fromString(it) }
            .flatMap {instruction ->
                List(instruction.amount) { instruction.direction }
            }
            .scan(rope) { r, d ->
                buildList {
                    val h = when (d) {
                        'R' -> r.first().moveRight()
                        'L' -> r.first().moveLeft()
                        'U' -> r.first().moveUp()
                        'D' -> r.first().moveDown()
                        else -> throw RuntimeException("Invalid direction")
                    }
                    add(h)
                    r.drop(1).scan(h) { prev, current ->
                        val t = current.follow(prev)
                        add(t)
                        t
                    }
                }
            }
            .map { it.last() }
            .toSet()
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
