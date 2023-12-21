package application.store.MainStore.compiler

data class Location(val line: Int, val column: Int) {
    override fun toString(): String = "$line:$column)"
}