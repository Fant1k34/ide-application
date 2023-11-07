package application

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import application.IDEMenu.IDEContentRow
import application.IDEMenu.IDEMenuRow
import application.state.ApplicationState
import application.textStructure.Direction

val applicationState = ApplicationState();
var lineToShow = mutableStateOf(applicationState.bufferObject.showText())
var ind = 0;

@Composable
@Preview
fun app() {
    MaterialTheme {
        Column {
            IDEMenuRow()
            IDEContentRow(lineToShow, ind)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun getApp() = application {
    Window(onCloseRequest = ::exitApplication,
        onKeyEvent = {
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
        app()
    }
}