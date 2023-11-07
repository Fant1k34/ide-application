package application.IDEMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import application.modifyIDEFeature

@Composable
fun IDEFeatureColumn() {
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
                featureName = "C",
                featurePlace = FeaturePlace.RIGHT,
                onClick = { println("Кликнули по коду") })
            IDEFeatureItem(
                featureName = "G",
                featurePlace = FeaturePlace.RIGHT,
                onClick = { println("Тут типо будет Гит") })
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