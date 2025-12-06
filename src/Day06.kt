fun main() {
    fun solve(numbers: List<List<Long>>, operators: List<Char>): Long {
        return operators.withIndex().sumOf { (i, operator) ->
            when (operator) {
                '+' -> numbers[i].sum()
                '*' -> numbers[i].reduce(Long::times)
                else -> error("unknown operator '$operator'")
            }
        }
    }

    fun part1(input: List<String>): Long {
        val regex = "\\s+".toRegex()
        val operators = input.last().filter { it != ' ' }.toList()
        val numbers = List(operators.size) { mutableListOf<Long>() }

        input.dropLast(1).forEach { line ->
            line.trim().split(regex).forEachIndexed { index, number ->
                numbers[index].add(number.toLong())
            }
        }

        return solve(numbers, operators)
    }

    fun part2(input: List<String>): Long {
        val numbers = mutableListOf<MutableList<Long>>()
        val operators = mutableListOf<Char>()
        val maxLength = input.maxOf { it.length }
        val numberBuilder = StringBuilder()
        for (j in 0 until maxLength) {
            for (i in input.indices) {
                when (val c = input[i].getOrElse(j) { ' ' }) {
                    ' ' -> {} // ignore
                    '*', '+' -> {
                        numbers.add(mutableListOf())
                        operators.add(c)
                    }
                    else -> numberBuilder.append(c)
                }
            }

            if (numberBuilder.isNotEmpty()) {
                numbers.last().add(numberBuilder.toString().toLong())
                numberBuilder.clear()
            }
        }

        return solve(numbers, operators)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 4277556L)
    check(part2(testInput) == 3263827L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
