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

val bufferState = mutableStateOf(GapBuffer(gapBufferSize = 4, text = inputText))
var lineToShow = mutableStateOf(bufferState.value.showText())

fun updateBufferObject(text: String) {
    bufferState.value = GapBuffer(gapBufferSize = 4, text = text)
}
