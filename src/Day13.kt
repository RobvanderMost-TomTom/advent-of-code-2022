import org.json.JSONArray

fun main() {
    fun isInOrder(left:JSONArray, right: JSONArray): Order {
        for (i in 0 until left.length()) {
            if (i >= right.length()) {
                return Order.OutOfOrder
            }
            val leftArray = left.optJSONArray(i)
            val rightArray = right.optJSONArray(i)
            if (leftArray == null && rightArray == null) {
                val leftValue = left.getInt(i)
                val rightValue = right.getInt(i)
                if (leftValue < rightValue) {
                    return Order.InOrder
                } else if (leftValue > rightValue) {
                    return Order.OutOfOrder
                }
            } else if (leftArray == null) {
                val leftValue = left.getInt(i)
                return isInOrder(JSONArray(listOf(leftValue)), rightArray)
            } else if (rightArray == null) {
                val rightValue = right.getInt(i)
                return isInOrder(leftArray, JSONArray(listOf(rightValue)))
            } else {
                val result = isInOrder(leftArray, rightArray)
                if (result != Order.Inconclusive) {
                    return result
                }
            }
        }
        if (left.length() < right.length()) {
            return Order.InOrder
        }
        return Order.Inconclusive
    }
    fun part1(input: List<String>): Int {
        return input.chunked(3)
            .onEach { check(it.size >= 2) }
            .mapIndexedNotNull { index, pair ->
                val left = JSONArray(pair[0])
                val right = JSONArray(pair[1])
                if (isInOrder(left, right) == Order.InOrder) {
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
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
