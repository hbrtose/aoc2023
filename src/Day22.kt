fun main() {

    fun getStack(bricks: List<Brick>): List<Brick> {
        val stacked = mutableListOf<Brick>()
        bricks.forEach { b ->
            var curr = b
            var supported = false
            while (!supported) {
                val supporting = stacked.filter {
                    it.x.intersect(curr.x).isNotEmpty() && it.y.intersect(curr.y).isNotEmpty() && it.z.last == curr.z.first - 1
                }
                supported = supporting.isNotEmpty() || curr.z.first == 0
                if (supported) {
                    supporting.forEach {
                        it.supports.add(curr)
                        curr.supportedBy.add(it)
                    }
                    stacked.add(curr)
                } else {
                    val next = stacked
                        .filter { it.z.last < curr.z.first - 1 }
                        .ifEmpty { null }
                        ?.maxBy { it.z.last }?.z?.last?.let { it + 1 } ?: 0
                    val height = curr.z.last - curr.z.first
                    curr = Brick(curr.x, curr.y, next..(next+height))
                }
            }
        }
        return stacked
    }

    fun part1(input: List<String>): Int {
        val bricks = input.map { Brick.fromLine(it) }.sortedBy { it.z.first }
        return getStack(bricks).filter { b -> !b.supports.any { it.supportedBy.size == 1 } }.size
    }

    fun part2(input: List<String>): Int {
        val bricks = input.map { Brick.fromLine(it) }.sortedBy { it.z.first }
        val stack = getStack(bricks)
        val chains = stack.filter { b -> b.supports.any { it.supportedBy.size == 1 } }
        return chains.sumOf { b ->
            val chain = stack.toMutableList()
            chain.remove(b)
            val gone = mutableListOf(b)
            var unsupported = chain.filter { it.supportedBy.isNotEmpty() && it.supportedBy.all { support -> gone.contains(support) } }
            while (unsupported.isNotEmpty()) {
                chain.removeAll(unsupported)
                gone.addAll(unsupported)
                unsupported = chain.filter { it.supportedBy.isNotEmpty() && it.supportedBy.all { support -> gone.contains(support) } }
            }
            gone.size - 1
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}

data class Brick(val x: IntRange, val y: IntRange, var z: IntRange) {

    companion object {
        fun fromLine(line: String): Brick {
            val parts = line.split("~").map { it.trim() }
            val parts1 = parts[0].split(",").map { it.trim() }.map { it.toInt() }
            val parts2 = parts[1].split(",").map { it.trim() }.map { it.toInt() }
            return Brick(parts1[0]..parts2[0], parts1[1]..parts2[1], parts1[2]..parts2[2])
        }
    }

    val supports = mutableListOf<Brick>()
    val supportedBy = mutableListOf<Brick>()
}
