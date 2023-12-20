package application.Widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import application.Widgets.bufferObject
import application.Widgets.projectDir
import application.Widgets.setProjectDir
import application.compiler.Compiler
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext

val currentCodeOutput = mutableStateOf("")
val isProjectChosen = mutableStateOf(false)
val isModalOfDirectoryChoiceOpen = mutableStateOf(false)

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
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

    if (projectDir.value == null) return

    Text(projectDir.value ?: "Meh")
}
