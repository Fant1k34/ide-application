package application.Widgets

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import application.Widgets.code.editor.textStructure.GapBuffer
import java.io.File

var projectDir: MutableState<String?> = mutableStateOf(null)

fun setProjectDir(projectDirectory: String) {
    if (File(projectDirectory).isDirectory) {
        projectDir.value = projectDirectory
    }
}

val inputText = """
    var f1 = 0;
    print(f1);
    
    while (1 == 1) {
        f1 = f1 + 1;
    }
    
    print(f1);
""".trimIndent()

val editableFilename = mutableStateOf<String?>(null)
val fileContent = mutableStateOf<String?>(null)
val bufferState = mutableStateOf<GapBuffer?>(null)
var lineToShow = mutableStateOf<String?>(bufferState.value?.showText())

fun updateFileToEdit(newFileToEdit: String) {
    println(newFileToEdit)

    editableFilename.value = newFileToEdit

    println(editableFilename.value)

    fileContent.value = editableFilename.value?.let {
        val currentFile = File(it)
        if (currentFile.canRead()) {
            return@let currentFile.readLines().joinToString("\n")
        }
        return@let null
    }

    println(fileContent.value)

    bufferState.value = fileContent.value?.let {
        GapBuffer(gapBufferSize = 4, text = it)
    }

    println(bufferState.value)

    lineToShow.value = bufferState.value?.showText()
}
