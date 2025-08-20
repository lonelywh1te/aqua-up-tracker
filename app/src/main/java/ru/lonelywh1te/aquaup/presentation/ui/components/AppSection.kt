package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme

@Composable
fun AppSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
            )
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
        AppSection(title = "Title") {
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