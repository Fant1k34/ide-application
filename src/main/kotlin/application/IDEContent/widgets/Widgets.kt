package application.IDEContent.widgets

import androidx.compose.runtime.Composable
import application.IDEContent.Widgets
import application.IDEContent.widgets.project.structure.projectStructureWidget

// Набор левых виджетов
val leftWidgets = mutableMapOf<Widgets, @Composable () -> Unit>(
    Widgets.PROJECT_STRUCTURE to { projectStructureWidget() },
    Widgets.GIT to { throw Exception() },
)

// Набор правых виджетов
val rightWidgets = mutableMapOf<Widgets, @Composable () -> Unit>(
    Widgets.RUNTIME to { projectStructureWidget() },
)
