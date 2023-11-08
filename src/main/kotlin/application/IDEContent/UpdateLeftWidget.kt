package application.IDEContent

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun updateLeftWidget(chosenWidget: Widgets?, action: @Composable () -> Unit) {
    if (chosenWidget != null) {
        action()
    } else {
        Text("Что-то сломалось, но мы уже чиним!")
    }
}