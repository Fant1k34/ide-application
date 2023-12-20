package application.Widgets.code.editor

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.Widgets.bufferState
import application.Widgets.code.editor.textStructure.Direction
import application.Widgets.code.editor.textStructure.GapBuffer
import application.Widgets.lineToShow
import application.compiler.Compiler

val printableCharacters =
    "`~ёЁ!1@2\"3№#4;$5%6:^7&?8*9(0)-_+=qwertyuiop[{]}asdfghjkl;':zxcvbnm,<.>/?йцукенгшщзхъ/\\|фывапролджэячсмитьбю., QWERTYUIOPLKJHGFDSAZXCVBNMЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ\n\tэ\'\"".toCharArray()

@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CodeEditor() {
    val bufferObject = bufferState.value ?: return

    // Определяем фокусировку
    val requester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

    // Место каретки в тексте
    val carriagePlace = remember { mutableStateOf(bufferObject.carriage) }

    val textMeasurer = rememberTextMeasurer()

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor = infiniteTransition.animateColor(
        initialValue = Color.Black,
        targetValue = Color.White,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart),
        label = "color"
    )

    val currentModifier = if (isFocused.value) (Modifier.fillMaxSize()
        .border(1.5.dp, Color.DarkGray, RectangleShape)) else Modifier.fillMaxSize()

    Canvas(modifier = currentModifier
        .onClick { requester.requestFocus() }
        // Обрабатываем нажатия кнопок в случае фокусировки на этом канвасе
        .onKeyEvent {
            if (it.type == KeyEventType.KeyUp) {
                when (it.key) {
                    Key.DirectionRight -> bufferObject.moveCarriage(Direction.RIGHT)
                    Key.DirectionLeft -> bufferObject.moveCarriage(Direction.LEFT)
                    Key.DirectionUp -> while (!listOf(
                            null,
                            '\n'
                        ).contains(bufferObject.moveCarriage(Direction.LEFT))
                    ) {
                    }

                    Key.DirectionDown -> while (!listOf(
                            null,
                            '\n'
                        ).contains(bufferObject.moveCarriage(Direction.RIGHT))
                    ) {
                    }

                    Key.Tab -> repeat(4) { bufferObject.addSymbol(' ') }

                    Key.Backspace -> bufferObject.deleteSymbol(Direction.LEFT)
                    Key.Enter -> bufferObject.addSymbol('\n')
                    else -> if (printableCharacters.contains(it.utf16CodePoint.toChar()))
                        bufferObject.addSymbol(it.utf16CodePoint.toChar())
                }

                carriagePlace.value = bufferObject.carriage
                lineToShow.value = bufferObject.showText()
            }
            true
        }.focusRequester(requester)
        .onFocusChanged { focusState -> isFocused.value = focusState.isFocused }
        .focusable()
    ) {
        val line = lineToShow.value ?: return@Canvas

        // Рисуем весь текст черным цветом без прозрачности
        val measuredText = textMeasurer.measure(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(brush = SolidColor(Color.Black), alpha = 1f)) {
                    append(line.slice(0 until carriagePlace.value))
                    append(line.slice(carriagePlace.value until line.length))
                }
            },
            style = TextStyle(fontSize = 20.sp)
        )

        // Рисуем текст до каретки полностью прозрачным (цвет не важен), а затем рисуем НЕ прозрачным символ каретки ⎸
        val measuredCarriage = textMeasurer.measure(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(brush = SolidColor(Color.Black), alpha = 0f)) {
                    append(line.slice(0 until carriagePlace.value))
                }
                withStyle(
                    style = SpanStyle(
                        brush = SolidColor(animatedColor.value),
                        alpha = 1f,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("⎸")
                }
            },
            style = TextStyle(fontSize = 20.sp)
        )

        // За счет работы функции рендеринга первый текст отрисовывается полностью, а второй накладывается на первый
        // Вследствие того, что во втором тексте все прозрачное, кроме символа каретки,
        // то мы просто наложили символ каретки на первый текст, не двигая постоянно символы
        translate(100f, 100f) {
            drawText(measuredText)
            if (isFocused.value) {
                drawText(measuredCarriage)
            }
        }
    }
}