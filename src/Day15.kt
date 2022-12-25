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

    fun part2(input: List<String>, maxCoordinate: Int): Long {
        val sensors = readSensors(input)
        check(sensors.size == input.size)

        for (x in 0..maxCoordinate) {
            var y = 0
            while (y <= maxCoordinate) {
                val coord = Coordinate(x, y)
                val furthestSensor = sensors.filter { sensor ->
                    coord.manhattanDistanceTo(sensor.location) <= sensor.distanceToBeacon
                }.maxByOrNull { sensor ->
                    sensor.distanceToBeacon - abs(sensor.location.x - x)
                } ?: return 4000000L * x + y

                y = furthestSensor.location.y +
                        furthestSensor.distanceToBeacon -
                        abs(furthestSensor.location.x - x) + 1

            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    check(part1(input, 2000000) == 4879972)
    val answer2 = part2(input, 4000000)
    check(answer2 > 1602012312L)
    println(answer2)
}
