package util

data class Point2D(var x: Long, var y: Long)

fun Point2D.sqDistanceTo(other: Point2D): Long {
    return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y)
}