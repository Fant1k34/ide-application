package application.Widgets

import androidx.compose.runtime.mutableStateOf
import application.Widgets.code.editor.textStructure.GapBuffer

val inputText = """
    var f1 = 0;
    print(f1);
    
    while (1 == 1) {
        f1 = f1 + 1;
    }
    
    print(f1);
""".trimIndent()
val bufferObject = GapBuffer(gapBufferSize = 4, text = inputText)
var lineToShow = mutableStateOf(bufferObject.showText())
