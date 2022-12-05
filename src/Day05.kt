data class Instruction(
    val amount: Int,
    val fromStack: Int,
    val toStack: Int
)
fun main() {
    fun splitStacksAndInstructions(input: List<String>): Pair<List<String>, List<String>> {
        val separator = input.indexOfFirst { it.isEmpty() }
        return input.subList(0, separator) to input.subList(separator + 1, input.size)
    }

    fun parseStacks(input: List<String>): List<MutableList<Char>> {
        val numberOfStacks = input.last().split(" ").last().toInt()
        val stacks = buildList(numberOfStacks + 1) {
            (0..numberOfStacks).forEach{ _ -> add(mutableListOf<Char>())}
        }

        input.dropLast(1).reversed().forEach { row ->
            (1..numberOfStacks).forEach { stackNr ->
                val crateCol = 1 + 4 * (stackNr - 1)
                if (crateCol < row.length) {
                    val crate = row[crateCol]
                    if (!crate.isWhitespace()) {
                        stacks[stackNr].add(crate)
                    }
                }
            }
        }

        return stacks
    }

    fun parseInstructions(input: List<String>) =
        input.mapNotNull {
            "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(it)
        }.map {
            Instruction(it.groupValues[1].toInt(), it.groupValues[2].toInt(), it.groupValues[3].toInt())
        }

    fun createMover9000(stacks: List<MutableList<Char>>, instructions: List<Instruction>) {
        instructions.forEach {instruction ->
            check(instruction.fromStack < stacks.size)
            check(instruction.toStack < stacks.size)
            check(stacks[instruction.fromStack].size >= instruction.amount)
            (1..instruction.amount).forEach { _ ->
                stacks[instruction.toStack].add(stacks[instruction.fromStack].removeLast())
            }

            stacks.drop(1).forEachIndexed { index, chars -> println("${index+1} - ${chars.joinToString()}") }
        }
    }

    fun createMover9001(stacks: List<MutableList<Char>>, instructions: List<Instruction>) {
        instructions.forEach {instruction ->
            check(instruction.fromStack < stacks.size)
            check(instruction.toStack < stacks.size)
            check(stacks[instruction.fromStack].size >= instruction.amount)
            stacks[instruction.toStack].addAll(stacks[instruction.fromStack].takeLast(instruction.amount))
            (1..instruction.amount).forEach { _ ->
                stacks[instruction.fromStack].removeLast()
            }

            stacks.drop(1).forEachIndexed { index, chars -> println("${index+1} - ${chars.joinToString()}") }
        }
    }

    fun part1(input: List<String>): String {
        val (rawStacks, rawInstructions) = splitStacksAndInstructions(input)
        val stacks = parseStacks(rawStacks)
        check(stacks.maxOf { it.size } == rawStacks.size - 1)
        val instructions = parseInstructions(rawInstructions)
        check(instructions.size == rawInstructions.size)
        createMover9000(stacks, instructions)
        return stacks.drop(1).joinToString(separator = "") { it.last().toString() }
    }

    fun part2(input: List<String>): String {
        val (rawStacks, rawInstructions) = splitStacksAndInstructions(input)
        val stacks = parseStacks(rawStacks)
        check(stacks.maxOf { it.size } == rawStacks.size - 1)
        val instructions = parseInstructions(rawInstructions)
        check(instructions.size == rawInstructions.size)
        createMover9001(stacks, instructions)
        return stacks.drop(1).joinToString(separator = "") { it.last().toString() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    println("==========================================")
    check(part2(testInput) == "MCD")
    println("==========================================")

    val input = readInput("Day05")
    println(part1(input))
    println("==========================================")
    println(part2(input))
}
