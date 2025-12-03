fun main() {
    fun solve(input: List<String>, maxLength: Int) = input.sumOf { batteryBank ->
        val n = batteryBank.length
        val stack = StringBuilder() // The StringBuilder is used as monotonic stack
        batteryBank.forEachIndexed { index, battery ->
            while (stack.isNotEmpty() && stack.last() < battery && (n - index + stack.length) > maxLength) {
                stack.setLength(stack.length - 1)
            }

            if (stack.length < maxLength) {
                stack.append(battery)
            }
        }

        stack.toString().toLong()
    }

    fun part1(input: List<String>) = solve(input, 2)

    fun part2(input: List<String>) = solve(input, 12)

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 357L)
    check(part2(testInput) == 3121910778619L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
