import util.UnionFind
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

private data class JunctionBox(val id: Int, val x: Double, val y: Double, val z: Double) {

    fun distanceTo(other: JunctionBox): Double {
        return sqrt((x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2))
    }
}

fun main() {
    fun part1(input: List<String>, count: Int): Int {
        val n = input.size
        val junctionBoxes = input.mapIndexed { index, position ->
            val (x, y, z) = position.split(',').map(String::toDouble)
            JunctionBox(index, x, y, z)
        }

        val closestPairs = PriorityQueue<Triple<Double, JunctionBox, JunctionBox>>(compareByDescending { it.first })
        for (i in 0 until n) {
            val a = junctionBoxes[i]
            for (j in i + 1 until n) {
                val b = junctionBoxes[j]
                val distance = a.distanceTo(b)
                closestPairs.add(Triple(distance, a, b))
                if (closestPairs.size > count) {
                    closestPairs.remove()
                }
            }
        }

        val dsu = UnionFind(n)
        while (closestPairs.isNotEmpty()) {
            val (_, a, b) = closestPairs.remove()
            dsu.union(a.id, b.id)
        }

        val seen = mutableSetOf<Int>()
        val largestCircuits = PriorityQueue<Int>()
        for (i in 0 until n) {
            val circuit = dsu.find(i)
            if (seen.add(circuit)) {
                largestCircuits.add(dsu.getComponentSize(i))
                if (largestCircuits.size > 3) {
                    largestCircuits.remove()
                }
            }
        }

        return largestCircuits.reduce(Int::times)
    }

    fun part2(input: List<String>): Long {
        val n = input.size
        val boxes = input.mapIndexed { index, position ->
            val (x, y, z) = position.split(',').map(String::toDouble)
            JunctionBox(index, x, y, z)
        }

        val closestPairs = PriorityQueue<Triple<Double, JunctionBox, JunctionBox>>(compareBy { it.first })
        for (i in 0 until n) {
            val a = boxes[i]
            for (j in i + 1 until n) {
                val b = boxes[j]
                val distance = a.distanceTo(b)
                closestPairs.add(Triple(distance, a, b))
            }
        }

        val dsu = UnionFind(n)
        while (closestPairs.isNotEmpty()) {
            val (_, a, b) = closestPairs.remove()
            if (dsu.union(a.id, b.id) && dsu.isConnected()) {
                return a.x.toLong() * b.x.toLong()
            }
        }

        error("No solution found for part 2")
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput, 10) == 40)
    check(part2(testInput) == 25272L)

    val input = readInput("Day08")
    part1(input, 1000).println()
    part2(input).println()
}