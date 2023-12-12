import java.lang.Math.abs

fun main() {

    fun findGalaxies(input: List<String>): List<Pair<Int, Int>> {
        val galaxies = mutableListOf<Pair<Int, Int>>()
        input.forEachIndexed { index, s ->
            s.forEachIndexed { indexOfChar, c ->
                if (c == '#') {
                    galaxies.add(index to indexOfChar)
                }
            }
        }
        return galaxies
    }

    fun expand(input: List<String>, galaxies: List<Pair<Int, Int>>, by: Long): List<Pair<Long, Long>> {
        val emptyRows = input.indices.filter { !input[it].contains("#") }
        val emptyCols = input[0].indices.filter { ind -> input.all { it[ind] == '.' } }
        return galaxies.map { (row, col) ->
            var r = row.toLong()
            var c = col.toLong()
            for (er in emptyRows) {
                if (row > er) r += by - 1
            }
            for (ec in emptyCols) {
                if (col > ec) c += by - 1
            }
            r to c
        }
    }

    fun part1(input: List<String>): Long {
        val galaxies = findGalaxies(input)
        val expanded = expand(input, galaxies, 2L)
        return expanded.flatMap { g ->
            expanded.map { gg ->
                abs(g.first - gg.first) + abs(g.second - gg.second)
            }
        }.sum() / 2L
    }

    fun part2(input: List<String>): Long {
        val galaxies = findGalaxies(input)
        val expanded = expand(input, galaxies, 1000000L)
        return expanded.flatMap { g ->
            expanded.map { gg ->
                abs(g.first - gg.first) + abs(g.second - gg.second)
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
