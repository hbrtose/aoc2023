fun main() {

    fun compute(info: String, groups: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long {
        return cache.getOrPut(info to groups) {
            var res: Long
            if (info.isEmpty()) {
                res = if (groups.isEmpty()) 1 else 0
            } else if (info.startsWith(".")) {
                res = compute(info.drop(1), groups, cache)
            } else if (info.startsWith("?")) {
                res = compute(info.replaceFirst("?", "."), groups, cache) + compute(info.replaceFirst("?", "#"), groups, cache)
            } else {
                res = if (groups.isEmpty() || info.length < groups[0] || info.substring(0, groups[0]).contains(".")) {
                    0
                } else if (groups.size > 1) {
                    if (info.length < groups[0] + 1 || info[groups[0]] == '#') {
                        0
                    } else {
                        compute(info.substring(groups[0] + 1), groups.drop(1), cache)
                    }
                } else {
                    compute(info.substring(groups[0]), groups.drop(1), cache)
                }
            }
            res
        }
    }

    fun countArrangements(line: String, times: Int? = null): Long {
        val info = line.split(" ")[0]
        val groups = line.split(" ")[1].split(",").map { it.toInt() }
        val cache = hashMapOf<Pair<String, List<Int>>, Long>()
        return times?.let {
            val infoMulti = ("$info?").repeat(times).dropLast(1)
            val groupsMulti = mutableListOf<Int>()
            repeat(times) {
                groupsMulti.addAll(groups)
            }
            compute(infoMulti, groupsMulti, cache)
        } ?: compute(info, groups, cache)
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { countArrangements(it) }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { countArrangements(it, 5) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
