data class File(
    val name: String,
    val size: Int
)

data class Dir(
    val name: String,
    val parent: Dir? = null,
    val files: MutableList<File> = mutableListOf(),
    val dirs: MutableMap<String, Dir> = mutableMapOf()
) {
    fun size(): Int =
        files.sumOf { it.size } + dirs.values.sumOf { it.size() }

    fun allSizes(): List<Int> =
        dirs.values.flatMap { it.allSizes() } + size()

}

fun main() {
    fun parseFileTree(input: List<String>): Dir {
        val root = Dir("")
        var currentDir = root

        input
            .map {
                it.split(" ")
            }
            .forEach {
                check(it.size >= 2)
                if (it[0] == "$" && it[1] == "cd") {
                    check(it.size == 3)
                    currentDir = if (it[2] == "/") {
                        root
                    } else if (it[2] == "..") {
                        checkNotNull(currentDir.parent)
                        currentDir.parent!!
                    } else {
                        currentDir.dirs[it[2]] ?: throw RuntimeException()
                    }
                } else if (it[0] == "dir") {
                    if (!currentDir.dirs.containsKey(it[1])) {
                        currentDir.dirs[it[1]] = Dir(it[1], currentDir)
                    }
                } else if (it[0].toIntOrNull() != null){
                    currentDir.files.add(
                        File(it[1], it[0].toInt())
                    )
                }
            }
        return root
    }

    fun part1(input: List<String>): Int {
        val tree = parseFileTree(input)
        return tree.allSizes().filter { it < 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val tree = parseFileTree(input)
        val totalDiskSpace = 70000000
        val spaceRequired = 30000000
        val freeDiskSpace = totalDiskSpace - tree.size()
        val toFree = spaceRequired - freeDiskSpace
        check(toFree > 0)
        return tree.allSizes()
            .filter { it > toFree }.minOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
