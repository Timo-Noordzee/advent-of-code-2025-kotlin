import util.Point2D
import util.sqDistanceTo
import java.awt.Polygon
import java.awt.geom.Rectangle2D
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    // Calculates the orientation of ordered triplets
    // Return -1 for clockwise, 1 for counter-clockwise, 0 for collinear
    fun orientation(a: Point2D, b: Point2D, c: Point2D): Int {
        return (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)).sign
    }

    fun findConvexHull(points: List<Point2D>): List<Point2D> {
        val n = points.size
        if (n < 3) {
            error("cannot create convex hull with less than 3 points")
        }

        // Find the bottom most point (and left-most when a tie occurs)
        val p0 = points.minWith(compareBy({ it.y }, { it.x }))

        val sortedPoints = points.sortedWith { p1, p2 ->
            val orientation = orientation(p0, p1, p2)

            // If points are collinear, sort by the distance from p0
            if (orientation == 0) {
                return@sortedWith p0.sqDistanceTo(p1).compareTo(p0.sqDistanceTo(p2))
            }

            orientation
        }

        val stack = ArrayDeque<Point2D>()
        sortedPoints.forEach { p ->
            while (stack.size > 2 && orientation(stack[stack.size - 2], stack[stack.size - 1], p) > 0) {
                stack.removeLast()
            }
            stack.add(p)
        }

        if (stack.size < 3) {
            error("convex hull is invalid")
        }

        stack.addLast(stack.first())

        return stack
    }

    fun part1(input: List<String>): Long {
        val points = input
            .map { it.split(',').map(String::toLong) }
            .map { (x, y) -> Point2D(x, y) }

        val convexHull = findConvexHull(points)

        // Brute force with points in convex hull
        var maxArea = Long.MIN_VALUE
        for (i in 0 until convexHull.size) {
            val a = convexHull[i]
            for (j in i + 1 until convexHull.size) {
                val b = convexHull[j]
                val xMax = a.x.coerceAtLeast(b.x)
                val xMin = a.x.coerceAtMost(b.x)
                val yMax = a.y.coerceAtLeast(b.y)
                val yMin = a.y.coerceAtMost(b.y)
                val area = (xMax - xMin + 1) * (yMax - yMin + 1)
                if (area > maxArea) {
                    maxArea = area
                }
            }
        }
        return maxArea
    }

    fun part2(input: List<String>): Long {
        val points = input.map { it.split(',').map(String::toInt) }

        val poly = Polygon()
        points.forEach { (x, y) ->
            poly.addPoint(x, y)
        }

        var max: Long = Long.MIN_VALUE
        for (i in 0 until points.size) {
            val (x1, y1) = points[i]
            for (j in i + 1 until points.size) {
                val (x2, y2) = points[j]
                val xMin = x1.coerceAtMost(x2)
                val xMax = x1.coerceAtLeast(x2)
                val yMin = y1.coerceAtMost(y2)
                val yMax = y1.coerceAtLeast(y2)
                val area = (xMax - xMin + 1L) * (yMax - yMin + 1L)

                if (area > max) {
                    val rect = Rectangle2D.Double(
                        minOf(x1, x2).toDouble(),
                        minOf(y1, y2).toDouble(),
                        (x2 - x1).absoluteValue.toDouble(),
                        (y2 - y1).absoluteValue.toDouble()
                    )

                    if (poly.contains(rect)) {
                        max = area
                    }
                }
            }
        }
        return max
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 50L)
    check(part2(testInput) == 24L)

    val input = readInput("Day09")
    measureTimeMillis { part1(input).println() }.also { println("$it ms") }
    part2(input).println() // 4607140320 high
}