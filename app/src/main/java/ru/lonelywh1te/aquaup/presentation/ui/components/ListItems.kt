package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme


@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    label: String,
    onClick: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .fillMaxWidth()
            .height(57.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        content?.invoke()
    }
}

@Composable
fun ValueListItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        label = label,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.alpha(0.5f),
            text = value
        )
    }
}

@Composable
fun CheckboxListItem(
    modifier: Modifier = Modifier,
    label: String,
    isChecked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    var isChecked by remember { mutableStateOf(isChecked) }

    ListItem(
        label = label,
        onClick = {
            isChecked = !isChecked
            onCheckedChange?.invoke(isChecked)
        },
        modifier = modifier
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = !isChecked
                onCheckedChange?.invoke(it)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ValueItemPreview() {
    AquaUpTheme {
        ValueListItem(label = "label", value = "value")
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxItemPreview() {
    AquaUpTheme {
        CheckboxListItem(
            label = "label",
            isChecked = true
        )
    }
}