fun main() {
    fun part1(input: List<String>): Int {
        val regions = input.takeLastWhile { it.isNotEmpty() }
        val shapeAreas = input.dropLast(regions.size + 1).fold(mutableListOf(0)) { acc, line ->
            if (line.isEmpty()) {
                acc.add(0)
            }

            acc[acc.lastIndex] += line.count { it == '#' }
            acc
        }

        return regions.count { region ->
            val totalArea = region
                .substringBefore(':')
                .split('x')
                .map(String::toInt)
                .reduce(Int::times)

            val requiredArea = region.substringAfter(' ')
                .split(' ')
                .map(String::toInt)
                .withIndex()
                .sumOf { (shapeId, count) -> shapeAreas[shapeId] * count }

            requiredArea <= totalArea
        }
    }

    val input = readInput("Day12")
    part1(input).println()
}
