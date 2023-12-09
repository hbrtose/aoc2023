fun main() {

    fun extrapolate(nums: List<Int>): Int {
        var finalDerivative = true
        val derivatives = mutableListOf<Int>()
        nums.forEachIndexed { index, l ->
            if (index != 0) {
                derivatives.add(l - nums[index - 1])
                finalDerivative = finalDerivative && derivatives.last() == 0
            }
        }
        return if (finalDerivative) {
            nums.last()
        } else {
            val next = extrapolate(derivatives)
            return nums.last() + next
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val numbers = line.split(" ").map { it.toInt() }
            extrapolate(numbers)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val numbers = line.split(" ").map { it.toInt() }.reversed()
            extrapolate(numbers)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
