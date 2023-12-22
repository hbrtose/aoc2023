import kotlin.math.max

fun main() {

    fun part1(input: List<String>): Int {
        val route = input[0]
        val map = hashMapOf<String, Pair<String, String>>()
        input.drop(2).forEach {
            val split = it.split(" ")
            val key = split[0]
            val l = split[2].drop(1).dropLast(1)
            val r = split[3].dropLast(1)
            map[key] = l to r
        }
        var steps = 0
        var currentStep = "AAA"
        while (currentStep != "ZZZ") {
            val currentChoice = map[currentStep]
            currentStep = if (route[steps % route.length] == 'L') {
                currentChoice!!.first
            } else {
                currentChoice!!.second
            }
            steps++
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val route = input[0]
        val map = hashMapOf<String, Pair<String, String>>()
        input.drop(2).forEach {
            val split = it.split(" ")
            val key = split[0]
            val l = split[2].drop(1).dropLast(1)
            val r = split[3].dropLast(1)
            map[key] = l to r
        }
        val startingKeys = map.keys.filter { it.endsWith("A") }
        val routeLengths = startingKeys.map {
            var steps = 0
            var currentStep = it
            while (!currentStep.endsWith("Z")) {
                val currentChoice = map[currentStep]
                currentStep = if (route[steps % route.length] == 'L') {
                    currentChoice!!.first
                } else {
                    currentChoice!!.second
                }
                steps++
            }
            steps
        }
        return routeLengths.map { it.toLong() }.reduce(::lcm)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
