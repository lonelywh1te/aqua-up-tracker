package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CheckableListBottomSheet(
    items: List<T>,
    checkedItems: List<T>,
    text: @Composable (T) -> String,
    onConfirm: (List<T>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var checkedItems: List<T> by remember { mutableStateOf(checkedItems) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            CheckableList(
                modifier = Modifier.heightIn(max = 450.dp),
                items = items,
                text = text,
                checkedItems = checkedItems,
                onItemsChecked = { checkedItems = it }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.base_spacing))
                    .height(56.dp),
                onClick = {
                    onConfirm(checkedItems)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.submit))
            }
        }

    }
}