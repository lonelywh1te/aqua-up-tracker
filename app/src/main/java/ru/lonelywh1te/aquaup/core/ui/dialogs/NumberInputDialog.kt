package ru.lonelywh1te.aquaup.core.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.core.ui.theme.AquaUpTheme

@Composable
fun NumberInputDialog(
    title: String,
    initValue: Int = 0,
    onConfirm: (Int) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var fieldValue by remember { mutableStateOf(initValue.toString()) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .width(260.dp)
                .height(250.dp)
                .padding(dimensionResource(R.dimen.screen_padding)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.screen_padding)),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedTextField(
                    textStyle = MaterialTheme.typography.displayMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    value = fieldValue,
                    onValueChange = { changedValue ->
                        if (changedValue.length <= 4) {
                            fieldValue = changedValue.filter { it.isDigit() }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(onClick = { onConfirm(fieldValue.toIntOrNull() ?: 0) },) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NumberInputDialogPreview() {
    AquaUpTheme {
        NumberInputDialog(
            title = "Заголовок"
        )
    }
}