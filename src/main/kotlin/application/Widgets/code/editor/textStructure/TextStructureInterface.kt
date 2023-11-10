package application.Widgets.code.editor.textStructure

interface TextStructureInterface {
    fun moveCarriage(ind: Int, direction: Direction): Int
    fun addSymbol(ind: Int, symbol: Char): Int
    fun deleteSymbol(ind: Int, direction: Direction): Int
    fun showText(): String
}