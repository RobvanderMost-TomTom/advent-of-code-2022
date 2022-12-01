fun main() {
    fun foodPerElf(input: List<String>): List<List<String>> {
        val separators = input.withIndex().filter { it.value.isEmpty() }.map { it.index }
        return (listOf(-1) + separators + listOf(input.size)).zipWithNext().map { input.subList(it.first + 1, it.second) }
    }

    fun part1(input: List<String>): Int {
        val elves = foodPerElf(input)
        return elves.maxOf { food ->
            food.sumOf { it.toInt() }
        }
    }

    fun part2(input: List<String>): Int {
        return foodPerElf(input)
            .map { it.sumOf { it.toInt() }}
            .sorted()
            .takeLast(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
