package ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart

data class BarChartConfig (

    val target: Float? = null,

    val customMaxY: Float? = null,

    val ySteps: Int = 10,

    val xLabelFormatter: (String) -> String = { it },
    val yLabelFormatter: (Float) -> String = { value -> String.format("%.0f", value) },

)