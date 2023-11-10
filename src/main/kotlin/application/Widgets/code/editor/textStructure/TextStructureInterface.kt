package application.Widgets.code.editor.textStructure

interface TextStructureInterface {
    // Возвращает символ, который был пересечен кареткой или null
    fun moveCarriage(direction: Direction): Char?
    fun addSymbol(symbol: Char)
    fun deleteSymbol(direction: Direction)
    fun showText(): String
}