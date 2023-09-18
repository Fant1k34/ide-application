@file:OptIn(ExperimentalTextApi::class, ExperimentalTextApi::class, ExperimentalTextApi::class,
    ExperimentalTextApi::class, ExperimentalTextApi::class, ExperimentalTextApi::class, ExperimentalTextApi::class
)

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import textStructure.Direction

import textStructure.GapBuffer

val printableCharacters = "`~ёЁ!1@2\"3№#4;$5%6:^7&?8*9(0)-_+=qwertyuiop[{]}asdfghjkl;':zxcvbnm,<.>/?йцукенгшщзхъ/\\|фывапролджэячсмитьбю., QWERTYUIOPLKJHGFDSAZXCVBNMЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ\n\t".toCharArray()

val bufferObject = GapBuffer(gapBufferSize = 4, text = "Вот определенная строка!")
val lineToShow = mutableStateOf(bufferObject.showText())
var ind = 0;

@OptIn(ExperimentalTextApi::class)
@Composable
@Preview
fun app() {
    val textMeasurer = rememberTextMeasurer()

    MaterialTheme {
        Box() {
            Canvas(modifier = Modifier.fillMaxSize()) {
                lineToShow.let {
                    val line = it.value

                    val measuredText = textMeasurer.measure(
                        text = buildAnnotatedString {
                            append(line.slice(0 until ind))
                            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                append(line[ind])
                            }
                            append(line.slice(ind + 1 until line.length))},
                        style = TextStyle(fontSize = 20.sp)
                    )

                    translate(100f, 100f) {
                        drawText(measuredText)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        onKeyEvent = {
            if (it.type == KeyEventType.KeyUp) {
                if (it.key == Key.DirectionRight) {
                    ind = bufferObject.moveCarriage(ind, Direction.RIGHT);
                }

                if (it.key == Key.DirectionLeft) {
                    ind = bufferObject.moveCarriage(ind, Direction.LEFT);
                }

                if (it.key == Key.Backspace) {
                    ind = bufferObject.deleteSymbol(ind, Direction.LEFT);
                }

                if (printableCharacters.contains(it.utf16CodePoint.toChar())) {
                    ind = bufferObject.addSymbol(ind, it.utf16CodePoint.toChar())
                }

                lineToShow.value = bufferObject.showAll()
            }
            true
        }) {
        app()
    }
}
