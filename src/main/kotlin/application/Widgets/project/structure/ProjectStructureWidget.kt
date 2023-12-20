package application.Widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import application.Widgets.projectDir
import application.Widgets.setProjectDir
import application.Widgets.updateFileToEdit
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

val currentCodeOutput = mutableStateOf("")
val isProjectChosen = mutableStateOf(false)
val isModalOfDirectoryChoiceOpen = mutableStateOf(false)

@OptIn(DelicateCoroutinesApi::class)
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
        if (it.extension == "idl") {
            Button(onClick = {
                updateFileToEdit(it.absolutePath)
            }) {
                Text(it.path)
            }
        }
    }
}
