package application.IDEContent.widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun projectStructureWidget() {
    Button(onClick = { println("hhhhh") }) {
        Text("Тут простой пример встраивания виджета")
    }
}
