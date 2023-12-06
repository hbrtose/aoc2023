fun main() {

    fun solveRace(race: Pair<Long, Long>): Long {
        var ways = 0L
        for (i in 0..race.first) {
            val distance = (race.first - i) * i
            if (distance > race.second) ways++
        }
        return ways
    }

    fun part1(input: List<String>): Long {
        val times = input[0].split(" ").map {
            it.trim()
        }.filter { it.isNotEmpty() }.drop(1).map { it.toLong() }
        val distances = input[1].split(" ").map {
            it.trim()
        }.filter { it.isNotEmpty() }.drop(1).map { it.toLong() }
        val races = times.mapIndexed { index, i ->
            i to distances[index]
        }
        var sum = 1L
        races.forEach {
            sum *= solveRace(it)
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val time = input[0].split(" ").map {
            it.trim()
        }.filter { it.isNotEmpty() }.drop(1).joinToString("").toLong()
        val distance = input[1].split(" ").map {
            it.trim()
        }.filter { it.isNotEmpty() }.drop(1).joinToString("").toLong()
        return solveRace(time to distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
