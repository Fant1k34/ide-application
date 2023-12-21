package application.ui.Widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import application.store.MainStore.compiler.Compiler
import application.store.MainStore.mainStore
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun codeRunner() {
    var backgroundWork: Job? = null
    var codeExecutionThread = newFixedThreadPoolContext(1, "Code Runner")

    Button(onClick = {
        try {
            codeExecutionThread.close()
            codeExecutionThread = newFixedThreadPoolContext(1, "Code Runner")
            currentCodeOutput.value = ""

            backgroundWork = GlobalScope.launch {
                launch(codeExecutionThread) {
                    val compiler = Compiler(mainStore.lineToShow.value ?: return@launch)

                    compiler.run { currentCodeOutput.value += (it + "\n") }
                }
            }

            println("Successful compilation")
        } catch (e: Exception) {
            println("Compilation exception ${e.message}")
        }
    }) {
        Text("Запустить")
    }
    Text("Output:\n${currentCodeOutput.value}")
}
