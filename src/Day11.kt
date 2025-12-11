fun main() {
    fun buildGraph(input: List<String>): Map<String, MutableList<String>> {
        val graph = mutableMapOf<String, MutableList<String>>()
        input.forEach { deviceWithOutputs ->
            val device = deviceWithOutputs.substringBefore(':')
            val outputs = deviceWithOutputs.substringAfter(' ').split(' ')
            graph.getOrPut(device) { mutableListOf() }.addAll(outputs)
        }
        return graph
    }

    fun countPaths(graph: Map<String, MutableList<String>>, start: String, target: String): Long {
        val memo = mutableMapOf<String, Long>()
        fun traverse(current: String): Long = when {
            current == target -> 1L
            memo.containsKey(current) -> memo.getValue(current)
            else -> graph.getOrElse(current) { emptyList() }.sumOf(::traverse).also { memo[current] = it }
        }
        return traverse(start)
    }

    fun part1(input: List<String>): Long {
        val graph = buildGraph(input)
        return countPaths(graph, "you", "out")
    }


    fun part2(input: List<String>): Long {
        val graph = buildGraph(input)
        // Order determined by visualizing graph using graphviz
        return listOf("svr" to "fft", "fft" to "dac", "dac" to "out")
            .map { (start, target) -> countPaths(graph, start, target) }
            .reduce(Long::times)
    }

    val testInput1 = readInput("Day11_test_1")
    check(part1(testInput1) == 5L)
    val testInput2 = readInput("Day11_test_2")
    check(part2(testInput2) == 2L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}