fun main() {

    fun getHands(input: List<String>, jokerify: Boolean): List<Hand> {
        return input.map { hand ->
            val parts = hand.split(" ")
            val bid = parts[1].toInt()
            val cards = parts[0].map {
                CamelCard.fromChar(it)
            }.filterNotNull()
            Hand(cards, bid, jokerify)
        }
    }

    fun calculateSum(hands: List<Hand>): Int {
        var sum = 0
        hands.sorted().forEachIndexed { index, hand ->
            sum += (hand.bid * (index + 1))
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        return calculateSum(getHands(input, false))
    }

    fun part2(input: List<String>): Int {
        return calculateSum(getHands(input, true))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}



data class Hand(
    val cards: List<CamelCard>,
    val bid: Int,
    val jokerify: Boolean = false
): Comparable<Hand> {

    fun getScore(): Score {
        val repeats = hashMapOf<Int, Int>()
        CamelCard.values().forEach {
            val repeat = cards.count { card -> card == it }
            val r = repeats[repeat]
            r?.let { repeats[repeat] = r + 1 } ?: repeats.put(repeat, 1)
        }
        val maxRepeat = repeats.maxBy { it.key }.key
        val score = when(maxRepeat) {
            5 -> Score.FIVE_OF_A_KIND
            4 -> Score.FOUR_OF_A_KIND
            3 -> if (repeats.contains(2)) Score.FULL_HOUSE else Score.THREE_OF_A_KIND
            2 -> if (repeats[2] == 2) Score.TWO_PAIR else Score.PAIR
            else -> Score.HIGH_CARD
        }
        return if (jokerify) {
            jokerify(score, cards)
        } else {
            score
        }
    }

    fun jokerify(score: Score, cards: List<CamelCard>): Score {
        return when (score) {
            Score.HIGH_CARD -> {
                when (cards.count { it == CamelCard.JACK }) {
                    1 -> Score.PAIR
                    else -> score
                }
            }

            Score.PAIR -> {
                when (cards.count { it == CamelCard.JACK }) {
                    1 -> Score.THREE_OF_A_KIND
                    2 -> Score.THREE_OF_A_KIND
                    else -> score
                }
            }

            Score.TWO_PAIR -> {
                when (cards.count { it == CamelCard.JACK }) {
                    1 -> Score.FULL_HOUSE
                    2 -> Score.FOUR_OF_A_KIND
                    else -> score
                }
            }

            Score.THREE_OF_A_KIND -> {
                when (cards.count { it == CamelCard.JACK }) {
                    1 -> Score.FOUR_OF_A_KIND
                    3 -> Score.FOUR_OF_A_KIND
                    else -> score
                }
            }

            Score.FULL_HOUSE -> {
                when (cards.count { it == CamelCard.JACK }) {
                    2 -> Score.FIVE_OF_A_KIND
                    3 -> Score.FIVE_OF_A_KIND
                    else -> score
                }
            }

            Score.FOUR_OF_A_KIND -> {
                when (cards.count { it == CamelCard.JACK }) {
                    1 -> Score.FIVE_OF_A_KIND
                    4 -> Score.FIVE_OF_A_KIND
                    else -> score
                }
            }

            Score.FIVE_OF_A_KIND -> Score.FIVE_OF_A_KIND
        }
    }

    override fun compareTo(other: Hand): Int {
        if (getScore() > other.getScore()) {
            return 1
        } else if (getScore() < other.getScore()) {
            return -1
        } else {
            cards.forEachIndexed { index, camelCard ->
                var ourCard = if (jokerify && camelCard == CamelCard.JACK) CamelCard.ONE else camelCard
                var theirCard = if (jokerify && other.cards[index] == CamelCard.JACK) CamelCard.ONE else other.cards[index]
                if (ourCard > theirCard) {
                    return 1
                } else if (ourCard < theirCard) {
                    return -1
                }
            }
        }
        return 0
    }
}

enum class Score {
    HIGH_CARD,
    PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND;
}

enum class CamelCard(private val face: Char) {
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun fromChar(c: Char): CamelCard? {
            CamelCard.values().forEach {
                if (c == it.face) {
                    return it
                }
            }
            return null
        }
    }
}
