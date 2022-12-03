fun main() {
    fun String.splitRucksack() =
        toCharArray(0, length / 2) to toCharArray(length / 2)

    fun Char.toPriority() =
        if (this in 'a'..'z') {
            this - 'a' + 1
        } else {
            this - 'A' + 27
        }

    fun part1(input: List<String>): Int {
        return input.map { it.splitRucksack() }
            .map { it.first.intersect(it.second.asList().toSet()) }
            .sumOf { it.sumOf { c -> c.toPriority() }}
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.toCharArray().toSet() }
            .chunked(3)
            .flatMap { it.reduce { acc, s -> acc.intersect(s) }}
            .sumOf { it.toPriority() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
