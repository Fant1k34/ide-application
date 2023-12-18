package application.Widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun codeRunner() {
    Button(onClick = { println("hhhhh") }) {
        Text("Тут простой пример встраивания виджета")
    }
}
