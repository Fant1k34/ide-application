package application.IDEMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import application.*

/**
 * IDEMenuRow is IDE Menu to configure IDE.
 * It describes how IDE Menu items should be named, and how it's subItems
 * should be named and what these subItems should do by clicking on them
 * TODO: Make these actions @Composable
 */
@Composable
fun IDEMenuRow() {
    Row(
        modifier = Modifier.drawBehind {
            val strokeWidth = modifyIDEMenu(4f)
            val y = size.height + strokeWidth / 2

            drawLine(
                Color.LightGray,
                Offset(0f, y),
                Offset(Float.MAX_VALUE, y),
                strokeWidth
            )
        }.fillMaxWidth().background(
            Color.LightGray
        )
    ) {
        IDEMenuItem(
            "Project",
            mapOf(
                "Set project" to { println("Setting the project, bla bla bla") },
                "Close current project" to { println("Закрыли проект") }),
            isFirst = true,
        )

        IDEMenuItem(
            "Configuration",
            mapOf(
                "Upscale menu" to { increaseIDEMenuSizeCoefficient() },
                "Downscale menu" to { decreaseIDEMenuSizeCoefficient() },
                "Upscale features" to { increaseIDEFeatureCoefficient() },
                "Downscale features" to { decreaseIDEFeatureCoefficient() },
            )
        )

        IDEMenuItem(
            "Configuration 2",
            mapOf(
                "Configuration" to { println("Configuration") },
                "Configuration Configuration" to { println("Configuration Configuration") })
        )

        IDEMenuItem(
            "Configuration 3",
            mapOf(
                "Configuration" to { println("Configuration") },
                "Configuration Configuration" to { println("Configuration Configuration") }),
            isLast = true,
        )
    }
}
