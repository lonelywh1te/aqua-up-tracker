package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme
import ru.lonelywh1te.aquaup.presentation.ui.utils.toRelativeDateString
import ru.lonelywh1te.aquaup.presentation.ui.utils.valueStringRes
import java.time.LocalTime

@Composable
fun <T> SelectableList(
    modifier: Modifier = Modifier,
    items: List<T>,
    text: @Composable (T) -> String,
    onItemSelected: ((T) -> Unit)? = null,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(items) { index, item ->
            ListItem(
                label = text(item),
                onClick = { onItemSelected?.invoke(item) }
            )

            if (index < items.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun <T> CheckableList(
    modifier: Modifier = Modifier,
    items: List<T>,
    checkedItems: List<T> = emptyList(),
    text: @Composable (T) -> String,
    onItemsChecked: ((List<T>) -> Unit)? = null,
) {
    var checkedItems: List<T> by remember { mutableStateOf(checkedItems) }

    LazyColumn(modifier = modifier) {
        itemsIndexed(items) { index, item ->
            CheckboxListItem(
                label = text(item),
                isChecked = checkedItems.contains(item),
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        checkedItems = checkedItems.plus(item)
                    } else {
                        checkedItems = checkedItems.minus(item)
                    }

                    onItemsChecked?.invoke(checkedItems)
                }
            )

            if (index < items.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectableListPreview() {
    AquaUpTheme {
        SelectableList(
            items = AppTheme.entries,
            text = { theme -> stringResource(theme.valueStringRes()) },
            onItemSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckableListPreview() {
    AquaUpTheme {
        CheckableList(
            items = listOf(
                LocalTime.now(),
                LocalTime.now().minusHours(1),
                LocalTime.now().minusHours(2),
            ),
            checkedItems = listOf(LocalTime.now().minusHours(2)),
            text = { it.toRelativeDateString() }
        )
    }
}