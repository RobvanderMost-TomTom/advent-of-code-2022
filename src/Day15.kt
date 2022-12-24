import kotlin.math.abs

fun Coordinate.manhattanDistanceTo(other: Coordinate) =
    abs(x - other.x) + abs(y - other.y)

data class Sensor(
    val location: Coordinate,
    val closestBeacon: Coordinate,
    val distanceToBeacon: Int = location.manhattanDistanceTo(closestBeacon)
)
fun main() {
    fun readSensors(input: List<String>): List<Sensor> {
        val re = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
        return input.mapNotNull { re.matchEntire(it) }
            .map {
                Sensor(
                    Coordinate(it.groupValues[1].toInt(), it.groupValues[2].toInt()),
                    Coordinate(it.groupValues[3].toInt(), it.groupValues[4].toInt()),
                )
            }
    }

    fun part1(input: List<String>, y: Int): Int {
        val sensors = readSensors(input)
        check(sensors.size == input.size)

        val minX = sensors.minOf {
            minOf(it.location.x - it.distanceToBeacon, it.closestBeacon.x)
        }
        val maxX = sensors.maxOf {
            maxOf(it.location.x + it.distanceToBeacon, it.closestBeacon.x)
        }

        return (minX..maxX)
            .map { x -> Coordinate(x, y) }
            .count { coord ->
                sensors.any { sensor ->
                    coord != sensor.closestBeacon &&
                    coord.manhattanDistanceTo(sensor.location) <= sensor.distanceToBeacon
                }
            }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input))
}
