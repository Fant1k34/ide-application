package application.Widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import application.Widgets.projectDir
import application.Widgets.setProjectDir
import application.Widgets.updateBufferObject
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import java.io.File

val currentCodeOutput = mutableStateOf("")
val isProjectChosen = mutableStateOf(false)
val isModalOfDirectoryChoiceOpen = mutableStateOf(false)

@Composable
fun projectStructureWidget() {
    Button(onClick = { isModalOfDirectoryChoiceOpen.value = true }) {
        Text("Open new project")
    }

    if (!isProjectChosen.value) {
        DirectoryPicker(isModalOfDirectoryChoiceOpen.value) { path ->
            isModalOfDirectoryChoiceOpen.value = false

            if (path != null) {
                setProjectDir(path)
            }
        }
    }
    val projectPath = projectDir.value ?: return

    File(projectPath).walk(FileWalkDirection.BOTTOM_UP).forEach {
        Button(onClick = { updateBufferObject(it.readLines().joinToString("\n")) }) {
            Text(it.path)
        }
    }
}
