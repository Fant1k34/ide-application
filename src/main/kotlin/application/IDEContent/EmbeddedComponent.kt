package application.IDEContent

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import application.Widgets.Widgets

/**
 * EmbeddedComponent shows current @Composable Action Component
 */
@Composable
fun EmbeddedComponent(chosenWidget: Widgets?, action: @Composable () -> Unit) {
    if (chosenWidget != null) {
        action()
    } else {
        Text("Что-то сломалось, но мы уже чиним!")
    }
}