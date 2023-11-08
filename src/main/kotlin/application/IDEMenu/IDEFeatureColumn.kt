package application.IDEMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import application.IDEContent.Widgets
import application.modifyIDEFeature

@Composable
fun IDEFeatureColumn(
    choseWidget: (chosen: Widgets) -> Unit,
//    updateBottomWidget: () -> Unit
) {
    Column(
        modifier = Modifier.drawBehind {
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
                featureName = "PS",
                featurePlace = FeaturePlace.LEFT,
                onClick = {
                    choseWidget(Widgets.PROJECT_STRUCTURE)
                })

            IDEFeatureItem(
                featureName = "G",
                featurePlace = FeaturePlace.LEFT,
                onClick = {
                    choseWidget(Widgets.GIT)
                })
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(), verticalArrangement = Arrangement.Bottom
        ) {
            IDEFeatureItem(
                featureName = "R",
                featurePlace = FeaturePlace.RIGHT,
                onClick = { println("Кликнули по коду") })
            IDEFeatureItem(
                featureName = "D",
                featurePlace = FeaturePlace.RIGHT,
                onClick = { println("Тут типо будет Гит") })
        }
    }
}