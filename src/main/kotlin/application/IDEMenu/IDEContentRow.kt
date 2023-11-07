package application.IDEMenu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
@Composable
fun IDEContentRow(
    lineToShow: MutableState<String>,
    ind: Int
) {
    val textMeasurer = rememberTextMeasurer()

    Row {
        Column {
            IDEFeatureColumn()

            Box {
                Canvas(modifier = Modifier.fillMaxSize()) {
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
        }
    }
}