fun main() {

    fun part1(input: List<String>): Int {
        val patterns = mutableListOf<Pattern>()
        var pattern = mutableListOf<String>()
        input.forEach {
            if (it.isEmpty()) {
                patterns.add(Pattern(pattern))
                pattern = mutableListOf()
            } else {
                pattern.add(it)
            }
        }
        patterns.add(Pattern(pattern))
        return patterns.sumOf { it.lines() }
    }

    fun part2(input: List<String>): Int {
        val patterns = mutableListOf<Pattern>()
        var pattern = mutableListOf<String>()
        input.forEach {
            if (it.isEmpty()) {
                patterns.add(Pattern(pattern))
                pattern = mutableListOf()
            } else {
                pattern.add(it)
            }
        }
        patterns.add(Pattern(pattern))
        var count = 0
        patterns.forEach break1@ { pattern ->
            var found = false
            val vert = pattern.verticalLines()
            val hor = if (vert == 0) {
                pattern.horizontalLines()
            } else {
                0
            }
            pattern.pattern.forEachIndexed break2@ { index1, s ->
                s.forEachIndexed { index2, c ->
                    val newline = StringBuilder(s)
                    newline.setCharAt(index2, if (c == '.') '#' else '.')
                    val newPattern = Pattern(pattern.pattern.replaceAt(newline.toString(), index1))
                    var res = 0
                    res = newPattern.verticalLines(vert)
                    if (res == 0) {
                        res = newPattern.horizontalLines(hor)
                        if (res != 0) {
                            count += 100 * res
                            found = true
                            return@break1
                        }
                    } else if (res != vert) {
                        count += res
                        return@break1
                    }
                }
                if (found) return@break1
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

data class Pattern(val pattern: List<String>) {
    fun horizontalLines(comp: Int? = null): Int {
        for (i in 1..pattern.lastIndex) {
            var hasSymmetry = true
            for (j in 1..<i+1) {
                if (i + j - 1 >= pattern.size) {
                    break
                }
                if (pattern[i+j-1] != pattern[i-j]) {
                    hasSymmetry = false
                    break
                }
            }
            if (hasSymmetry && (comp == null || i != comp)) {
                return i
            }
        }
        return 0
    }

    fun verticalLines(comp: Int? = null): Int {
        for (i in 1..pattern[0].lastIndex) {
            var hasSymmetry = true
            for (j in 1..<i+1) {
                if (i + j - 1 >= pattern[0].length) {
                    break
                }
                if (vertString(i+j-1) != vertString(i-j)) {
                    hasSymmetry = false
                    break
                }
            }
            if (hasSymmetry && (comp == null || i != comp)) {
                return i
            }
        }
        return 0
    }

    fun lines(comp: Int? = null): Int {
        val verts = verticalLines()
        var hors = horizontalLines()
        return if (hors >= verts) {
            hors * 100
        } else {
            verts
        }
    }

    private fun vertString(i: Int): String {
        return pattern.map { it[i] }.joinToString("")
    }
}
