import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MinimalDialog(onSuccess: (isFile: Boolean, filename: String) -> Unit, onClose: () -> Unit) {
    val radioOptions = listOf("Create file", "Create directory")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
    var newFileName by remember { mutableStateOf("") }

    Dialog(onCloseRequest = onClose) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
                        )
                    }
                }

                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("File or directory name") }
                )

                Button(onClick = {
                    onSuccess(selectedOption == radioOptions[0], newFileName)
                    onClose()
                }) {
                    Text("Create")
                }
            }
        }
    }
}