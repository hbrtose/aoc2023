fun main() {

    fun part1(input: List<String>): Int {
        val cards = input.map { inp ->
            val splitId = inp.split(":")
            val id = splitId[0].split(" ").filter { !it.isNullOrEmpty() }[1].trim().toInt()
            val numbers = splitId[1].split("|")
            val scratched = numbers[0].split(" ").filter { !it.isNullOrEmpty() } .map { it.trim().toInt() }
            val winning = numbers[1].split(" ").filter { !it.isNullOrEmpty() } .map { it.trim().toInt() }
            Card(id, winning, scratched)
        }
        return cards.sumOf { it.value() }
    }

    fun process(card: Card, cards: List<Card>, copies: MutableList<Card>) {
        copies.add(card)
        val score = card.numberOfMatching()
        for (i in card.id+1..card.id+score) {
            if (i <= cards.size) {
                val cardToCopy = cards.find { it.id == i }?.copy()
                cardToCopy?.let { process(it, cards, copies) }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { inp ->
            val splitId = inp.split(":")
            val id = splitId[0].split(" ").filter { !it.isNullOrEmpty() }[1].trim().toInt()
            val numbers = splitId[1].split("|")
            val scratched = numbers[0].split(" ").filter { !it.isNullOrEmpty() } .map { it.trim().toInt() }
            val winning = numbers[1].split(" ").filter { !it.isNullOrEmpty() } .map { it.trim().toInt() }
            Card(id, winning, scratched)
        }.toMutableList()
        val copies = mutableListOf<Card>()
        cards.forEach { card ->
            process(card, cards, copies)
        }
        return copies.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

data class Card(
    val id: Int,
    val scratched: List<Int>,
    val winning: List<Int>,
) {
    fun value(): Int {
        var score = 0
        winning.intersect(scratched).forEach { _ ->
            if (score == 0) {
                score = 1
            } else {
                score *= 2
            }
        }
        return score
    }

    fun numberOfMatching() = winning.intersect(scratched).size
}
