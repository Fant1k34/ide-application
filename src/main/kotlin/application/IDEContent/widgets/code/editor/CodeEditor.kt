package application.IDEContent.widgets.code.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import application.state.ApplicationState
import application.textStructure.Direction

val applicationState = ApplicationState();
var lineToShow = mutableStateOf(applicationState.bufferObject.showText())
var ind = 0;

@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CodeEditor() {
    val textMeasurer = rememberTextMeasurer()

    // TODO: Не работает печаталка ((
    Canvas(modifier = Modifier.fillMaxSize().onClick { println("Click"); true }.onKeyEvent {
        println("Я тут!!!")
        if (it.type == KeyEventType.KeyUp) {
            if (it.key == Key.DirectionRight) {
                ind = applicationState.bufferObject.moveCarriage(ind, Direction.RIGHT);
            }

            if (it.key == Key.DirectionLeft) {
                ind = applicationState.bufferObject.moveCarriage(ind, Direction.LEFT);
            }

            if (it.key == Key.Backspace) {
                ind = applicationState.bufferObject.deleteSymbol(ind, Direction.LEFT);
            }

            if (applicationState.printableCharacters.contains(it.utf16CodePoint.toChar())) {
                ind = applicationState.bufferObject.addSymbol(ind, it.utf16CodePoint.toChar())
            }

            lineToShow.value = applicationState.bufferObject.showText()
        }
        true
    }) {
        lineToShow.let {
            val line = it.value

            val measuredText = textMeasurer.measure(
                text = buildAnnotatedString {
                    append(line.slice(0 until ind))
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(line[ind])
                    }
                    append(line.slice(ind + 1 until line.length))
                },
                style = TextStyle(fontSize = 20.sp)
            )

            translate(100f, 100f) {
                drawText(measuredText)
            }
        }
    }
}