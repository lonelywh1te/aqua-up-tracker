package ru.lonelywh1te.aquaup.presentation.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputDialog(
    initTime: LocalTime,
    onConfirm: (LocalTime) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initTime.hour,
        initialMinute = initTime.minute,
        is24Hour = true,
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .width(260.dp)

                .padding(dimensionResource(R.dimen.screen_padding)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.screen_padding)),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Выберите время",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(Modifier.height(dimensionResource(R.dimen.base_spacing)))

                TimeInput(timePickerState)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            val confirmedLocalTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            onConfirm(confirmedLocalTime)
                        }
                    ) {
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
        TimeInputDialog(
            initTime = LocalTime.now(),
            onConfirm = {},
            onDismiss = {}
        )
    }
}