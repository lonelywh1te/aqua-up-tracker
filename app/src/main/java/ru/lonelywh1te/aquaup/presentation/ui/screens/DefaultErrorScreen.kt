package ru.lonelywh1te.aquaup.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.R

@Composable
fun DefaultErrorScreen(
    message: String,
    details: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize()
            .padding(dimensionResource(R.dimen.screen_padding)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var showDetails by remember { mutableStateOf(false) }

        Column(
            modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.padding(dimensionResource(R.dimen.base_spacing)),
                imageVector = ImageVector.vectorResource(R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )

            if (showDetails && details != null) {
                Text(
                    text = details,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        FilledTonalButton (
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = { showDetails = !showDetails },
        ) {
            Text(
                text = if (!showDetails) "Показать детали" else "Скрыть делали"
            )
        }
    }
}