fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.split(",", limit = 2)
                .map { e -> e.split("-", limit = 2).map { e -> e.toInt() } }
        }.count {
            (it[0][0] <= it[1][0] && it[0][1] >= it[1][1]) ||
                    (it[1][0] <= it[0][0] && it[1][1] >= it[0][1])
        }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(",", limit = 2)
                .map { e -> e.split("-", limit = 2).map { e -> e.toInt() } }
        }.count {
            (it[1][0] in it[0][0]..it[0][1]) ||
                    (it[1][1] in it[0][0]..it[0][1]) ||
                    (it[0][0] in it[1][0]..it[1][1]) ||
                    (it[0][1] in it[1][0]..it[1][1])
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
