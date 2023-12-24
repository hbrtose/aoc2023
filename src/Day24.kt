import com.microsoft.z3.*

fun main() {

    fun willMeet(h: Hailstone, cx:Double, cy:Double): Boolean {
        return !((h.vx < 0 && h.x < cx) || (h.vx > 0 && h.x > cx) || (h.vy < 0 && h.y < cy) || (h.vy > 0 && h.y > cy))
    }

    fun intersect(h1: Hailstone, h2: Hailstone, from: Double, to: Double): Boolean {
        if (h1.xyAngle() == h2.xyAngle()) return false
        val cx = ((h2.xyAngle() * h2.x) - (h1.xyAngle() * h1.x) + h1.y - h2.y) / (h2.xyAngle() - h1.xyAngle())
        val cy = (h1.xyAngle() * (cx - h1.x)) + h1.y
        val valid = willMeet(h1, cx, cy) && willMeet(h2, cx, cy)

        return cx in from..to && cy in from..to && valid
    }

    fun getHailStones(input: List<String>): List<Hailstone> {
        return input.map { line ->
            val locsAndVs = line.split("@")
            val locs = locsAndVs[0].trim().split(",").map {
                it.trim().toLong()
            }
            val vs = locsAndVs[1].trim().split(",").map {
                it.trim().toInt()
            }
            Hailstone(locs[0], locs[1], locs[2], vs[0], vs[1], vs[2])
        }
    }

    fun part1(input: List<String>): Int {
        val hailstones = getHailStones(input)
        val from = if (input.size < 10) 7.0 else 200000000000000.0
        val to = if (input.size < 10) 27.0 else 400000000000000.0
        return hailstones.combinations(2).count { intersect(it[0], it[1], from, to) }
    }

    fun part2(input: List<String>): Long {
        val hailstones = getHailStones(input)
        var res = 0L
        with (Context(HashMap<String, String>())) {
            val solver = mkSolver()
            val (x, y, z) = listOf("x", "y", "z").map { mkIntConst(it) }
            val (vx, vy, vz) = listOf("vx", "vy", "vz").map { mkIntConst(it) }
            hailstones.forEachIndexed { i, h ->
                val t = mkIntConst("t$i")
                solver.add(mkEq(mkAdd(x, mkMul(vx, t)), mkAdd(mkInt(h.x), mkMul(mkInt(h.vx.toLong()), t))))
                solver.add(mkEq(mkAdd(y, mkMul(vy, t)), mkAdd(mkInt(h.y), mkMul(mkInt(h.vy.toLong()), t))))
                solver.add(mkEq(mkAdd(z, mkMul(vz, t)), mkAdd(mkInt(h.z), mkMul(mkInt(h.vz.toLong()), t))))
            }

            if (solver.check() == Status.SATISFIABLE) {
                res = solver.model.eval(mkAdd(x, mkAdd(y, z)), false).toString().toLong()
            }
        }
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 47L)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}

data class Hailstone(val x: Long, val y: Long, val z: Long, val vx: Int, val vy: Int, val vz: Int) {
    fun xyAngle(): Double {
        return vy.toDouble() / vx.toDouble()
    }
}
