fun main() {
    fun solve(input: List<String>, regex: Regex) = input.first()
        .split(',')
        .map { it.split('-').map(String::toLong) }
        .map { (start, end) -> start..end }
        .flatMap { productId -> productId.filter { regex.matches(it.toString()) } }
        .sum()

    fun part1(input: List<String>) = solve(input, "^(.+)\\1$".toRegex())

    fun part2(input: List<String>) = solve(input, "^(.+)\\1+$".toRegex())

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
