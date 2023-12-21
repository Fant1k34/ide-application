package application.ui.Widgets.code.editor

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.store.MainStore.mainStore

val printableCharacters =
    "`~ёЁ!1@2\"3№#4;$5%6:^7&?8*9(0)-_+=qwertyuiop[{]}asdfghjkl;':zxcvbnm,<.>/?йцукенгшщзхъ/\\|фывапролджэячсмитьбю., QWERTYUIOPLKJHGFDSAZXCVBNMЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ\n\tэ\'\"".toCharArray()

@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CodeEditor() {
    // Определяем фокусировку
    val requester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    val clipboard: ClipboardManager = LocalClipboardManager.current

    // Место каретки в тексте
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
            if (it.type == KeyEventType.KeyDown) {
                if (it.isCtrlPressed && it.key == Key.V) {
                    clipboard.getText()?.forEach { newIt ->
                        mainStore.addSymbol(newIt)
                    }
                }

                when (it.key) {
                    Key.DirectionRight -> mainStore.moveCarriageRight()
                    Key.DirectionLeft -> mainStore.moveCarriageLeft()
                    Key.DirectionUp -> mainStore.moveCarriageUp()
                    Key.DirectionDown -> mainStore.moveCarriageDown()
                    Key.Tab -> repeat(4) { mainStore.addSymbol(' ') }
                    Key.Backspace -> mainStore.deleteLeftSymbol()
                    Key.Enter -> mainStore.addSymbol('\n')

                    else -> if (printableCharacters.contains(it.utf16CodePoint.toChar()))
                        mainStore.addSymbol(it.utf16CodePoint.toChar())
                }
            }
            true
        }.focusRequester(requester)
        .onFocusChanged { focusState -> isFocused.value = focusState.isFocused }
        .focusable()
    ) {
        val line = mainStore.lineToShow.value ?: return@Canvas

        // Рисуем весь текст черным цветом без прозрачности
        val measuredText = textMeasurer.measure(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(brush = SolidColor(Color.Black), alpha = 1f)) {
                    append(line.slice(0 until mainStore.carriagePlace.value))
                    append(line.slice(mainStore.carriagePlace.value until line.length))
                }
            },
            style = TextStyle(fontSize = 20.sp)
        )

        // Рисуем текст до каретки полностью прозрачным (цвет не важен), а затем рисуем НЕ прозрачным символ каретки ⎸
        val measuredCarriage = textMeasurer.measure(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(brush = SolidColor(Color.Black), alpha = 0f)) {
                    append(line.slice(0 until mainStore.carriagePlace.value))
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