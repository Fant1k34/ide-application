package application.Widgets.project.structure

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import application.Widgets.bufferObject
import application.compiler.Compiler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext

val currentCodeOutput = mutableStateOf("")

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun projectStructureWidget() {
    var backgroundWork: Job? = null
    var codeExecutionThread = newFixedThreadPoolContext(1, "Code Runner")

    Button(onClick = {
        try {
            codeExecutionThread.close()
            codeExecutionThread = newFixedThreadPoolContext(1, "Code Runner")
            currentCodeOutput.value = ""

            backgroundWork = GlobalScope.launch {
                launch(codeExecutionThread) {
                    val compiler = Compiler(bufferObject.showText())

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
