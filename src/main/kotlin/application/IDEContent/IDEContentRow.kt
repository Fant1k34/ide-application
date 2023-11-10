package application.IDEContent

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import application.Widgets.Widgets
import application.Widgets.code.editor.CodeEditor
import application.Widgets.leftWidgets
import application.modifyIDEFeature

/**
 * IDEContentRow consist of feature column, left widget (optional), divider and main widget
 *      1. FeatureColumn:
 *          - Column with different features like Project Structure, Git, Runtime, Debug...
 *          - By clicking on these features they call choseLeftWidget(...) with necessary Widget enum value
 *          - After that state for left widget (chosenLeftWidget) could be changed
 *      2. Left Widget:
 *          - Left place to show some widget.
 *          - Always show the state of chosenLeftWidget. If it changes, the component rerenders
 *          - After changing the state of chosenLeftWidget,
 *          - component finds the action for current Widget enum value based on leftWidgets map in Widgets/WidgetsConfig.kt
 *          - Then component calls this action (it must be a @Composable function) and show the result into left widget
 *      3. Divider:
 *          - Just movable (draggable) divider between Left Widget and Main Widget
 *      4. Main Widget:
 *          - Code Editor widget
 *
 * How to add new left widget?
 *      1. Implement @Composable function inside ./Widgets/
 *      2. Add enum value to Widgets inside ./Widgets/WidgetsConfig.kt
 *      3. Add pair inside leftWidgets map with importing @Composable function from 1.
 *      4. Add IDEFeatureItem to IDEFeatureColumn.kt file
 *          with created enum value in Widgets (from 2.) and fill other fields of IDEFeatureItem
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IDEContentRow() {
    val featureSize = remember { mutableStateOf(256.dp) }

    val chosenLeftWidget = remember { mutableStateOf<Widgets?>(null) }

    // Function for choosing widget
    // It is accepted by IDEFeatureColumn component to change parent's state via clicking to some feature button
    val choseLeftWidget = { newWidget: Widgets ->
        if (chosenLeftWidget.value == newWidget) {
            chosenLeftWidget.value = null
        } else {
            chosenLeftWidget.value = newWidget
        }
    }

    Row {
        IDEFeatureColumn(choseLeftWidget)

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
                    EmbeddedComponent(chosenWidget, action)
                }
            }

            Column(
                modifier = Modifier.defaultMinSize(minWidth = 8.dp, minHeight = 100.dp).fillMaxHeight().width(4.dp)
                    .background(Color.LightGray)
                    .onDrag { featureSize.value += (it.x / 1.25).dp }
            ) {}
        }

        Column(
            modifier = Modifier.defaultMinSize(minWidth = 200.dp, minHeight = 100.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // TODO: Придумать, как виджет может заставить компонент CodeEditor обновиться (например, файл удалили)
            // TODO: Может быть, как-то по-умному написать VFS
            CodeEditor()
        }
    }
}