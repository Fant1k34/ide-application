package application.ui.Widgets.project.structure

import MinimalDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import application.store.MainStore.mainStore
import application.ui.modifyIDEFeature
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import kotlinx.coroutines.*
import java.io.File

val isProjectChosen = mutableStateOf(false)
val isModalOfDirectoryChoiceOpen = mutableStateOf(false)

@Composable
fun projectStructureWidget() {
    val filesInProject = remember { mutableStateOf(listOf<File>()) }
    val chosenFileInProjectStructureWidget = remember { mutableStateOf<File?>(null) }
    val isCreateModalActive = remember { mutableStateOf(false) }

    Row {
        Button(onClick = { isModalOfDirectoryChoiceOpen.value = true }) {
            Text("O")
        }

        if (chosenFileInProjectStructureWidget.value?.isFile == true) {
            Button(onClick = {
                mainStore.writeToCurrentFile()
            }) {
                Text("S")
            }
        }

        if (chosenFileInProjectStructureWidget.value?.isDirectory == true) {
            Button(onClick = {
                isCreateModalActive.value = true
            }) {
                Text("+")
            }
        }

        if (chosenFileInProjectStructureWidget.value != null) {
            Button(onClick = {
                val fileToDelete = chosenFileInProjectStructureWidget.value

                if (fileToDelete != null) {
                    mainStore.deleteRecursively(fileToDelete)
                }
            }) {
                Text("D")
            }
        }
    }

    if (isCreateModalActive.value) {
        MinimalDialog({ isFile, filename ->
            mainStore.createNewFile(
                isFile, chosenFileInProjectStructureWidget.value?.path ?: return@MinimalDialog, filename
            )
        }, { isCreateModalActive.value = false })
    }

    if (!isProjectChosen.value) {
        DirectoryPicker(isModalOfDirectoryChoiceOpen.value) { path ->
            isModalOfDirectoryChoiceOpen.value = false

            if (path != null) {
                mainStore.setProjectPath(path)
            }
        }
    }
    val projectPath = mainStore.projectPath.value ?: return


    filesInProject.value.reversed().forEach {
        if (it.extension == "idl" || it.isDirectory) {
            Button(
                onClick = {
                    if (it.isDirectory) {
                        println("Директория")
                    } else {
                        mainStore.setCurrentFile(it)
                    }

                    chosenFileInProjectStructureWidget.value = it
                },
                colors = if (chosenFileInProjectStructureWidget.value?.path == it.path)
                    ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
                contentPadding = PaddingValues(),
                modifier = Modifier.padding(
                    top = modifyIDEFeature(0.dp),
                    bottom = modifyIDEFeature(0.dp),
                ).height(modifyIDEFeature(24.dp))
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            ) {
                // Удаляем лишнюю часть пути
                var fileName = it.path.slice(projectPath.length until it.path.length)
                fileName = projectPath.split(File.separator).last() + fileName

                val splittedFilename = fileName.split(File.separator)
                val onlyFirstName = splittedFilename.slice(0 until splittedFilename.size - 1)
                val onlyLastName = splittedFilename.last()

                Text(
                    " ".repeat(onlyFirstName.joinToString(File.separator).length) + onlyLastName,
                    color = if (chosenFileInProjectStructureWidget.value?.path == it.path) Color.White else Color.Black
                )
            }
        }
    }

    LaunchedEffect(projectPath) {
        filesInProject.value = File(projectPath).walk(FileWalkDirection.BOTTOM_UP).toList()
    }
}
