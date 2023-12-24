import java.lang.Math.abs

fun main() {

    fun findGalaxies(input: List<String>): List<Point> {
        val galaxies = mutableListOf<Point>()
        input.forEachIndexed { index, s ->
            s.forEachIndexed { indexOfChar, c ->
                if (c == '#') {
                    galaxies.add(Point(indexOfChar, index))
                }
            }
        }
        return galaxies
    }

    fun expand(input: List<String>, galaxies: List<Point>, by: Long): List<Point> {
        val emptyRows = input.indices.filter { !input[it].contains("#") }
        val emptyCols = input[0].indices.filter { ind -> input.all { it[ind] == '.' } }
        return galaxies.map { p ->
            var r = p.y.toLong()
            var c = p.x.toLong()
            for (er in emptyRows) {
                if (p.y > er) r += by - 1
            }
            for (ec in emptyCols) {
                if (p.x > ec) c += by - 1
            }
            Point(r.toInt(), c.toInt())
        }
    }

    fun part1(input: List<String>): Long {
        val galaxies = findGalaxies(input)
        val expanded = expand(input, galaxies, 2L)
        return expanded.flatMap { g ->
            expanded.map { gg ->
                abs(g.y - gg.y) + abs(g.x - gg.x)
            }
        }.sum() / 2L
    }

    fun part2(input: List<String>): Long {
        val galaxies = findGalaxies(input)
        val expanded = expand(input, galaxies, 1000000L)
        return expanded.flatMap { g ->
            expanded.map { gg ->
                abs(g.y - gg.y) + abs(g.x - gg.x)
            }
        }.sum() / 2L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
