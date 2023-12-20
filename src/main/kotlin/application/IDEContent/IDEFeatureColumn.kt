package application.IDEContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import application.Widgets.Widgets

/**
 * IDEFeatureColumn is Column with buttons to show (or hide) left widget
 * When user clicks on IDEFeatureItem, it invokes function with it's Widget enum value
 * And this function update state of IDEContentRow
 *
 * IDEFeatureColumn defines possible features, and it's place (left or bottom) with updating parents state function
 */
@Composable
fun IDEFeatureColumn(
    choseLeftWidget: (chosen: Widgets) -> Unit,
) {
    Column(
        modifier = Modifier.drawBehind {
            // To draw a line-divider
            val strokeWidth = 4f
            val x = size.width + strokeWidth / 2

            drawLine(
                Color.LightGray,
                Offset(x, 0f),
                Offset(x, Float.MAX_VALUE),
                strokeWidth
            )
        }.background(
            Color.LightGray
        )
    ) {
        Column {
            IDEFeatureItem(
                feature = Widgets.PROJECT_STRUCTURE,
                featureName = "P",
                choseLeftWidget = choseLeftWidget,
            )

            IDEFeatureItem(
                feature = Widgets.GIT,
                featureName = "G",
                choseLeftWidget = choseLeftWidget
            )

            IDEFeatureItem(
                feature = Widgets.RUNTIME,
                featureName = "R",
                choseLeftWidget = choseLeftWidget
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(), verticalArrangement = Arrangement.Bottom
        ) {
            IDEFeatureItem(
                feature = Widgets.DEBUG,
                featureName = "D",
                choseLeftWidget = choseLeftWidget
            )
        }
    }
}