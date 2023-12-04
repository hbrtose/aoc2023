fun main() {
    fun part1(input: List<String>): Int {
        val games = input.map { parseInput(it) }
        return checkLimits(games, 12, 13, 14).sum()
    }

    fun part2(input: List<String>): Int {
        val games = input.map { parseInput(it) }
        return checkMins(games).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun parseInput(input: String): Game {
    val gameAndReveals = input.split(':')
    val id = gameAndReveals[0].split(' ')[1].toInt()
    val reveals = gameAndReveals[1].split(';')
    val revealsMaps = mutableListOf<Map<Color, Int>>()
    reveals.forEach { reveal ->
        val revealsMap = mutableMapOf<Color, Int>()
        val colorAndScore = reveal.split(',')
        colorAndScore.forEach { cs ->
            val amount = cs.trim().split(' ')
            revealsMap[Color.valueOf(amount[1].trim().toUpperCase())] = amount[0].trim().toInt()
        }
        revealsMaps.add(revealsMap)
    }
    return Game(id, revealsMaps)
}

fun checkLimits(games: List<Game>, redLimit: Int, greenLimit: Int, blueLimit: Int): List<Int> {
    val ids = mutableListOf<Int>()
    games.forEach { game ->
        val maxRed = game.reveals.maxOf { it[Color.RED] ?: 0 }
        val maxBlue = game.reveals.maxOf { it[Color.BLUE] ?: 0 }
        val maxGreen = game.reveals.maxOf { it[Color.GREEN] ?: 0 }
        if (maxRed <= redLimit && maxBlue <= blueLimit && maxGreen <= greenLimit) {
            ids.add(game.id)
        }
    }
    return ids
}

fun checkMins(games: List<Game>): List<Int> {
    val minMultis = mutableListOf<Int>()
    games.forEach { game ->
        val maxRed = game.reveals.maxOf { it[Color.RED] ?: 0 }
        val maxBlue = game.reveals.maxOf { it[Color.BLUE] ?: 0 }
        val maxGreen = game.reveals.maxOf { it[Color.GREEN] ?: 0 }
        minMultis.add(maxRed * maxGreen * maxBlue)
    }
    return minMultis
}


enum class Color {
    RED, GREEN, BLUE
}

data class Game(val id: Int, val reveals: List<Map<Color, Int>>)
