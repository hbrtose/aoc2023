fun main() {

    fun countLoad(input: List<String>): Int {
        return input.mapIndexed { index, s ->
            (input.size - index) * s.count { it == 'O' }
        }.sum()
    }

    fun optimizeString(s: String): String {
        var sliced = s.split("#")
        return sliced.joinToString("#") {
            it.toCharArray().sortedDescending().joinToString("")
        }
    }

    fun unoptimizeString(s: String): String {
        var sliced = s.split("#")
        return sliced.joinToString("#") {
            it.toCharArray().sorted().joinToString("")
        }
    }

    fun north(input: List<String>): List<String> {
        return transposeStrings(transposeStrings(input).map { optimizeString(it) })
    }

    fun south(input: List<String>): List<String> {
        return transposeStrings(transposeStrings(input).map { unoptimizeString(it) })
    }

    fun east(input: List<String>): List<String> {
        return input.map { unoptimizeString(it) }
    }

    fun west(input: List<String>): List<String> {
        return input.map { optimizeString(it) }
    }

    fun part1(input: List<String>): Int {
        return countLoad(north(input))
    }

    fun spinCycle(input: List<String>) = listOf(::north, ::west, ::south, ::east).fold(input) { value, f ->
        f(value)
    }

    fun part2(input: List<String>): Int {
        return countLoad(nTimes(::spinCycle, input, 1000000000L))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
