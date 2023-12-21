package application.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class UpdateCoefficientMode {
    Increase,
    Decrease,
}

fun modifyElement(value: Float, coefficient: Float): Float = (value * coefficient)
fun modifyElement(value: Dp, coefficient: Float): Dp = (value.value * coefficient).dp
fun modifyElement(value: TextUnit, coefficient: Float): TextUnit = (value.value * coefficient).sp

fun updateCoefficient(
    coefficient: MutableState<Float>,
    min: Double,
    max: Double,
    mode: UpdateCoefficientMode
): Boolean {
    if (mode == UpdateCoefficientMode.Increase && coefficient.value < max) {
        coefficient.value = (coefficient.value + 0.1).toFloat()

        return true
    }

    if (mode == UpdateCoefficientMode.Decrease && coefficient.value > min) {
        coefficient.value = (coefficient.value - 0.1).toFloat()

        return true
    }

    return false
}


var IDEMenuSizeCoefficient = mutableStateOf(1.2f)

fun increaseIDEMenuSizeCoefficient() {
    updateCoefficient(IDEMenuSizeCoefficient, 0.5, 2.5, UpdateCoefficientMode.Increase)
}

fun decreaseIDEMenuSizeCoefficient() {
    updateCoefficient(IDEMenuSizeCoefficient, 0.5, 2.5, UpdateCoefficientMode.Decrease)
}

fun modifyIDEMenu(value: Float): Float = modifyElement(value, IDEMenuSizeCoefficient.value)
fun modifyIDEMenu(value: Dp): Dp = modifyElement(value, IDEMenuSizeCoefficient.value)
fun modifyIDEMenu(value: TextUnit): TextUnit = modifyElement(value, IDEMenuSizeCoefficient.value)

// max = 2
var IDEFeatureCoefficient = mutableStateOf(1.2f)

fun increaseIDEFeatureCoefficient() {
    updateCoefficient(IDEFeatureCoefficient, 0.5, 2.0, UpdateCoefficientMode.Increase)
}

fun decreaseIDEFeatureCoefficient() {
    updateCoefficient(IDEFeatureCoefficient, 0.5, 2.0, UpdateCoefficientMode.Decrease)
}

fun modifyIDEFeature(value: Float): Float = modifyElement(value, IDEFeatureCoefficient.value)
fun modifyIDEFeature(value: Dp): Dp = modifyElement(value, IDEFeatureCoefficient.value)
fun modifyIDEFeature(value: TextUnit): TextUnit = modifyElement(value, IDEFeatureCoefficient.value)
