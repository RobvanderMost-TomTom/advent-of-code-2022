import org.json.JSONArray

fun main() {
    fun isInOrder(left:JSONArray, right: JSONArray): Order {
        for (i in 0 until left.length()) {
            if (i >= right.length()) {
                return Order.OutOfOrder
            }
            val leftArray = left.optJSONArray(i)
            val rightArray = right.optJSONArray(i)
            val result = if (leftArray == null && rightArray == null) {
                val leftValue = left.getInt(i)
                val rightValue = right.getInt(i)
                if (leftValue < rightValue) {
                    Order.InOrder
                } else if (leftValue > rightValue) {
                    Order.OutOfOrder
                } else {
                    Order.Inconclusive
                }
            } else if (leftArray == null) {
                val leftValue = left.getInt(i)
                isInOrder(JSONArray(listOf(leftValue)), rightArray)
            } else if (rightArray == null) {
                val rightValue = right.getInt(i)
                isInOrder(leftArray, JSONArray(listOf(rightValue)))
            } else {
                isInOrder(leftArray, rightArray)
            }
            if (result != Order.Inconclusive) {
                return result
            }
        }
        if (left.length() < right.length()) {
            return Order.InOrder
        }
        return Order.Inconclusive
    }

    fun isInOrder(left:String, right: String): Order = isInOrder(JSONArray(left), JSONArray(right))

    fun part1(input: List<String>): Int {
        return input.chunked(3)
            .onEach { check(it.size >= 2) }
            .mapIndexedNotNull { index, pair ->
                if (isInOrder(pair[0], pair[1]) == Order.InOrder) {
                    println("In order: ${index + 1}")
                    index + 1
                } else {
                    println("Out of order: ${index + 1}")
                    null
                }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val sortedPackets = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .plus("[[2]]")
            .plus("[[6]]")
            .sortedWith { o1, o2 ->
                if (isInOrder(o1, o2) == Order.InOrder) {
                    -1
                } else {
                    1
                }
            }
            .onEach {
                println(it)
            }
            .toList()
        return (sortedPackets.indexOf("[[2]]") + 1) * (sortedPackets.indexOf("[[6]]") + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    check(isInOrder("[[6]]", "[6,1,4,0,3]") == Order.InOrder)

    val input = readInput("Day13")
    check(part1(input) == 5350)
    val answer2 = part2(input)
    check(answer2 != 20085)
    println(answer2)
}
