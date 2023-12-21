package application.store.MainStore

import androidx.compose.runtime.mutableStateOf
import application.store.MainStore.textStructure.Direction
import application.store.MainStore.textStructure.GapBuffer
import java.io.File

class MainStore {
    val lineToShow = mutableStateOf<String?>(null)
    val carriagePlace = mutableStateOf<Int>(0)

    private var gapBuffer: GapBuffer? = null

    fun setCarriagePlace() {
        carriagePlace.value = gapBuffer?.carriage ?: 0
    }

    fun setLineToShow() {
        lineToShow.value = gapBuffer?.showText()
    }

    fun initBuffer(text: String) {
        gapBuffer = GapBuffer(64, text)
        lineToShow.value = text
        setCarriagePlace()
    }

    fun moveCarriageLeft() {
        gapBuffer?.moveCarriage(Direction.LEFT)
        setCarriagePlace()
    }

    fun moveCarriageRight() {
        gapBuffer?.moveCarriage(Direction.RIGHT)
        setCarriagePlace()
    }

    fun moveCarriageUp() {
        while (!listOf(
                null,
                '\n'
            ).contains(gapBuffer?.moveCarriage(Direction.LEFT))
        ) {
        }
        setCarriagePlace()
    }

    fun moveCarriageDown() {
        while (!listOf(
                null,
                '\n'
            ).contains(gapBuffer?.moveCarriage(Direction.RIGHT))
        ) {
        }
        setCarriagePlace()
    }


    fun addSymbol(symbol: Char) {
        gapBuffer?.addSymbol(symbol)
        setCarriagePlace()
        setLineToShow()
    }

    fun deleteLeftSymbol() {
        gapBuffer?.deleteSymbol(Direction.LEFT)
        setCarriagePlace()
        setLineToShow()
    }

    fun deleteRightSymbol() {
        gapBuffer?.deleteSymbol(Direction.RIGHT)
        setCarriagePlace()
        setLineToShow()
    }


    val projectPath = mutableStateOf<String?>(null)

    fun setProjectPath(path: String) {
        projectPath.value = path
    }


    val currentFile = mutableStateOf<File?>(null)

    fun setCurrentFile(file: File) {
        currentFile.value = file

        if (file.canRead()) {
            initBuffer(file.readLines().joinToString("\n"))
        }
    }

    fun writeToCurrentFile() {
        val file = currentFile.value ?: return

        if (file.canWrite()) {
            file.writeText(lineToShow.value ?: return)
        }
    }
}

val mainStore = MainStore()
