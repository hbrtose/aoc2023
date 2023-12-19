import kotlin.math.absoluteValue

fun main() {

    fun countArea(stages: List<Stage>): Long {
        return stages
            .runningFold(Point(0, 0)) { point, stage ->
                point + (stage.dir * stage.dist)
            }.zipWithNext { a, b ->
                (b.x - a.x) * a.y.toLong()
            }.sum().absoluteValue + stages.sumOf { it.dist } / 2 + 1
    }

    fun part1(input: List<String>): Long {
        val stages = input.map { inp ->
            inp.split(" ").let {
                val dir = when(it[0]) {
                    "U" -> Point.UP
                    "R" -> Point.RIGHT
                    "L" -> Point.LEFT
                    "D" -> Point.DOWN
                    else -> Point(0, 0)
                }
                Stage(dir, it[1].toInt(), it[2].substring(2..7).toInt(16))
            }
        }
        return countArea(stages)
    }

    fun part2(input: List<String>): Long {
        val stages = input.map { inp ->
            inp.split(" ").let {
                val dir = when(it[0]) {
                    "U" -> Point.UP
                    "R" -> Point.RIGHT
                    "L" -> Point.LEFT
                    "D" -> Point.DOWN
                    else -> Point(0, 0)
                }
                Stage(dir, it[1].toInt(), it[2].substring(2..7).toInt(16)).fromColor()
            }
        }
        return countArea(stages)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62L)
    check(part2(testInput) == 952408144115)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

data class Stage(
    val dir: Point,
    val dist: Int,
    val color: Int
) {
    fun fromColor(): Stage {
        val d = when(color % 16) {
            0 -> Point.RIGHT
            1 -> Point.DOWN
            2 -> Point.LEFT
            3 -> Point.UP
            else -> Point(0, 0)
        }
        return Stage(d, color / 16, 0)
    }
}
