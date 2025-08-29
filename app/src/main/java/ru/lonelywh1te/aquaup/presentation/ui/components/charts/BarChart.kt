package ru.lonelywh1te.aquaup.presentation.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme

@Composable
fun BarChart(
    barChartData: BarChartData,
    target: Float? = null,
    maxY: Float = target ?: 0f,
    ySteps: Int = 10,
    barSpacing: Dp = 2.dp,
    barCornerRadius: Dp = 4.dp,
    barColor: Color = Color.Blue,
    targetColor: Color = Color.Red,
    axisColor: Color = Color.Black,
    gridColor: Color = Color.LightGray,
    textColor: Color = axisColor,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    require(ySteps >= 2) { "ySteps steps should be >= 2" }

    val textMeasurer = rememberTextMeasurer()
    val maxY = maxOf(maxY, barChartData.data.maxOfOrNull { it.y } ?: 0f)

    val xLabels = remember { barChartData.data.map { it.x } }
    val xLabelLayouts = remember(xLabels, textStyle) {
        xLabels.map { label ->
            textMeasurer.measure(
                text = AnnotatedString(label),
                style = textStyle
            )
        }
    }
    val maxXLabelHeight = xLabelLayouts.maxOfOrNull { it.size.height } ?: 0

    val yLabels = remember { List(ySteps) { i -> maxY * i / (ySteps - 1) } }
    val yLabelLayouts = remember(yLabels, textStyle) {
        yLabels.map { v ->
            textMeasurer.measure(
                text = AnnotatedString(String.format("%.1f", v)),
                style = textStyle
            )
        }
    }
    val maxYLabelWidth = yLabelLayouts.maxOf { it.size.width }

    Canvas(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        val pointWidth = 5f
        val xAxisOffset = pointWidth + maxXLabelHeight
        val yAxisOffset = pointWidth + maxYLabelWidth

        val xAxisY = height - xAxisOffset
        val yAxisX = yAxisOffset

        val plotWidth = width - yAxisX
        val plotHeight = height - xAxisOffset

        val dataCount = barChartData.data.size
        val barSpacingPx = barSpacing.toPx()
        val maxBarWidth = (plotWidth / barChartData.data.size) - barSpacingPx
        val barWidth = maxBarWidth.coerceAtLeast(1f)

        val xStep = if (dataCount > 1) (plotWidth - barWidth) / (dataCount - 1) else 0f
        val yStep = plotHeight / (ySteps - 1)

        // ось Y
        drawLine(
            color = axisColor,
            start = Offset(yAxisX, 0f),
            end = Offset(yAxisX, xAxisY),
        )

        for (i in 0 until ySteps) {
            val y = xAxisY - i * yStep

            // сетка
            drawLine(
                color = gridColor,
                start = Offset(yAxisX, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )

            // точки оси Y
            drawLine(
                color = Color.Black,
                start = Offset(yAxisX, y),
                end = Offset(yAxisX - pointWidth, y),
            )

            // подписи оси Y
            val textLayoutResult = yLabelLayouts[i]
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = yAxisX - textLayoutResult.size.width - pointWidth * 2,
                    y = y - textLayoutResult.size.height / 2
                ),
                color = textColor
            )

            // линия цели
            target?.let {
                val normalized = (target / maxY).coerceIn(0f, 1f)
                val targetY = xAxisY - normalized * plotHeight
                drawLine(
                    color = targetColor,
                    start = Offset(yAxisX, targetY),
                    end = Offset(width, targetY),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(10f, 10f)
                    )
                )
            }
        }

        // ось X
        drawLine(
            color = axisColor,
            start = Offset(yAxisX, xAxisY),
            end = Offset(width, xAxisY),
        )

        barChartData.data.forEachIndexed { i, entry ->
            val barCenterX = xStep * i + barWidth / 2 + yAxisX

            // точки оси X
            drawLine(
                color = axisColor,
                start = Offset(barCenterX, xAxisY),
                end = Offset(barCenterX, xAxisY + pointWidth)
            )

            // столбцы
            val normalized = (entry.y / maxY).coerceIn(0f, 1f)
            val barTopY = xAxisY - normalized * plotHeight
            val barHeightPx = xAxisY - barTopY
            drawRoundRect(
                color = barColor,
                topLeft = Offset(xStep * i + yAxisX, barTopY),
                size = Size(barWidth, barHeightPx),
                cornerRadius = CornerRadius(barCornerRadius.toPx(), barCornerRadius.toPx())
            )

            // подписи оси X
            val textLayoutResult = xLabelLayouts[i]
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(barCenterX - textLayoutResult.size.width / 2, xAxisY + pointWidth * 2),
                color = textColor
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun BarChartPreview() {
    AquaUpTheme {
        BarChart(
            BarChartData(
                data = listOf(
                    BarChartEntry("пн", 500f),
                    BarChartEntry("вт", 700f),
                    BarChartEntry("ср", 2000f),
                    BarChartEntry("чт", 500f),
                    BarChartEntry("пт", 500f),
                    BarChartEntry("сб", 500f),
                    BarChartEntry("вс", 500f),
                )
            ),
            target = 1800f,
            maxY = 1800f,
            textStyle = MaterialTheme.typography.labelSmall
        )
    }
}