fun main() {
    fun parseRanges(input: List<String>) = input
        .takeWhile(String::isNotEmpty)
        .map { it.split('-').map(String::toLong) }
        .map { (start, end) -> start..end }
        .sortedBy { it.first }
        .fold(mutableListOf<LongRange>()) { acc, range ->
            // Merge overlapping ranges to avoid duplicates
            if (acc.isNotEmpty() && range.first <= acc.last().last) {
                val previous = acc.last()
                val end = previous.last.coerceAtLeast(range.last)
                acc[acc.lastIndex] = previous.first..end
            } else {
                acc.add(range)
            }

            acc
        }

    fun part1(input: List<String>): Int {
        val ranges = parseRanges(input)
        return input
            .takeLastWhile(String::isNotEmpty)
            .map(String::toLong)
            .count { productId -> ranges.any { productId in it } }
    }

    fun part2(input: List<String>) = parseRanges(input).sumOf { it.last - it.first + 1 }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 14L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
