package application.compiler.analysis.type

class UnionFindCollection<T> {

    val parent = mutableMapOf<T, T>()

    fun union(left: T, right: T) {
        val leftParent = find(left)
        val rightParent = find(right)

        if (leftParent != rightParent) {
            parent[leftParent] = rightParent
        }
    }

    fun find(elem: T): T {
        if (parent[elem] != elem) {
            parent[elem] = find(parent[elem]!!)
        }
        return parent[elem]!!
    }

    fun add(elem: T) {
        parent[elem] = elem
    }

    override fun toString(): String = parent.toString()
}