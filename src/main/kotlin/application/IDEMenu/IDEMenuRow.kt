package application.IDEMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import application.*

enum class IDEMenuOptions {
    Project,
    Configuration
}

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
            IDEMenuOptions.Project,
            "Project",
            mapOf(
                "Set project" to { println("Setting the project, bla bla bla") },
                "Close current project" to { println("Закрыли проект") }),
            isFirst = true,
        )

        IDEMenuItem(
            IDEMenuOptions.Configuration,
            "Configuration",
            mapOf(
                "Upscale menu" to { increaseIDEMenuSizeCoefficient() },
                "Downscale menu" to { decreaseIDEMenuSizeCoefficient() },
                "Upscale features" to { increaseIDEFeatureCoefficient() },
                "Downscale features" to { decreaseIDEFeatureCoefficient() },
            )
        )

        IDEMenuItem(
            IDEMenuOptions.Configuration,
            "Configuration 2",
            mapOf(
                "Configuration" to { println("Configuration") },
                "Configuration Configuration" to { println("Configuration Configuration") })
        )

        IDEMenuItem(
            IDEMenuOptions.Configuration,
            "Configuration 3",
            mapOf(
                "Configuration" to { println("Configuration") },
                "Configuration Configuration" to { println("Configuration Configuration") }),
            isLast = true,
        )
    }
}
