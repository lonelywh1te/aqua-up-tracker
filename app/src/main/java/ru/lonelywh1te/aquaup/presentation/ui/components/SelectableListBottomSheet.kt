package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectableListBottomSheet(
    items: List<T>,
    text: @Composable (T) -> String,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        SelectableList(
            items = items,
            text = text,
            onItemSelected = onItemSelected
        )
    }
}