package application.store.MainStore.textStructure

// gapBufferSize > 2
// buffer содержит строку
// leftPointer и rightPointer нужно менять
// Индекс действия (индекс каретки) должен совпадать с leftPointer
// Если вставляем символы, то меняем gapBufferSize
class GapBuffer(private var gapBufferSize: Int = 32, textBuffer: MutableList<Char?>) : TextStructureInterface {
    private val defaultGapBufferSize: Int = gapBufferSize

    // Левый и правый указатели на gapBuffer, оба включительно
    private var leftPointer = 0
    private var rightPointer = gapBufferSize - 1

    val carriage: Int get() = leftPointer

    // Общий буффер с текстом
    private val buffer: MutableList<Char?> = MutableList(gapBufferSize) { null }.let { list1 ->
        textBuffer.let { list2 -> list1 + list2 }
    }.toMutableList()

    // Инициализируем: размер для gapBuffer и text (строка текста)
    constructor(gapBufferSize: Int, text: String) : this(gapBufferSize, text.toMutableList() as MutableList<Char?>)

    // Инициализируем: размер для gapBuffer и размер будущего текста (по началу будет заполнение null)
    constructor(gapBufferSize: Int, amountOfSymbols: Int) : this(gapBufferSize, MutableList(amountOfSymbols) { null })

    override fun moveCarriage(direction: Direction): Char? {
        val crossedChar: Char

        if (direction == Direction.RIGHT) {
            // Если каретка стоит после последнего реального символа
            if (leftPointer > buffer.size - gapBufferSize - 1) {
                return null
            }

            // Смещаем буффер на один символ вправо, то есть перекидываем самый правый символ после буфера - до буфера
            buffer[leftPointer] = buffer[leftPointer + gapBufferSize]
            buffer[leftPointer + gapBufferSize] = null
            crossedChar = buffer[leftPointer] ?: throw Exception("Ошибка в рассчете crossedChar")

            leftPointer += 1
            rightPointer += 1
        } else {
            // Если каретка стоит на самом левом положении
            if (leftPointer <= 0) {
                return null
            }

            // Смещаем буффер на один символ влево
            buffer[leftPointer + gapBufferSize - 1] = buffer[leftPointer - 1]
            buffer[leftPointer - 1] = null
            crossedChar = buffer[leftPointer + gapBufferSize - 1] ?: throw Exception("Ошибка в рассчете crossedChar")

            leftPointer -= 1
            rightPointer -= 1
        }

        return crossedChar
    }

    override fun addSymbol(symbol: Char) {
        // Вставляем символ в буффер
        buffer[leftPointer] = symbol
        leftPointer += 1
        gapBufferSize -= 1

        // Если размер буффера стал 0
        if (gapBufferSize < 1) {
            if (leftPointer != rightPointer + 1) throw Exception("Gap Buffer должен быть пустым. Но на него сбиты указатели")
            // Подсчитываем сдвиг, на сколько символов нужно будет возвращать левый указатель в изначальное положение
            val shift = buffer.size - leftPointer

            // Генерируем новый буффер в конце
            leftPointer = buffer.size

            gapBufferSize = defaultGapBufferSize
            rightPointer = leftPointer + gapBufferSize - 1 // -1, потому что по правый указатель включительно

            // Заполняем null в количестве gapBufferSize
            for (i in 0 until gapBufferSize) {
                buffer.add(null)
            }
            showAll()

            // На этом этапе все хорошо, мы создали новый gap buffer в конце buffer
            // Но нужно передвинуть каретку на исходное положение

            // Двигаем каретку в то положение, в котором она была изначально
            repeat(shift) {
                moveCarriage(Direction.LEFT)
            }
        }
    }

    override fun deleteSymbol(direction: Direction) {
        if (direction == Direction.LEFT) {
            if (leftPointer > 0 && leftPointer <= buffer.size - gapBufferSize) {
                buffer[leftPointer - 1] = null
                leftPointer -= 1
                gapBufferSize += 1
            }
        }

        // TODO: Implement Delete by Delete button
        return
    }

    override fun showText(): String = buffer.filterNotNull().joinToString("")

    fun showAll(): String = buffer.map { it ?: "_" }.joinToString("")
}