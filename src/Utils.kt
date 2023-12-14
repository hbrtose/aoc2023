import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

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
