import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap

fun main() {

    fun getModules(input: List<String>): Map<String, Module> {
        val modules = hashMapOf<String, Module>()
        input.forEach {
            val nameAndConnections = it.split("->")
            val name = nameAndConnections[0].trim()
            val connections = nameAndConnections[1].split(",").map { it.trim() }
            if (name.startsWith("%")) {
                modules[name.drop(1)] = Module(name.drop(1), ModuleType.FLIPFLOP, false, connections, mutableMapOf())
            } else if (name.startsWith("&")) {
                modules[name.drop(1)] = Module(name.drop(1), ModuleType.CONJUNCTION, false, connections, mutableMapOf())
            } else if (name.startsWith("b")) {
                modules[name] = Module(name, ModuleType.BROADCASTER, false, connections, mutableMapOf())
            }
        }
        modules["output"] = Module("output", ModuleType.SINK, false, emptyList(), mutableMapOf())
        modules["rx"] = Module("rx", ModuleType.SINK, false, emptyList(), mutableMapOf())
        return modules
    }

    fun setUpInputs(modules: Map<String, Module>) {
        for (m in modules.values) {
            for (n in m.next) {
                modules[n]!!.inputs[m.name] = false
            }
        }
    }

    fun part1(input: List<String>): Long {
        var highs = 0L
        var lows = 0L
        val modules = getModules(input)
        setUpInputs(modules)
        for (i in 1..1000) {
            val ins = LinkedList<ModuleState>()
            ins.offer(ModuleState("button", "broadcaster", false))
            while (ins.isNotEmpty()) {
                val current = ins.poll()
                if (current.high) highs++ else lows++
                val currentMod = modules[current.to]!!
                when (currentMod.type) {
                    ModuleType.FLIPFLOP -> {
                        if (!current.high) {
                            currentMod.state = !currentMod.state
                            for (n in currentMod.next) {
                                ins.offer(ModuleState(currentMod.name, n, currentMod.state))
                            }
                        }
                    }
                    ModuleType.CONJUNCTION -> {
                        currentMod.inputs[current.from] = current.high
                        val output = currentMod.inputs.values.any { !it }
                        for (n in currentMod.next) {
                            ins.offer(ModuleState(currentMod.name, n, output))
                        }
                    }
                    ModuleType.BROADCASTER -> {
                        for (n in currentMod.next) {
                            ins.offer(ModuleState(currentMod.name, n, current.high))
                        }
                    }
                    ModuleType.SINK -> {}
                }
            }
        }
        return highs*lows
    }

    fun filter(modules: Map<String, Module>, s: String): Map<String, Module> {
        val broadcaster = modules["broadcaster"]!!
        val new = Module(broadcaster.name, ModuleType.BROADCASTER, false, listOf(s), mutableMapOf())
        val res = mutableSetOf(new)
        var count = 0
        while (count != res.size) {
            count = res.size
            for (m in res.toList()) {
                for (n in m.next) {
                    res.add(modules[n]!!)
                }
            }
        }
        return res.associateBy { it.name }
    }

    fun part2(input: List<String>): Long {
        val modules = getModules(input)
        val lcms = mutableListOf<Long>()
        for (n in modules["broadcaster"]!!.next.toList()) {
            val mods = filter(modules, n)
            for (m in mods) {
                m.value.inputs.clear()
            }
            setUpInputs(mods)

            var count = 0L
            var ready = false
            while (!ready) {
                count++
                val ins = LinkedList<ModuleState>()
                ins.offer(ModuleState("button", "broadcaster", false))
                var rx = false
                while (ins.isNotEmpty()) {
                    val current = ins.poll()
                    if (current.to == "rx" && !current.high) {
                        rx = true
                    }
                    val currentMod = mods[current.to]!!
                    when (currentMod.type) {
                        ModuleType.FLIPFLOP -> {
                            if (!current.high) {
                                currentMod.state = !currentMod.state
                                for (n in currentMod.next) {
                                    ins.offer(ModuleState(currentMod.name, n, currentMod.state))
                                }
                            }
                        }

                        ModuleType.CONJUNCTION -> {
                            currentMod.inputs[current.from] = current.high
                            val output = currentMod.inputs.values.any { !it }
                            for (n in currentMod.next) {
                                ins.offer(ModuleState(currentMod.name, n, output))
                            }
                        }

                        ModuleType.BROADCASTER -> {
                            for (n in currentMod.next) {
                                ins.offer(ModuleState(currentMod.name, n, current.high))
                            }
                        }

                        ModuleType.SINK -> {}
                    }
                }
                if (rx) {
                    ready = true
                    lcms.add(count)
                }
            }
        }
        return lcms.reduce(::lcm)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 32000000L)
    //check(part2(testInput) == 6)

    val testInput2 = readInput("Day20_test2")
    check(part1(testInput2) == 11687500L)
    //check(part2(testInput2) == 6)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}

data class Module(
    val name: String,
    val type: ModuleType,
    var state: Boolean,
    val next: List<String>,
    val inputs: MutableMap<String, Boolean>
)

enum class ModuleType {
    FLIPFLOP, CONJUNCTION, BROADCASTER, SINK
}

data class ModuleState(val from: String, val to: String, val high: Boolean)
