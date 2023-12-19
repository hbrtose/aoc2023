fun main() {

    fun List<Condition>.pass(item: XmasItem): String {
        forEach {
            if (it.key == null) {
                return it.next
            } else {
                if (it.greater) {
                    if ((item.items[it.key] ?: 0) > (it.than ?: 0)) {
                        return it.next
                    }
                } else {
                    if ((item.items[it.key] ?: 0) < (it.than ?: 0)) {
                        return it.next
                    }
                }
            }
        }
        return "R"
    }

    fun part1(input: List<String>): Int {
        val items = mutableListOf<XmasItem>()
        val conditions = hashMapOf<String, List<Condition>>()
        input.forEach {
            if(it.startsWith("{")) {
                items.add(XmasItem.fromString(it))
            } else if (it.isNotEmpty()) {
                val parts = it.split("{")
                conditions[parts[0]] = parts[1].dropLast(1).split(",").map { c ->
                    Condition.fromString(c)
                }
            }
        }
        return items.filter {
            var next = "in"
            while (next != "A" && next != "R") {
                next = conditions[next]!!.pass(it)
            }
            next == "A"
        }.sumOf { it.items.values.sum() }
    }

    fun range(flow: String, map: MutableMap<String, IntRange>, conditions: Map<String, List<Condition>>): Long {
        var sum = 0L
        if (flow == "R") {
            return 0
        }

        if (flow == "A") {
            var p = 1L
            map.values.forEach {
                p *= it.last - it.first + 1
            }
            return p
        }

        conditions[flow]?.forEach {
            if (it.key == null) {
                sum += range(it.next, map, conditions)
            } else {
                val new = map.toMutableMap()
                val range = map[it.key]!!
                if (it.than in range) {
                    if (it.greater) {
                        new[it.key] = it.than!! + 1..range.last
                        map[it.key] = range.first..it.than
                    } else {
                        new[it.key] = range.first..it.than!!-1
                        map[it.key] = it.than..range.last
                    }
                    sum += range(it.next, new, conditions)
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val conditions = hashMapOf<String, List<Condition>>()
        input.forEach {
            if(it.startsWith("{")) {
                //items.add(XmasItem.fromString(it))
            } else if (it.isNotEmpty()) {
                val parts = it.split("{")
                conditions[parts[0]] = parts[1].dropLast(1).split(",").map { c ->
                    Condition.fromString(c)
                }
            }
        }
        return range("in", hashMapOf("x" to 1..4000, "m" to 1..4000, "a" to 1..4000, "s" to 1..4000), conditions)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

data class XmasItem(val items: Map<String, Int>) {
    companion object {
        fun fromString(s: String): XmasItem {
            val its = s.drop(1).dropLast(1).split(",")
            val hashmap = hashMapOf<String, Int>()
            its.forEach {
                val parts = it.split("=")
                hashmap[parts[0]] = parts[1].toInt()
            }
            return XmasItem(hashmap)
        }
    }
}

data class Condition(val key: String? = null, val greater: Boolean = false, val than: Int? = null, val next: String) {
    companion object {
        fun fromString(s: String): Condition {
            val conAndNext = s.split(":")
            if (conAndNext.size == 1) {
                return Condition(next = conAndNext[0])
            } else {
                val k = conAndNext[0][0].toString()
                val g = conAndNext[0][1] == '>'
                val t = conAndNext[0].drop(2).toInt()
                return Condition(k, g, t, conAndNext[1])
            }
        }
    }
}
