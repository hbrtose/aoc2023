import kotlin.math.min

fun main() {

    fun getMappers(input: List<String>): List<Mapper> {
        val inputString = input.subList(2, input.lastIndex).joinToString()
        return inputString.split(", ,").map { Mapper.fromString(it) }
    }

    fun calculateLocation(seed: Long, maps: List<Mapper>): Long {
        var currentMap = "seed"
        var value = seed
        while (currentMap != "location") {
            maps.find { map -> map.from == currentMap }?.let { mapper ->
                value = mapper.map(value)
                currentMap = mapper.to
            }
        }
        return value
    }

    fun getLowestLocation(seeds: List<Long>, maps: List<Mapper>): Long {
        return seeds.minOf { calculateLocation(it, maps) }
    }

    fun part1(input: List<String>): Long {
        val seeds = mutableListOf<Long>()
        input.forEach { line ->
            if (line.startsWith("seeds")) {
                val seedItems = line.split(" ")
                seedItems.forEachIndexed { index, item ->
                    if (index != 0) seeds.add(item.toLong())
                }
            }
        }
        val maps = getMappers(input)
        return getLowestLocation(seeds, maps)
    }

    fun getNumberOfPointsInRange(number: Int, range: LongRange): List<Long> {
        val size = range.last - range.first
        val subrange = size / number
        val res = mutableListOf<Long>()
        for (i in 0..number) {
            res.add(range.first + (i*subrange))
        }
        return res
    }

    fun searchRanges(ranges: List<LongRange>, mappers: List<Mapper>): Long {
        return ranges.minBy { getLowestLocation(getNumberOfPointsInRange(100, it), mappers) }.let {
            val size = it.last - it.first
            println(calculateLocation(it.first, mappers).toString() + " - " + calculateLocation(it.last, mappers))
            if (size < 200L) {
                getLowestLocation(it.toList(), mappers)
            } else {
                searchRanges(listOf(it.first..<it.first+(size/2), it.first+(size/2)..it.last), mappers)
            }
        }
    }


    fun part2(input: List<String>): Long {
        var ranges = listOf<LongRange>()
        input.forEach { line ->
            if (line.startsWith("seeds")) {
                val splitLine = line.split(" ")
                ranges = splitLine
                    .subList(1, splitLine.size)
                    .map {
                        it.trim()
                        it.toLong()
                    }.let {
                        val rangeStarts = mutableListOf<Long>()
                        val rangeSizes = mutableListOf<Long>()
                        it.forEachIndexed { index, l ->
                            if (index % 2 == 0) {
                                rangeStarts.add(l)
                            } else {
                                rangeSizes.add(l)
                            }
                        }
                        val rangeEnds = rangeSizes.mapIndexed { index, l ->
                            l+rangeStarts[index] - 1
                        }
                        rangeStarts.mapIndexed { index, l ->
                            l..<rangeEnds[index]
                        }
                    }
            }
        }
        val maps = getMappers(input)
        return searchRanges(ranges, maps)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

data class Mapper(
    val from: String,
    val to: String,
    val ranges: List<Pair<LongRange, LongRange>>,
) {
    companion object {
        fun fromString(string: String): Mapper {
            val parts = string.split(",").map { it.trim() }
            var titleParts = listOf<String>()
            val ranges = mutableListOf<Pair<LongRange, LongRange>>()
            parts.forEachIndexed { index, part ->
                if (index == 0) {
                    val title = part.removeSuffix(" map:")
                    titleParts = title.split("-")
                } else {
                    val numbers = part.split(" ").map {
                        it.trim()
                        it.toLong()
                    }
                    val destRange = numbers[0]..<numbers[0]+numbers[2]
                    val sourceRange = numbers[1]..<numbers[1]+numbers[2]
                    ranges.add(sourceRange to destRange)
                }
            }
            return Mapper(
                from = titleParts[0],
                to = titleParts[2],
                ranges = ranges
            )
        }
    }

    fun map(number: Long): Long {
        return ranges.find {
            number in it.first
        }?.let {
            val offset = number - it.first.first
            it.second.first + offset
        } ?: number
    }
}
