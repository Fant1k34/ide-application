package application.textStructure

// gapBufferSize > 2
// buffer содержит строку
// leftPointer и rightPointer нужно менять
// Индекс действия (индекс каретки) должен совпадать с leftPointer
// Если вставляем символы, то меняем gapBufferSize
class GapBuffer(private var gapBufferSize: Int = 32, textBuffer: MutableList<Char?>) : TextStructureInterface {
    private val defaultGapBufferSize: Int = gapBufferSize;

    // Левый и правый указатели на gapBuffer, оба включительно
    private var leftPointer = 0;
    private var rightPointer = gapBufferSize - 1;

    // Общий буффер с текстом
    private val buffer: MutableList<Char?> = MutableList(gapBufferSize) { null }.let { list1 ->
        textBuffer.let { list2 -> list1 + list2 }
    }.toMutableList()

    // Инициализируем: размер для gapBuffer и text (строка текста)
    constructor(gapBufferSize: Int, text: String) : this(gapBufferSize, text.toMutableList() as MutableList<Char?>)

    // Инициализируем: размер для gapBuffer и размер будущего текста (по началу будет заполнение null)
    constructor(gapBufferSize: Int, amountOfSymbols: Int) : this(gapBufferSize, MutableList(amountOfSymbols) { null })

    override fun moveCarriage(ind: Int, direction: Direction): Int {
        if (ind != leftPointer) throw Exception("Положение каретки не совпадает с указателями на Gap Buffer")

        if (direction == Direction.RIGHT) {
            // Если каретка стоит после последнего реального символа
            if (ind > buffer.size - gapBufferSize - 1) {
                return ind;
            }

            // Смещаем буффер на один символ вправо, то есть перекидываем самый правый символ после буфера - до буфера
            leftPointer += 1
            rightPointer += 1

            buffer[ind] = buffer[ind + gapBufferSize]
            buffer[ind + gapBufferSize] = null

            return ind + 1
        } else {
            // Если каретка стоит на самом левом положении
            if (ind <= 0) {
                return ind;
            }

            // Смещаем буффер на один символ влево
            leftPointer -= 1
            rightPointer -= 1

            buffer[ind + gapBufferSize - 1] = buffer[ind - 1]
            buffer[ind - 1] = null

            return ind - 1
        }
    }

    override fun addSymbol(ind: Int, symbol: Char): Int {
        if (ind != leftPointer) throw Exception("Положение каретки не совпадает с указателями на Gap Buffer")

        // Вставляем символ в буффер
        leftPointer += 1
        gapBufferSize -= 1
        buffer[ind] = symbol

        // Если размер буффера стал 0
        if (gapBufferSize < 1) {
            if (leftPointer != rightPointer + 1) throw Exception("Gap Buffer должен быть пустым. Но на него сбиты указатели")

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

            var pseudoCarret = leftPointer;
            // Двигаем каретку в то положение, в котором она была изначально + 1 (вставка символа)
            while (pseudoCarret != ind + 1) {
                pseudoCarret = moveCarriage(pseudoCarret, Direction.LEFT)
            }
            return pseudoCarret
        } else {
            // Если размер буфера не ноль
            return ind + 1
        }
    }

    override fun deleteSymbol(ind: Int, direction: Direction): Int {
        if (direction == Direction.LEFT) {
            if (ind > 0 && ind <= buffer.size - gapBufferSize) {
                leftPointer -= 1
                buffer[ind - 1] = null
                gapBufferSize += 1

                return ind - 1
            }

            return ind;
        }

        // TODO: Implement Delete by Delete button
        return ind;
    }

    override fun showText(): String = buffer.filterNotNull().joinToString("")

    fun showAll(): String = buffer.map { it ?: "_" }.joinToString("")
}