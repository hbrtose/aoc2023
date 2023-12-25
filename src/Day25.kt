import java.util.TreeSet

fun main() {

    fun getNodes(input: List<String>): Map<String, Node> {
        val nodes = hashMapOf<String, Node>()
        input.forEach {
            val nodeAndAdj = it.split(":")
            val adj = nodeAndAdj[1].trim().split(" ")
            val node = nodes.getOrPut(nodeAndAdj[0]) { Node(nodeAndAdj[0]) }
            adj.forEach { a ->
                val n = nodes.getOrPut(a) { Node(a) }
                node.adj.add(n)
                n.adj.add(node)
            }
        }
        return nodes
    }

    fun nodeDistance(n1: Node, n2: Node): Int {
        return compareValuesBy(n1, n2, { it.dist }, { it.name })
    }

    fun reset(nodes: Collection<Node>) {
        for (n in nodes) {
            n.dist = Int.MAX_VALUE
        }
    }

    fun getDist(heap: TreeSet<Node>, n: Node): Int {
        while (true) {
            heap.pollFirst()?.let {
                if (it == n) {
                    return it.dist
                }
                val new = it.dist + 1
                for (i in it.adj) {
                    if (new < i.dist) {
                        heap.remove(i)
                        i.dist = new
                        heap.add(i)
                    }
                }
            }
        }
    }

    fun getSize(n: Node): Int {
        if (n.dist == 0) {
            return 0
        }
        n.dist = 0
        return n.adj.sumOf { getSize(it) } + 1
    }

    fun part1(input: List<String>): Int {
        val nodes = getNodes(input)
        val dists = hashMapOf<Set<Node>, Int>()
        nodes.values.forEach { node ->
            node.adj.toList().forEach { n ->
                val p = setOf(n, node)
                if (dists.containsKey(p)) {
                    return@forEach
                }
                node.adj.remove(n)
                n.adj.remove(node)
                node.dist = 0
                val heap = TreeSet(::nodeDistance).apply { addAll(nodes.values) }
                val dist = getDist(heap, n)
                dists[p] = dist
                node.adj.add(n)
                n.adj.add(node)
                reset(nodes.values)
            }
        }
        val cut = dists.toList()
            .sortedByDescending { it.second }
            .slice(0..2)
            .map { it.first.toList() }
        for ((n1, n2) in cut) {
            n1.adj.remove(n2)
            n2.adj.remove(n1)
        }

        val g1 = getSize(cut[0][0])
        val g2 = getSize(cut[0][1])

        return g1 * g2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 54)

    val input = readInput("Day25")
    part1(input).println()
}

data class Node(val name: String) {
    var dist: Int = Int.MAX_VALUE
    val adj = mutableSetOf<Node>()
}
