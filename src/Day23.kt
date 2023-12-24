fun main() {

    fun longestPath(
        input: List<String>,
        visited: Array<BooleanArray>,
        current: Point, end: Point,
        steps: Int,
        ignoreSteep: Boolean
    ) : Int {
        val directionals = "v^<>"
        val dirsMap = hashMapOf('v' to Point.DOWN, '^' to Point.UP, '>' to Point.RIGHT, '<' to Point.LEFT)
        if (!current.containedIn(input) || visited[current.y][current.x] || input[current.y][current.x] == '#') {
            return -1
        }

        if (current == end) {
            return steps
        }

        if (input[current.y][current.x] in directionals && !ignoreSteep) {
            return longestPath(
                input,
                visited,
                current + (dirsMap[input[current.y][current.x]] ?: Point(0, 0)),
                end,
                steps + 1,
                ignoreSteep
            )
        }

        visited[current.y][current.x] = true
        val max = current.nearby4().maxOf { p ->
            longestPath(input, visited, p, end, steps + 1, ignoreSteep)
        }
        visited[current.y][current.x] = false

        return max
    }

    fun part1(input: List<String>): Int {
        val visited = Array(input.size) { BooleanArray(input.first().length) }
        val start = Point(1, 0)
        val end = Point(input.first().lastIndex - 1, input.lastIndex)
        return longestPath(input, visited, start, end, 0, false)
    }

    fun part2(input: List<String>): Int {
        val visited = Array(input.size) { BooleanArray(input.first().length) }
        val start = Point(1, 0)
        val end = Point(input.first().lastIndex - 1, input.lastIndex)
        return longestPath(input, visited, start, end, 0, true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
