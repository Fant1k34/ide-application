package application.IDEContent

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.IDEContent.widgets.code.editor.CodeEditor
import application.IDEContent.widgets.leftWidgets
import application.IDEMenu.IDEFeatureColumn
import application.modifyIDEFeature

enum class Widgets {
    PROJECT_STRUCTURE,
    GIT,
    RUNTIME,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IDEContentRow() {
    val featureSize = remember { mutableStateOf(256.dp) }

    // Выбранный виджет
    val chosenLeftWidget = remember { mutableStateOf<Widgets?>(null) }
    // Функция для установки виджета -- передается в компоненты, чтобы изменить state у родителя

    val choseWidget = { widget: Widgets ->
        if (chosenLeftWidget.value != null) {
            chosenLeftWidget.value = null
        } else {
            chosenLeftWidget.value = widget
        }
    }

    Row {
        IDEFeatureColumn(choseWidget)

        if (chosenLeftWidget.value != null) {
            Column(
                modifier = Modifier.defaultMinSize(minWidth = 100.dp, minHeight = 100.dp).fillMaxHeight()
                    .width(featureSize.value).background(
                        Color.White, RoundedCornerShape(
                            modifyIDEFeature(16f),
                            modifyIDEFeature(16f),
                            modifyIDEFeature(0f),
                            modifyIDEFeature(0f)
                        )
                    )
            ) {
                val chosenWidget = chosenLeftWidget.value
                val action = leftWidgets[chosenWidget]

                if (action != null) {
                    updateLeftWidget(chosenWidget, action)
                }
            }



            Column(
                modifier = Modifier.defaultMinSize(minWidth = 8.dp, minHeight = 100.dp).fillMaxHeight().width(4.dp)
                    .background(Color.LightGray)
                    .onDrag {
                        featureSize.value += (it.x / 1.25).dp
                    }
            ) {

            }
        }

        Column(
            modifier = Modifier.fillMaxSize().background(
                Color.White
            )
        ) {
            CodeEditor()
        }
    }
}