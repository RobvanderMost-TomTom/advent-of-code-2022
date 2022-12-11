class CPU(
    var cycle: Int = 1,
    var x: Int = 1,
    var signalStrength: Int = 0
) {
    fun noop() = finishCycle()
    fun addx(value: Int) {
        finishCycle()
        finishCycle()
        x += value
    }

    private fun finishCycle() {
        if ((cycle - 20) % 40 == 0) {
            signalStrength += cycle * x
        }
        if (crtPosition() == 0) println()
        if (isSpriteDrawn()) {
            print("#")
        } else {
            print(".")
        }
        cycle++
    }

    private fun crtPosition() = (cycle - 1) % 40

    private fun isSpriteDrawn() = crtPosition() in (x-1..x+1)
}
fun main() {
    fun part1(input: List<String>): Int {
        val cpu = CPU()
        input.forEach {
            when {
                it == "noop" -> cpu.noop()
                it.startsWith("addx") -> {
                    cpu.addx(it.split(" ").last().toInt())
                }
            }
        }
        return cpu.signalStrength
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    println()

    val input = readInput("Day10")
    println(part1(input))
}
