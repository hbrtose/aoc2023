fun main() {

    fun matchLine(s: String, startIndex: Int, endIndex: Int, regex: Regex): Boolean {
        val start = if (startIndex > 0) startIndex - 1 else 0
        val end = if (endIndex < s.length - 1) endIndex + 1 else s.length
        val testLine = s.substring(start, end)
        return regex.containsMatchIn(testLine)
    }

    fun getNumbersAround(s: String, index: Int, regex: Regex): List<Int> {
        val list = mutableListOf<Int>()
        var indexOfLast = 0
        regex.findAll(s).map { it.value }.forEach { number ->
            val startIndex = s.indexOf(number, indexOfLast)
            val endIndex = startIndex + number.length
            val start = if (startIndex > 0) startIndex - 1 else 0
            val end = endIndex
            indexOfLast = endIndex
            if (index in start..end) list.add(number.toInt())
        }
        return list
    }

    fun part1(input: List<String>): Int {
        val symbolsRegex = Regex("^(?=.*[!@#\$%^&*\"\\\\[\\\\]\\\\{\\\\}<>/\\\\(\\\\)=\\\\\\\\\\\\-_´+`~\\\\:;,\\-€\\\\|])")
        val numbersRegex = Regex("\\d+")
        var sum = 0
        input.forEachIndexed { index, s ->
            var indexOfLast = 0
            numbersRegex.findAll(s).map { it.value }.forEach { number ->
                var hasSymbolsAround = false
                val startIndex = s.indexOf(number, indexOfLast)
                val endIndex = startIndex + number.length
                indexOfLast = endIndex
                if (index > 0) {
                    hasSymbolsAround = hasSymbolsAround || matchLine(input[index - 1], startIndex, endIndex, symbolsRegex)
                }
                hasSymbolsAround = hasSymbolsAround || matchLine(s, startIndex, endIndex, symbolsRegex)
                if (index < input.size - 1) {
                    hasSymbolsAround = hasSymbolsAround || matchLine(input[index + 1], startIndex, endIndex, symbolsRegex)
                }
                if (hasSymbolsAround) sum += number.toInt()
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val starRegex = Regex("\\*")
        val numbersRegex = Regex("\\d+")
        var sum = 0
        input.forEachIndexed { index, s ->
            var indexOfLast = 0
            starRegex.findAll(s).map { it.value }.forEach { star ->
                val indexOfStar = s.indexOf(star, indexOfLast)
                indexOfLast = indexOfStar+1
                val numbersAround = mutableListOf<Int>()
                if (index > 0) {
                    numbersAround.addAll(getNumbersAround(input[index - 1], indexOfStar, numbersRegex))
                }
                numbersAround.addAll(getNumbersAround(s, indexOfStar, numbersRegex))
                if (index < input.size - 1) {
                    numbersAround.addAll(getNumbersAround(input[index + 1], indexOfStar, numbersRegex))
                }
                if (numbersAround.size == 2) sum += (numbersAround[0] * numbersAround[1])
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
