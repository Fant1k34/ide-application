package application.IDEMenu

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IDEMenuItem(
    // option - enum элемент строки настройки IDE
    option: IDEMenuOptions,
    // optionText - текст настройки IDE (например как File, Edit, View, Navigate... в IDEA)
    optionText: String,
    // optionItems - Строка из вложенных элементов в этот option с действиями по кликам на неё
    optionItems: Map<String, () -> Unit>,
    // Является ли первой вкладкой
    isFirst: Boolean = false,
    // Является ли последней вкладкой
    isLast: Boolean = false,
) {
    val isOpen = remember { mutableStateOf(false) }
    val setClose = { isOpen.value = false }
    val setOpen = { isOpen.value = true }

    Box {
        Button(
            onClick = setOpen,
            shape = RoundedCornerShape(0f, 0f, if (isLast) 8f else 0f, if (isFirst) 8f else 0f),
            colors = ButtonDefaults.outlinedButtonColors(),
            contentPadding = PaddingValues(),
            modifier = Modifier.height(24.dp).defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
        ) {
            Text(
                optionText,
                color = Color.Black,
                fontSize = 14.sp,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        DropdownMenu(
            DropdownMenuState(
                if (isOpen.value) (DropdownMenuState.Status.Open(
                    position = Offset(0F, 30F)
                )) else DropdownMenuState.Status.Closed
            ), onDismissRequest = setClose
        ) {
            optionItems.map {
                Text(
                    it.key, color = Color.Black,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.onClick {
                        it.value()
                        setClose()
                    }.padding(all = 2.dp)
                )
            }
        }
    }
}