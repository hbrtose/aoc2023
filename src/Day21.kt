import java.util.LinkedList
import kotlin.math.ceil
import kotlin.math.pow

fun main() {

    fun getStart(input: List<String>): Point {
        input.forEachIndexed { index, s ->
            if (s.contains("S")) {
                return Point(s.indexOf("S"), index)
            }
        }
        return Point(0, 0)
    }

    fun step(points:List<Point>, input: List<String>, visited: Array<BooleanArray>): List<Point> {
        val res = mutableSetOf<Point>()
        points.forEach { p ->
            var neighbors = p.nearby4()
                .filter { it.x in input.first().indices && it.y in input.indices }
                .filter { input[it.y][it.x] != '#' }
            neighbors.forEach {
                visited[it.y][it.x] = true
                res.add(it)
            }
        }
        return res.toList()
    }

    fun part1(input: List<String>): Int {
        val start = getStart(input)
        var visited = Array(input.size) { BooleanArray(input.first().length) }
        var steps = listOf(start)
        var count = 0
        for (i in 1..64) {
            steps = step(steps, input, visited)
            count = visited.sumOf { it.count { it } }
            visited = Array(input.size) { BooleanArray(input.first().length) }
        }
        return count
    }

    fun part2(input: List<String>): Long {
        val start = getStart(input)
        val mod = 26501365 % input.size
        val vals = mutableListOf<Int>()
        for (i in listOf(mod, mod + input.size, mod + (2 * input.size))) {
            var queue = LinkedList<Point>()
            queue.offer(start)
            for (j in 0..<i) {
                val current = queue.copy()
                val visited = current.toMutableSet()
                queue = LinkedList()
                while (current.isNotEmpty()) {
                    val c = current.poll()
                    for (p in c.nearby4()) {
                        if (input[p.y.mod(input.size)][p.x.mod(input.first().length)] != '#' && !visited.contains(p)) {
                            visited.add(p)
                            queue.offer(p)
                        }
                    }
                }
            }
            vals.add(queue.size)
        }
        val m = vals[1] - vals[0]
        val n = vals[2] - vals[1]
        val a = (n - m) / 2
        val b = m - 3 * a
        val c = vals[0] - b - a
        val ceil = ceil(26501365.0 / input.size)
        return (a * ceil.pow(2) + b * ceil + c).toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 42)
    //check(part2(testInput) == 6)

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}
