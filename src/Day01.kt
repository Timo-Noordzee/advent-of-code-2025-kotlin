import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        var dial = 50
        return input.count { rotation ->
            val direction = rotation[0]
            val distance = rotation.drop(1).toInt()
            when (direction) {
                'L' -> dial = (dial - distance).mod(100)
                'R' -> dial = (dial + distance).mod(100)
            }

            dial == 0
        }
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        var dial = 50
        input.forEach { rotation ->
            val direction = rotation[0]
            val current = dial
            val distance = rotation.drop(1).toInt()
            when (direction) {
                'L' -> dial -= distance
                'R' -> dial += distance
            }

            if (dial >= 100) {
                answer += dial / 100
                dial %= 100
            } else if (dial <= 0) {
                answer += 1 + (dial.absoluteValue / 100)
                dial = dial.mod(100)

                // If the dial was already pointing at 0 while rotating to the left, one click too many is counted.
                if (current == 0) {
                    answer--
                }
            }
        }
        return answer
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
