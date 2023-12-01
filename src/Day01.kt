fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val numbers = Regex("\\d+").findAll(it)
            "${numbers.first().value[0]}${numbers.last().value[numbers.last().value.length - 1]}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val newList = mutableListOf<String>()
        input.forEach {
            var newString: String = it
            newString = newString.replace("one", "o1ne")
            newString = newString.replace("two", "t2wo")
            newString = newString.replace("three", "t3hree")
            newString = newString.replace("four", "f4our")
            newString = newString.replace("five", "f5ive")
            newString = newString.replace("six", "s6ix")
            newString = newString.replace("seven", "s7even")
            newString = newString.replace("eight", "e8ight")
            newString = newString.replace("nine", "n9ine")
            newList.add(newString)
        }
        return part1(newList)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test2")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
