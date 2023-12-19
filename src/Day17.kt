import Point.Companion.RIGHT
import Point.Companion.LEFT
import Point.Companion.UP
import Point.Companion.DOWN
import java.util.PriorityQueue

fun main() {

    fun calculate(map: List<List<Int>>, startDirs: List<Point>, minSteps: Int = 1, isValid: (State, Point) -> Boolean): Int {
        val possibleDirs = mapOf(
            UP to setOf(UP, RIGHT, LEFT),
            LEFT to setOf(LEFT, UP, DOWN),
            DOWN to setOf(DOWN, RIGHT, LEFT),
            RIGHT to setOf(RIGHT, UP, DOWN)
        )

        val goal = Point(map.first().lastIndex, map.lastIndex)
        val losses = mutableListOf<Int>()
        startDirs.forEach {
            val seen = mutableSetOf<State>()
            val queue = PriorityQueue<Work>()
            State(Point(0, 0), it, 0).apply {
                queue += Work(this, 0)
                seen += this
            }

            while (queue.isNotEmpty()) {
                val current = queue.poll()
                if (current.state.p == goal && current.state.steps >= minSteps) {
                    losses.add(current.loss)
                    return@forEach
                }

                possibleDirs
                    .getValue(current.state.direction)
                    .filter {
                        val p = current.state.p + it
                        p.x >= 0 && p.x <= map.first().lastIndex && p.y >= 0 && p.y <= map.lastIndex
                    }
                    .filter { isValid(current.state, it) }
                    .map { current.state.next(it) }
                    .filterNot { it in seen }
                    .forEach {
                        queue += Work(it, current.loss + map[it.p.y][it.p.x])
                        seen += it
                    }
            }
        }
        println(losses)
        return losses.min()
    }

    fun part1(input: List<String>): Int {
        val map = input.map { row -> row.map { it.digitToInt() } }
        return calculate(map, listOf(RIGHT)) { state, next ->
            state.steps < 3 || state.direction != next
        }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { row -> row.map { it.digitToInt() } }
        return calculate(map, listOf(RIGHT, DOWN), 4) { state, next ->
            if (state.steps > 9) state.direction != next
            else if (state.steps < 4) state.direction == next
            else true
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102)
    check(part2(testInput) == 94)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

private data class State(val p: Point, val direction: Point, val steps: Int) {
    fun next(dir: Point): State = State(p + dir, dir, if (direction == dir) steps + 1 else 1)
}

private data class Work(val state: State, val loss: Int) : Comparable<Work> {
    override fun compareTo(other: Work): Int = loss - other.loss
}
