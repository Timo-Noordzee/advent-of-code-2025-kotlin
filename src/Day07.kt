fun main() {
    fun part1(input: List<String>): Int {
        val m = input.size
        val n = input[0].length
        val start = input.first().indexOfFirst { it == 'S' }
        val visited = Array(m) { BooleanArray(n) }
        val queue = ArrayDeque<IntArray>()
        queue.add(intArrayOf(0, start))

        var splitCount = 0
        while (queue.isNotEmpty()) {
            val (i, j) = queue.removeFirst()
            if (visited[i][j]) {
                continue
            }

            visited[i][j] = true

            if (input[i][j] == '^') {
                splitCount++
                queue.add(intArrayOf(i + 1, j - 1))
                queue.add(intArrayOf(i + 1, j + 1))
            } else if (i + 1 < m) {
                queue.add(intArrayOf(i + 1, j))
            }
        }

        return splitCount
    }

    fun part2(input: List<String>): Long {
        val m = input.size
        val n = input[0].length
        val start = input.first().indexOfFirst { it == 'S' }
        val timelines = Array(m) { LongArray(n) }
        val visited = Array(m) { BooleanArray(n) }
        val queue = ArrayDeque<IntArray>()

        queue.add(intArrayOf(0, start))
        timelines[0][start] = 1

        var totalTimelines = 0L
        while (queue.isNotEmpty()) {
            val (i, j) = queue.removeFirst()
            if (visited[i][j]) {
                continue
            }

            visited[i][j] = true

            if (input[i][j] == '^') {
                // When splitting the beam, the current number of timelines is added to both the left and right side
                timelines[i + 1][j - 1] += timelines[i][j]
                timelines[i + 1][j + 1] += timelines[i][j]

                // Split the beam to the left and right
                queue.add(intArrayOf(i + 1, j - 1))
                queue.add(intArrayOf(i + 1, j + 1))
            } else if (i + 1 < m) {
                // The beam doesn't collide with a splitter, continue to move it downwards
                timelines[i + 1][j] += timelines[i][j]
                queue.add(intArrayOf(i + 1, j))
            } else if (i + 1 == m) {
                // After reaching the end, add the current number of timelines to the total
                totalTimelines += timelines[i][j]
            }
        }

        return totalTimelines
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
