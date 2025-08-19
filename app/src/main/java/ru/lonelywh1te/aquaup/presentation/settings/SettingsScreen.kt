package ru.lonelywh1te.aquaup.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.presentation.ui.components.AppSection
import ru.lonelywh1te.aquaup.presentation.ui.components.LabeledValueItem
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        AppSection(title = stringResource(R.string.main_settings)) {
            MainSettingsList()
        }

        AppSection(title = stringResource(R.string.about)) {
            AboutSettingsList()
        }
    }
}

@Composable
fun MainSettingsList(
    modifier: Modifier = Modifier
) {
    val settings = listOf(
        "Единица измерения" to "ml",
        "Цель" to "1800",
        "Напоминание" to "Каждые 3 часа",
        "Тема приложения" to "Светлая",
        "Язык" to "Русский"
    )

    Column(modifier = modifier) {
        settings.forEachIndexed { index, (label, value) ->
            LabeledValueItem(label, value)
            if (index < settings.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun AboutSettingsList(
    modifier: Modifier = Modifier
) {
    Column {
        LabeledValueItem("Версия", value = "1.0.0")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            SettingsScreen(Modifier.padding(innerPadding))
        }
    }
}


