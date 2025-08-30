package ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BarChartStyle(
    val barSpacing: Dp,
    val barCornerRadius: Dp,
    val xAxisStrokeWidth: Dp,
    val yAxisStrokeWidth: Dp,
    val targetStrokeWidth: Dp,
    val gridStrokeWidth: Dp,
    val pointWidth: Dp,
    val barColor: Color,
    val targetColor: Color,
    val axisColor: Color,
    val gridColor: Color,
    val textColor: Color,
    val textStyle: TextStyle,
) {
    companion object {
        val default: BarChartStyle = BarChartStyle(
            barSpacing = 2.dp,
            barCornerRadius = 8.dp,
            barColor = Color.Blue,
            targetColor = Color.Red,
            axisColor = Color.Black,
            gridColor = Color.LightGray,
            textColor = Color.Black,
            textStyle = TextStyle.Default,
            xAxisStrokeWidth = 1.dp,
            yAxisStrokeWidth = 1.dp,
            pointWidth = 2.dp,
            targetStrokeWidth = 1.dp,
            gridStrokeWidth = 1.dp,
        )
    }
}
