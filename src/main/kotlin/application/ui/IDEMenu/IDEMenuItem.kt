package application.ui.IDEMenu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.ui.modifyIDEMenu

/**
 * IDEMenuItem is button in top IDE menu with optionText name
 * By clicking on its name the dropdown element appears based on optionItems
 * By clicking on option from optionItems the corresponding function calls
 * If this IDEMenuItem is the first item or the last one in the IDEMenu, you should mark true corresponding argument
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IDEMenuItem(
    optionText: String,
    optionItems: Map<String, () -> Unit>,
    isFirst: Boolean = false,
    isLast: Boolean = false,
) {
    val isOpen = remember { mutableStateOf(false) }
    val setClose = { isOpen.value = false }
    val setOpen = { isOpen.value = true }

    Box {
        Button(
            onClick = setOpen,
            shape = RoundedCornerShape(
                0f,
                0f,
                if (isLast) modifyIDEMenu(8f) else 0f,
                if (isFirst) modifyIDEMenu(8f) else 0f
            ),
            colors = ButtonDefaults.outlinedButtonColors(),
            contentPadding = PaddingValues(),
            modifier = Modifier.height(modifyIDEMenu(24.dp)).defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
        ) {
            Text(
                optionText,
                color = Color.Black,
                fontSize = modifyIDEMenu(14.sp),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = modifyIDEMenu(4.dp))
            )
        }

        DropdownMenu(
            DropdownMenuState(
                if (isOpen.value) (DropdownMenuState.Status.Open(
                    position = Offset(0F, modifyIDEMenu(30F))
                )) else DropdownMenuState.Status.Closed
            ), onDismissRequest = setClose
        ) {
            optionItems.map {
                Text(
                    it.key, color = Color.Black,
                    fontSize = modifyIDEMenu(14.sp),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.onClick {
                        it.value()
                        setClose()
                    }.padding(all = modifyIDEMenu(2.dp))
                )
            }
        }
    }
}