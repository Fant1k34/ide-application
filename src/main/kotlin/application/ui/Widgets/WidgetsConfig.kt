package application.ui.Widgets

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import application.ui.Widgets.project.structure.codeRunner
import application.ui.Widgets.project.structure.projectStructureWidget

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
    Widgets.RUNTIME to { codeRunner() },
)

// Набор нижних виджетов
val bottomWidgets = mutableMapOf<Widgets, @Composable () -> Unit>(
    Widgets.RUNTIME to { codeRunner() },
)
