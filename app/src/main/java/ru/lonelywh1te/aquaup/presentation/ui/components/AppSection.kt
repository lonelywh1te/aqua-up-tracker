package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme

@Composable
fun AppSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String? = null,
    onValueClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            title?.let {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.base_spacing))
                )
            }

            value?.let {
                TextButton(
                    contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.base_spacing), horizontal = 0.dp),
                    onClick = { onValueClick?.invoke() }
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = ShapeDefaults.Large,
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppSectionPreview() {
    AquaUpTheme {
        AppSection(
            title = "Title",
            value = "Value"
        ) {
            LazyColumn {
                items(List(5) { "Text" }) { item ->
                    ValueListItem(
                        label = "label", value = item
                    )
                }
            }
        }
    }
}