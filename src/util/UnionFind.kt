package util

class UnionFind(n: Int) {

    private val parent = IntArray(n) { it }
    private val size = IntArray(n) { 1 }

    private var components = n

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x])
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val xSet = find(x)
        val ySet = find(y)

        if (xSet == ySet) {
            return false
        }

        components--
        when {
            size[xSet] < size[ySet] -> {
                parent[xSet] = ySet
                size[ySet] += size[xSet]
            }
            else -> {
                parent[ySet] = xSet
                size[xSet] += size[ySet]
            }
        }
        return true
    }

    fun getComponentSize(x: Int): Int {
        val xSet = find(x)
        return size[xSet]
    }

    fun isConnected() = components == 1
}