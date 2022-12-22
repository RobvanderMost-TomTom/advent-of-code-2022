/**
 * This was a nice try, but way to complicated. Still fun to do.
 */
enum class Day13FailedListType{
    IMPLICIT,
    EXPLICIT
}
class Day13FailedPacketReader(
    data: String
) {
    private val dataIt = data.iterator().withIndex()
    private var currentChar: IndexedValue<Char>? = dataIt.next()
    private val listStack = mutableListOf<Day13FailedListType>()

    init {
        check(isListStart())
        check(dataIt.hasNext())
    }

    fun next() {
        currentChar = if (dataIt.hasNext()) {
            dataIt.next()
        } else {
            null
        }
    }
    fun isListStart() = currentChar?.value == '['
    fun isListEnd() = currentChar?.value == ']'
    fun isDigit() = currentChar?.value?.isDigit() ?: false
    fun isComma() = currentChar?.value == ','
    fun isEmpty() = currentChar == null

    fun readInteger() =
        buildString {
            while (isDigit()) {
                append(currentChar?.value)
                next()
            }
        }.toInt()

    fun startList(kind: Day13FailedListType) = listStack.add(kind)
    fun endList() = listStack.removeLast()
    fun currentListType() = listStack.last()
    fun currentPosition() = currentChar?.index ?: -1
}

fun main() {
    fun arePacketsInOrder(left: String, right: String): Boolean {
        val lr = Day13FailedPacketReader(left)
        val rr = Day13FailedPacketReader(right)

        fun printResult(result: Boolean, description: String) {
            if (result) {
                print("In order: ")
            } else {
                print("Not in order: ")
            }
            println(description)
            println(left)
            if (lr.currentPosition() == -1) {
                (1..left.lastIndex).forEach { print(" ")}
            } else {
                (1..lr.currentPosition()).forEach { print(" ")}
            }
            println("^")

            println(right)
            if (rr.currentPosition() == -1) {
                (1..right.lastIndex).forEach { print(" ")}
            } else {
                (1..rr.currentPosition()).forEach { print(" ")}
            }
            println("^")
        }

        do {
            if (rr.isEmpty()) {
                printResult(false, "Right empty before left")
                return false
            } else if (lr.isListStart()) {
                lr.startList(Day13FailedListType.EXPLICIT)
                lr.next()
                if (rr.isListStart()) {
                    rr.startList(Day13FailedListType.EXPLICIT)
                    rr.next()
                } else if (rr.isListEnd()) {
                    printResult(false, "Left new list at end of right list")
                    return false
                } else if (rr.isDigit()) {
                    if (rr.currentListType() == Day13FailedListType.IMPLICIT) {
                        printResult(false, "Already created implicit list right")
                        return false
                    }
                    rr.startList(Day13FailedListType.IMPLICIT)
                }
            } else if (lr.isDigit()) {
                if (rr.isDigit()) {
                    val lv = lr.readInteger()
                    val rv = rr.readInteger()
                    if (lv < rv) {
                        // Proven in order
                        printResult(true, "Left value below right")
                        return true
                    } else if (lv > rv) {
                        // Proven out of order
                        printResult(false, "Left value above right")
                        return false
                    }
                    // Not conclusive yet
                } else if (rr.isListStart()) {
                    if (lr.currentListType() == Day13FailedListType.IMPLICIT) {
                        printResult(false, "Already created implicit list left")
                        return false
                    }
                    lr.startList(Day13FailedListType.IMPLICIT)
                    rr.startList(Day13FailedListType.EXPLICIT)
                    rr.next()
                } else if (rr.isListEnd()) {
                    printResult(false, "Right list ended early")
                    return false
                } else if (rr.isComma()) {
                    rr.next()
                }
            } else if (lr.isComma()) {
                while (lr.currentListType() == Day13FailedListType.IMPLICIT) {
                    lr.endList()
                }
                lr.next()
                if (rr.isComma()) {
                    while (rr.currentListType() == Day13FailedListType.IMPLICIT) {
                        rr.endList()
                    }
                    rr.next()
                } else if (rr.isListEnd()) {
                    printResult(false, "Right list ended early")
                    return false
                }
            } else if (lr.isListEnd()) {
                while (rr.currentListType() == Day13FailedListType.IMPLICIT) {
                    rr.endList()
                }
                if (rr.isListStart()) {
                    rr.startList(Day13FailedListType.EXPLICIT)
                    rr.next()
                } else {
                    lr.endList()
                    lr.next()
                    if (rr.isListEnd()) {
                        rr.endList()
                        rr.next()
                    } else if (rr.isComma() || rr.isListStart() || rr.isDigit()) {
                        printResult(true, "Left list ended early")
                        return true
                    }
                }
            }

        } while (!lr.isEmpty())

        printResult(true, "Past end of left")
        return true
    }

    fun part1(input: List<String>): Int {
        return input.chunked(3)
            .onEach { check(it.size >= 2) }
            .mapIndexedNotNull { index, pair ->
                if (arePacketsInOrder(pair[0], pair[1])) {
                    index + 1
                } else {
                    null
                }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    println("==================== TEST ===================")
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(!arePacketsInOrder("[[[]]]", "[[]]"))
    check(!arePacketsInOrder("[[[[],6,[1,0,1,0,2]],[3,3,[7,10,2,4],[2,10,10],5],[1],[6,[9,3,2,9,3],[4,8,2,1,6],[5,6,5]]],[[[3,6],1],[[8,9,3,3,7],[10],[1,8,0,9],[3,6,7,10,7]]],[[0,[6,4,2,7],8,[7,4]],[[3,2,7],[9,1,7],1,4]],[[[4,6,7,1,1]],[[2,8,8,2]],[[],0,[4,3,8,3,5],10,7],10],[[0,8]]]",
        "[[],[10,[[],6]],[[],6,6,8],[]]"))

    println("\n==================== REAL 1 ===================")
    val input = readInput("Day13")
    val answer1 = part1(input)
    check(answer1 != 5646)
    check(answer1 != 5423)
    check(answer1 != 4510)
    println(answer1)
    println("\n==================== REAL 2 ===================")
    println(part2(input))
}
