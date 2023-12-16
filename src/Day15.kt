fun main() {

    fun hash(s: String): Int {
        var current = 0
        s.toCharArray().forEach {
            current += it.code
            current *= 17
            current %= 256
        }
        return current
    }

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { hash(it) }
    }

    fun part2(input: List<String>): Int {
        val boxes = hashMapOf<Int, LinkedHashMap<String, Int>>()
        var sum = 0
        input.first().split(",").forEach { command ->
            if (command.split("=").size == 2) {
                val lens = command.split("=")
                val box = hash(lens[0])
                boxes.getOrPut(box){ linkedMapOf() }[lens[0]] = lens[1].toInt()
            } else {
                val label = command.dropLast(1)
                val box = hash(label)
                boxes[box]?.remove(label)
            }
        }
        boxes.forEach { box ->
            box.value.onEachIndexed { index, entry ->
                sum += (box.key + 1) * (index + 1) * entry.value
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
