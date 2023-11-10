package application.Widgets

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import application.Widgets.project.structure.projectStructureWidget

enum class Widgets {
    PROJECT_STRUCTURE,
    GIT,
    RUNTIME,
    DEBUG,
}

// Набор левых виджетов
val leftWidgets = mutableMapOf<Widgets, @Composable () -> Unit>(
    Widgets.PROJECT_STRUCTURE to { projectStructureWidget() },
    Widgets.GIT to { Button(onClick = { println() }) { Text("Тут типо ГИТ") } },
)

// Набор нижних виджетов
val bottomWidgets = mutableMapOf<Widgets, @Composable () -> Unit>(
    Widgets.RUNTIME to { projectStructureWidget() },
)
