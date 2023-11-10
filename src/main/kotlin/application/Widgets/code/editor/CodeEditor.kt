package application.Widgets.code.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import application.Widgets.code.editor.state.ApplicationState
import application.Widgets.code.editor.textStructure.Direction

val applicationState = ApplicationState();
var lineToShow = mutableStateOf(applicationState.bufferObject.showText())
var ind = 0;

@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CodeEditor() {
    val textMeasurer = rememberTextMeasurer()
    val requester = remember { FocusRequester() }

    Canvas(modifier = Modifier.fillMaxSize()
        .onClick { requester.requestFocus() }
        .onKeyEvent {
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

                if (it.key == Key.Enter) {
                    ind = applicationState.bufferObject.addSymbol(ind, '\n');
                }

                if (applicationState.printableCharacters.contains(it.utf16CodePoint.toChar())) {
                    ind = applicationState.bufferObject.addSymbol(ind, it.utf16CodePoint.toChar())
                }

                lineToShow.value = applicationState.bufferObject.showText()
            }
            true
        }.focusRequester(requester)
        .focusable()
    ) {
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

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}