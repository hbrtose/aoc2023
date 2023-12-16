fun main() {

    fun getNext(input: List<String>, visit: Visit): List<Direction> {
        return when(input[visit.x][visit.y]) {
            '-' -> if (visit.from == Direction.LEFT || visit.from == Direction.RIGHT) listOf(visit.from) else listOf(Direction.LEFT, Direction.RIGHT)
            '|' -> if (visit.from == Direction.UP || visit.from == Direction.DOWN) listOf(visit.from) else listOf(Direction.UP, Direction.DOWN)
            '\\' -> when(visit.from) {
                Direction.UP -> listOf(Direction.RIGHT)
                Direction.DOWN -> listOf(Direction.LEFT)
                Direction.LEFT -> listOf(Direction.DOWN)
                Direction.RIGHT -> listOf(Direction.UP)
            }
            '/' -> when(visit.from) {
                Direction.UP -> listOf(Direction.LEFT)
                Direction.DOWN -> listOf(Direction.RIGHT)
                Direction.LEFT -> listOf(Direction.UP)
                Direction.RIGHT -> listOf(Direction.DOWN)
            }
            else -> listOf(visit.from)
        }
    }

    fun getTiles(input: List<String>, start: Visit): Int {
        val bools = Array(input.size) { BooleanArray(input.first().length) }
        val visits = mutableSetOf<Visit>()
        var next = listOf(start)
        while (next.isNotEmpty()) {
            next = next.mapNotNull { visit ->
                if (visit.x < 0 || visit.x > input.lastIndex || visit.y < 0 || visit.y > input.first().lastIndex || visits.contains(visit)) {
                    return@mapNotNull null
                }
                bools[visit.x][visit.y] = true
                visits.add(visit)
                getNext(input, visit).map { dir ->
                    Visit(visit.x + dir.x, visit.y + dir.y, dir)
                }
            }.flatten()
        }
        return bools.sumOf { it.count() { it } }
    }

    fun part1(input: List<String>): Int {
        return getTiles(input, Visit(0, 0, Direction.LEFT))
    }

    fun part2(input: List<String>): Int {
        val maxTop = input.first().indices.maxOf { getTiles(input, Visit(0, it, Direction.DOWN)) }
        val maxDown = input.last().indices.maxOf { getTiles(input, Visit(input.lastIndex, it, Direction.UP)) }
        val maxLeft = input.indices.maxOf { getTiles(input, Visit(it, 0, Direction.LEFT)) }
        val maxRight = input.indices.maxOf { getTiles(input, Visit(it, input.first().lastIndex, Direction.RIGHT)) }
        return listOf(maxTop, maxDown, maxLeft, maxRight).max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

enum class Direction(val x: Int, val y: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, 1),
    RIGHT(0, -1)
}

data class Visit(val x: Int, val y: Int, val from: Direction)
