package application.IDEMenu

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
import application.modifyIDEFeature

enum class FeaturePlace {
    LEFT,
    RIGHT,
    BOTTOM,
}

@Composable
fun IDEFeatureItem(
    // featureName - имя фичи в панели слева в IDE
    featureName: String,
    // featurePlace - месторасположение фичи (справа (код), слева (файлы или гит), снизу (runtime, debug))
    featurePlace: FeaturePlace,
    // onClick - действие по клику
    onClick: () -> Unit,
    // onHover - действие по наведение на фичу
    onHover: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(
            modifyIDEFeature(4f),
            modifyIDEFeature(4f),
            modifyIDEFeature(4f),
            modifyIDEFeature(4f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(),
        contentPadding = PaddingValues(),
        modifier = Modifier.padding(
            top = modifyIDEFeature(2.dp),
            bottom = modifyIDEFeature(2.dp),
            start = modifyIDEFeature(2.dp),
            end = modifyIDEFeature(2.dp)
        ).size(modifyIDEFeature(36.dp))
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