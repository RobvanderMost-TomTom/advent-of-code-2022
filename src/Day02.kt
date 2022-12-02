enum class Hand(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSOR(3);
}

enum class Result(val score: Int) {
    LOSE(0) {
        override fun playFor(other: Hand): Hand =
            when (other) {
                Hand.ROCK -> Hand.SCISSOR
                Hand.PAPER -> Hand.ROCK
                Hand.SCISSOR -> Hand.PAPER
            }
    },
    DRAW(3) {
        override fun playFor(other: Hand): Hand = other
    },
    WIN(6) {
        override fun playFor(other: Hand): Hand =
            when (other) {
                Hand.ROCK -> Hand.PAPER
                Hand.PAPER -> Hand.SCISSOR
                Hand.SCISSOR -> Hand.ROCK
            }
    };

    abstract fun playFor(other: Hand): Hand
}

fun main() {
    fun Char.toHand() =
        when (this) {
            'A', 'X' -> Hand.ROCK
            'B', 'Y' -> Hand.PAPER
            'C', 'Z' -> Hand.SCISSOR
            else -> throw RuntimeException("Invalid value")
        }

    fun Char.toResult() =
        when (this) {
            'X' -> Result.LOSE
            'Y' -> Result.DRAW
            'Z' -> Result.WIN
            else -> throw RuntimeException("Invalid value")
        }

    fun Hand.beats(other: Hand) =
        ((this == Hand.PAPER && other == Hand.ROCK) || (this == Hand.SCISSOR && other == Hand.PAPER) || (this == Hand.ROCK && other == Hand.SCISSOR))

    fun Hand.scoreTo(other: Hand) =
        if (this.beats(other)) {
            println("$this beats $other -> ${6 + score}")
            6 + score
        } else if (this == other) {
            println("$this draw to $other -> ${3 + score}")
            3 + score
        } else {
            println("$this loses from $other -> $score")
            score
        }

    fun part1(input: List<String>): Int {
        return input.sumOf { it[2].toHand().scoreTo(it[0].toHand()) }
    }

    fun part2(input: List<String>): Int {
        return input.map { it[0].toHand() to it[2].toResult() }
            .map { it.second.playFor(it.first) to it.second }
            .sumOf { it.first.score + it.second.score }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
