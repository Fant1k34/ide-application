package application

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import application.ui.IDEContent.IDEContentRow
import application.ui.IDEMenu.IDEMenuRow

/**
 * IDE Application consists of these parts:
 * 1. IDE Menu
 *      May include information about project, IDE settings, IDE information and so on
 *      It takes place to the top of this IDE
 * 2. IDE Feature Column
 *      May include IDE features such as project structure, git commands, runtime compilation, debug and so on.
 *      These features could be on the top part of column or on the bottom one
 *      Those, who take place on the top, embeds to the left widget
 *      On the bottom part - to bottom widget (see about widgets below)
 *      IDE Feature Column takes place to the left of this IDE
 * 3. Main (Right) Widget
 *      It is the main widget with Canvas to write or edit code.
 *      It imports the widget from Widgets/widgets/code/editor/CodeEditor.kt
 *      CodeEditor create a Canvas to edit chosen file
 *      It takes place to the center and right part of this IDE
 * 4. Left Widget + Divider
 *      It is the secondary widget that optional to show
 *      If the user click to the one feature from IDE Feature Column, the left widget embeds chosen widget
 *      To successfully insert widget to the left part of IDE, you should meet the requirements in Widgets/WidgetsConfig.kt
 *      Divider divides the left widget from the main one
 *      This divider could be dragged as much as you like it
 * 5. Bottom Widget
 *      TODO: ;)
 *
 * Widgets interactions:
 *      Left Widget:
 *          - could ask Main (Right) Widget to update via updateMainWidget(safe: Boolean) function
 *          - must be synchronized with the Main Widget (if user delete such file with left widget help, then
 *          the main widget should not show deleted file anymore)
 *      Main Widget:
 *          - could not ask any widgets to update
 *          - must show its' relevant condition
 *      Bottom Widget:
 *          - could not ask any widgets to update
 *          - show some statistics that could be calculated before
 *          - should not be updated with changes in Main Widget
 *
 * Application markup looks like:
 *  Column:
 *      Row #1 [IDE Menu Row]
 *      Row #2 Divider for [IDE Menu Row]
 *      Row #3 [IDE Content Row]:
 *          Column #1         Column #2             Column #3
 *          (inside Row #2)   (Draggable Divider)   CodeEditor
 *          [top]               |
 *          [features]          |
 *          [ ]                 |
 *          [bottom]            |
 *          [features]          |
 *      Row #4 TODO: Bottom Widget
 */
@Composable
@Preview
fun app() {
    MaterialTheme {
        Column {
            IDEMenuRow()
            Row(
                modifier = Modifier.background(Color.LightGray).defaultMinSize(100.dp, 4.dp).fillMaxWidth().height(8.dp)
            ) {}
            IDEContentRow()
            // TODO: Bottom Widget
        }
    }
}

fun getAndStartApp() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        app()
    }
}
