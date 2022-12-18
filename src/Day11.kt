import java.math.BigInteger
import kotlin.math.floor

fun BigInteger.divisibleBy(value: Int) = this % value.toBigInteger() == BigInteger.ZERO

class Monkey(
    val items: MutableList<BigInteger>,
    private val operation: (old: BigInteger) -> BigInteger,
    val divider: Int,
    private val trueMonkey: Int,
    private val falseMonkey: Int,
    var itemsInspected: Long = 0,
) {
    fun inspectItems() =
        items
            .map(operation)
            .map { BigInteger.valueOf(floor(it.toDouble() / 3).toLong()) }
            .map {
                if (it.divisibleBy(divider)) {
                    ThrowTo(it, trueMonkey)
                } else {
                    ThrowTo(it, falseMonkey)
                }
            }
            .also {
                itemsInspected += it.size
                items.clear()
            }
    fun inspectItemsVeryWorried() =
        items
            .map(operation)
            .map {
                if (it.divisibleBy(divider)) {
                    ThrowTo(it, trueMonkey)
                } else {
                    ThrowTo(it, falseMonkey)
                }
            }
            .also {
                itemsInspected += it.size
                items.clear()
            }
}

data class ThrowTo(
    val item: BigInteger,
    val monkey: Int
)

fun main() {
    fun bigIntegerListOf(vararg integers: Int) =
        integers.map { BigInteger.valueOf(it.toLong()) }.toMutableList()

    fun Int.toBigInteger() = BigInteger.valueOf(toLong())

    fun testMonkeys() =
        listOf(
            Monkey(bigIntegerListOf(79, 98),
                { old -> old * 19.toBigInteger() },
                23, 2, 3
            ),
            Monkey(bigIntegerListOf(54, 65, 75, 74),
                { old -> old + 6.toBigInteger() },
                19, 2, 0
            ),
            Monkey(bigIntegerListOf(79, 60, 97),
                { old -> old * old },
                13, 1, 3
            ),
            Monkey(bigIntegerListOf(74),
                { old -> old + 3.toBigInteger() },
                17, 0, 1
            ),
        )
    fun monkeys() =
        listOf(
            Monkey(bigIntegerListOf(89, 84, 88, 78, 70),
                { old -> old * 5.toBigInteger()},
                7, 6, 7
            ),
            Monkey(bigIntegerListOf(76, 62, 61, 54, 69, 60, 85),
                { old -> old + 1.toBigInteger() },
                17, 0, 6
            ),
            Monkey(bigIntegerListOf(83, 89, 53),
                { old -> old + 8.toBigInteger() },
                11, 5, 3
            ),
            Monkey(bigIntegerListOf(95, 94, 85, 57),
                { old -> old + 4.toBigInteger() },
                13, 0, 1
            ),
            Monkey(bigIntegerListOf(82, 98),
                { old -> old + 7.toBigInteger() },
                19, 5, 2
            ),
            Monkey(bigIntegerListOf(69),
                { old -> old + 2.toBigInteger() },
                2, 1, 3
            ),
            Monkey(bigIntegerListOf(82, 70, 58, 87, 59, 99, 92, 65),
                { old -> old * 11.toBigInteger() },
                5, 7, 4
            ),
            Monkey(bigIntegerListOf(91, 53, 96, 98, 68, 82),
                { old -> old * old },
                3, 4, 2
            ),
        )

    fun part1(input: List<Monkey>): Long {
        (1..20).forEach { _ ->
            input
                .forEach { monkey ->
                    monkey.inspectItems()
                        .forEach { item ->
                            input[item.monkey].items.add(item.item)
                        }
                }
            input.forEachIndexed { index, monkey ->
                // println("Monkey $index: ${monkey.items}")
            }
        }
        return input.map { it.itemsInspected }
            .sorted()
            .takeLast(2)
            .reduce { acc, i -> acc * i }

    }

    fun part2(input: List<Monkey>): Long {
        val reduceMod = input.map { it.divider }.reduce { a, b -> a * b }
        (1..10000).forEach { _ ->
            input
                .forEach { monkey ->
                    monkey.inspectItemsVeryWorried()
                        .forEach { item ->
                            input[item.monkey].items.add(item.item % reduceMod.toBigInteger())
                        }
                }
            input
                .forEach { check(it.itemsInspected > 0) }
        }
        return input.map { it.itemsInspected }
            .sorted()
            .takeLast(2)
            .also {
                println("Top items: $it")
            }
            .reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    println("================ TEST 1 ==============")
    check(part1(testMonkeys()) == 10605L)
    println("================ TEST 2 ==============")
    check(part2(testMonkeys()) == 2713310158L)

    println("================ REAL 1 ==============")
    // println(part1(monkeys()))
    check(part1(monkeys()) == 55930L)
    println("================ REAL 2 ==============")
    println(part2(monkeys()))
}
