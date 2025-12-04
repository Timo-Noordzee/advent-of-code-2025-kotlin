fun main() {
    fun solve(input: List<String>, repeating: Boolean): Int {
        val m = input.size
        val n = input[0].length
        val directions = intArrayOf(-1, -1, 0, 1, 1, 0, -1, 1, -1)
        val adjacentPapersCount = Array(m) { IntArray(n) }
        val queue = ArrayDeque<IntArray>()

        // Find all papers that are initially accessible and keep track of the amount of adjacent papers
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (input[i][j] == '@') {
                    adjacentPapersCount[i][j] = (0 until 8).count { d ->
                        val i = i + directions[d]
                        val j = j + directions[d + 1]
                        (i in 0 until m && j in 0 until n && input[i][j] == '@')
                    }

                    if (adjacentPapersCount[i][j] < 4) {
                        queue.add(intArrayOf(i, j))
                    }
                }
            }
        }

        if (!repeating) {
            return queue.size
        }

        // Perform BFS on all accessible papers by adding the newly accessible papers to the queue
        var accessiblePapersCount = 0
        while (queue.isNotEmpty()) {
            val (i, j) = queue.removeFirst()
            (0 until 8).forEach { d ->
                val i = i + directions[d]
                val j = j + directions[d + 1]
                if (i in 0 until m && j in 0 until n && input[i][j] == '@') {
                    // If the adjacent paper becomes available by removing the current paper add it to the queue
                    if (--adjacentPapersCount[i][j] == 3) {
                        queue.add(intArrayOf(i, j))
                    }
                }
            }
            accessiblePapersCount++
        }

        return accessiblePapersCount
    }

    fun part1(input: List<String>) = solve(input, false)

    fun part2(input: List<String>) = solve(input, true)

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 43)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
