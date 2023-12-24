import java.math.BigInteger
import java.security.MessageDigest
import java.util.LinkedList
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> List<T>.replaceAt(newValue: T, index: Int): List<T> {
    return mapIndexed { i, t ->
        if (index == i) newValue else t
    }
}

fun String.swapCharacters(i: Int, j: Int): String {
    var chars = this.toCharArray()
    val temp = chars[i]
    chars[i] = chars[j]
    chars[j] = temp
    return chars.joinToString("")
}

fun transposeStrings(list: List<String>): List<String> {
    return (0..list[0].lastIndex).map { i ->
        list.map { it[i] }
    }.map { it.joinToString("") }
}

operator fun Pair<Int, Int>.plus(p: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + p.first to this.second + p.second
}

fun <T> nTimes(f: (T) -> T, init: T, n: Long): T {
    val repeats = mutableListOf<T>()
    var current = init
    var i = 0L
    while (!repeats.contains(current)) {
        repeats.add(current)
        current = f(current)
        if (i == n - 1) {
            i++
            return current
        }
    }
    val offset = repeats.indexOf(current)
    val repLoop = repeats.size - offset
    val skip = (n - offset) % repLoop
    return (1..skip).fold(current) { value, _ ->
        f(value)
    }
}

fun lcm(a: Long, b: Long): Long {
    val larger = max(a, b)
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun <T> LinkedList<T>.copy(): LinkedList<T> {
    val l = LinkedList<T>()
    forEach {
        l.offer(it)
    }
    return l
}

fun <T> List<T>.combinations(size: Int): List<List<T>> = when (size) {
    0 -> listOf(listOf())
    else -> flatMapIndexed { idx, element -> drop(idx + 1).combinations(size - 1).map { listOf(element) + it } }
}

data class Point(val x: Int, val y: Int) {
    companion object {
        val UP = Point(0, -1)
        val RIGHT = Point(1, 0)
        val DOWN = Point(0, 1)
        val LEFT = Point(-1, 0)
    }

    fun nearby4(): List<Point> {
        return listOf(this + UP, this + DOWN, this + RIGHT, this + LEFT)
    }

    fun containedIn(list: List<String>): Boolean {
        return y in list.indices && x in list.first().indices
    }

    fun <T> containedInArrays(list: List<List<T>>): Boolean {
        return y in list.indices && x in list.first().indices
    }

    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)

    operator fun times(i: Int): Point = Point(x * i, y * i)
}
