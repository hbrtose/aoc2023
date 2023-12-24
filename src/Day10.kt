fun main() {

    fun getStart(input: List<String>): Point {
        val y = input.indexOfFirst { it.contains('S') }
        val x = input[y].indexOf('S')
        return Point(x, y)
    }

    fun path(input: List<String>, loc: Point, dir: Point): Point {
        var dir1 = dir
        return when(input[loc.y][loc.x]) {
            '|' -> dir1
            '-' -> dir1
            'L' -> {
                dir1 = if (dir1 == Point.DOWN) Point.RIGHT else Point.UP
                dir1
            }
            'J' -> {
                dir1 = if (dir1 == Point.DOWN) Point.LEFT else Point.UP
                dir1
            }
            '7' -> {
                dir1 = if (dir1 == Point.UP) Point.LEFT else Point.DOWN
                dir1
            }
            else -> {
                dir1 = if (dir1 == Point.UP) Point.RIGHT else Point.DOWN
                dir1
            }
        }
    }

    fun part1(input: List<String>): Int {
        val start = getStart(input)
        var direction = Point.DOWN
        var steps = 1
        var location = start + direction
        while (location != start) {
            steps++
            direction = path(input, location, direction)
            location += direction
        }
        return steps / 2
    }

    fun countClosed(closed: Array<Array<Char>>): Int {
        var count = 0
        closed.forEach { cl ->
            var inside = false
            var lastChar = '.'
            cl.forEach {
                when(it) {
                    '.' -> {
                        lastChar = '.'
                        if (inside) {
                            count++
                        }
                    }
                    '|' -> {
                        inside = !inside
                    }
                    '-' -> it
                    else -> {
                        if (lastChar == '.') {
                            lastChar = it
                            inside = !inside
                        } else {
                            if (lastChar == 'J' && it == 'L' || lastChar == 'L' && it == 'J' || lastChar == '7' && it == 'F' || lastChar == 'F' && it == '7') {
                                inside = !inside
                            }
                            lastChar = '.'
                        }
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val start = getStart(input)
        var direction = Point.RIGHT
        var steps = 1
        var location = start + direction
        val closed = Array(input.size) { Array(input[0].length) { '.' } }
        while (location != start) {
            steps++
            direction = path(input, location, direction)
            closed[location.y][location.x] = input[location.y][location.x]
            location += direction
        }
        closed[start.y][start.x] = '|'
        return countClosed(closed)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 4)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
