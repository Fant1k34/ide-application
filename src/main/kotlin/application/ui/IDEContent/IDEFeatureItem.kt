package application.ui.IDEContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.ui.Widgets.Widgets
import application.ui.modifyIDEFeature

/**
 * IDEFeatureItem is Component for feature button
 */

@Composable
fun IDEFeatureItem(
    feature: Widgets,
    featureName: String,
    // choseLeftWidget is function for updating state of left widget in parent component
    choseLeftWidget: (Widgets) -> Unit,
) {
    Button(
        onClick = {
            choseLeftWidget(feature)
        },
        shape = RoundedCornerShape(
            modifyIDEFeature(0f),
            modifyIDEFeature(8f),
            modifyIDEFeature(8f),
            modifyIDEFeature(0f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(),
        contentPadding = PaddingValues(),
        modifier = Modifier.padding(
            top = modifyIDEFeature(4.dp),
            bottom = modifyIDEFeature(4.dp),
            end = modifyIDEFeature(4.dp)
        ).width(modifyIDEFeature(36.dp))
            .height(modifyIDEFeature(48.dp))
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
    ) {
        Text(
            featureName,
            color = Color.Black,
            fontSize = modifyIDEFeature(24.sp),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = modifyIDEFeature(4.dp))
        )
    }
}