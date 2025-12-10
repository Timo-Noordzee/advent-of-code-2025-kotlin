import com.microsoft.z3.Context
import com.microsoft.z3.IntNum

fun main() {

    fun parseButtons(line: String) = line
        .substringAfter(' ')
        .substringBeforeLast(' ')
        .split(' ')
        .map { it.removeSurrounding("(", ")") }
        .map { it.split(',').map(String::toInt) }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val target = line
                .substringBefore(' ')
                .removeSurrounding("[", "]")
                .foldIndexed(0) { index, acc, light ->
                    if (light == '#') acc or (1 shl index) else acc
                }

            val buttons = parseButtons(line).map { it.fold(0) { acc, button -> acc or (1 shl button) } }

            val queue = ArrayDeque<Int>(0)
            val seen = mutableSetOf<Int>()
            buttons.forEach { button ->
                if (seen.add(button)) {
                    queue.add(button)
                }
            }

            var totalPresses = 1
            while (queue.isNotEmpty()) {
                repeat(queue.size) {
                    val state = queue.removeFirst()
                    if (state == target) {
                        return@sumOf totalPresses
                    }

                    buttons.forEach { button ->
                        val nextState = state xor button
                        if (seen.add(nextState)) {
                            queue.add(nextState)
                        }
                    }
                }
                totalPresses++
            }

            error("no solution found")
        }
    }

    fun part2(input: List<String>) = input.sumOf { line ->
        Context().use { context ->
            val buttons = parseButtons(line).mapIndexed { index, it ->
                it to context.mkIntConst("$index")
            }

            val joltageLevels = line
                .substringAfterLast(' ')
                .removeSurrounding("{", "}")
                .split(',')
                .map(String::toInt)

            val buttonConstants = buttons.map { it.second }

            // Z3 solution based on Reddit/Advent of Code Slack
            with(context.mkOptimize()) {
                MkMinimize(context.mkAdd(*buttonConstants.toTypedArray()))
                joltageLevels.forEachIndexed { index, target ->
                    val matchingButtons = buttons.filter { index in it.first }.map { it.second }
                    Add(context.mkEq(context.mkAdd(*matchingButtons.toTypedArray()), context.mkInt(target)))
                }
                buttonConstants.forEach { Add(context.mkGe(it, context.mkInt(0))) }
                Check()
                model.decls.sumOf { (model.eval(it.apply(), true) as IntNum).int }
            }
        }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 33)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}