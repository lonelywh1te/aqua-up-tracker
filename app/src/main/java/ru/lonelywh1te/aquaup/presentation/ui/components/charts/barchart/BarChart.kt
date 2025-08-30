package ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme

@Composable
fun BarChart(
    data: List<BarChartEntry>,
    config: BarChartConfig = BarChartConfig(),
    style: BarChartStyle = BarChartStyle.default,
    modifier: Modifier = Modifier
) {
    require(config.ySteps >= 2) { "ySteps should be >= 2" }

    val textMeasurer = rememberTextMeasurer()
    val maxY = remember(config.customMaxY, data) {
        maxOf(config.customMaxY ?: 0f, data.maxOfOrNull { it.y } ?: 0f)
    }

    val xLabels = remember(data) {
        data.map { it.x }
    }
    val xLabelLayouts = remember(xLabels, style.textStyle) {
        xLabels.map { label ->
            textMeasurer.measure(
                text = AnnotatedString(config.xLabelFormatter(label)),
                style = style.textStyle
            )
        }
    }
    val maxXLabelHeight = xLabelLayouts.maxOfOrNull { it.size.height } ?: 0

    val yLabels = remember(config.ySteps, maxY) {
        List(config.ySteps) { i -> maxY * i / (config.ySteps - 1) }
    }
    val yLabelLayouts = remember(yLabels, style.textStyle) {
        yLabels.map { v ->
            textMeasurer.measure(
                text = AnnotatedString(config.yLabelFormatter(v)),
                style = style.textStyle
            )
        }
    }
    val maxYLabelWidth = yLabelLayouts.maxOf { it.size.width }

    val valueLabels = remember(data) {
        data.map { it.y }
    }
    val valueLabelsLayouts = remember(valueLabels, style.textStyle) {
        valueLabels.map { v ->
            textMeasurer.measure(
                text = AnnotatedString(config.yLabelFormatter(v)),
                style = style.textStyle
            )
        }
    }


    var chartBars by remember { mutableStateOf(emptyList<RoundRect>()) }
    var pressedBarIndex by remember { mutableStateOf<Int?>(null) }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(data) {
                detectTapGestures(
                    onPress = { offset ->
                        val index = chartBars.indexOfFirst { bar ->
                            offset.x in bar.left..bar.right && offset.y in bar.top..bar.bottom
                        }

                        if (index != -1) {
                            pressedBarIndex = index
                            tryAwaitRelease()
                            pressedBarIndex = null
                        }
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height

        val xAxisOffset = style.pointWidth.toPx() + maxXLabelHeight
        val yAxisOffset = style.pointWidth.toPx() + maxYLabelWidth

        val xAxisY = height - xAxisOffset
        val yAxisX = yAxisOffset

        val plotWidth = width - yAxisX
        val plotHeight = height - xAxisOffset

        val dataCount = data.size

        val barSpacingPx = style.barSpacing.toPx()
        val maxBarWidth = (plotWidth / data.size) - barSpacingPx
        val barWidth = maxBarWidth.coerceAtLeast(1f)

        val xStep = if (dataCount > 1) (plotWidth - barWidth) / (dataCount - 1) else 0f
        val yStep = plotHeight / (config.ySteps - 1)

        drawGrid(
            startAxisX = Offset(yAxisX, xAxisY),
            endAxisX = Offset(width, xAxisY),
            ySteps = config.ySteps,
            yStepSize = yStep,
            strokeWidth = style.gridStrokeWidth.toPx(),
            color = style.gridColor,
        )

        drawAxisY(
            start = Offset(yAxisX, 0f),
            end = Offset(yAxisX, xAxisY),
            ySteps = config.ySteps,
            yStepSize = yStep,
            strokeWidth = style.yAxisStrokeWidth.toPx(),
            pointWidth = style.pointWidth.toPx(),
            labels = yLabelLayouts,
            color = style.axisColor
        )

        config.target?.let { target ->
            drawTarget(
                target = target,
                startAxisX = Offset(yAxisX, xAxisY),
                endAxisX = Offset(width, xAxisY),
                plotHeight = plotHeight,
                strokeWidth = style.targetStrokeWidth.toPx(),
                maxY = maxY,
                color = style.targetColor
            )
        }

        drawBars(
            data = data,
            startXAxis = Offset(yAxisX, xAxisY),
            barWidth = barWidth,
            stepSize = xStep,
            plotHeight = plotHeight,
            maxY = maxY,
            color = style.barColor,
            valueColor = style.textColor,
            cornerRadius = style.barCornerRadius.toPx(),
            pressedBarIndex = pressedBarIndex,
            labels = valueLabelsLayouts,
            onDraw = { chartBars = it }
        )

        drawAxisX(
            data = data,
            start = Offset(yAxisX, xAxisY),
            end = Offset(width, xAxisY),
            strokeWidth = style.xAxisStrokeWidth.toPx(),
            pointWidth = style.pointWidth.toPx(),
            barWidth = barWidth,
            stepSize = xStep,
            labels = xLabelLayouts,
            color = style.axisColor
        )
    }
}

private fun DrawScope.drawAxisX(
    data: List<BarChartEntry>,
    start: Offset,
    end: Offset,
    strokeWidth: Float,
    pointWidth: Float,
    barWidth: Float,
    stepSize: Float,
    labels: List<TextLayoutResult>,
    color: Color,
) {
    // ось X
    drawLine(
        start = start,
        end = end,
        color = color,
        strokeWidth = strokeWidth,
    )

    data.forEachIndexed { i, entry ->
        val barCenterX = stepSize*i + barWidth/2 + start.x

        // точки оси X
        drawLine(
            color = color,
            start = Offset(barCenterX, start.y),
            end = Offset(barCenterX, start.y + pointWidth),
            strokeWidth = strokeWidth
        )

        // подписи оси X
        val textLayoutResult = labels[i]
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                barCenterX - textLayoutResult.size.width / 2,
                start.y + pointWidth * 2
            ),
            color = color
        )
    }
}

private fun DrawScope.drawAxisY(
    start: Offset,
    end: Offset,
    ySteps: Int,
    yStepSize: Float,
    strokeWidth: Float,
    pointWidth: Float,
    labels: List<TextLayoutResult>,
    color: Color,
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
    )

    for (i in 0 until ySteps) {
        val y = end.y - i*yStepSize

        // точки оси Y
        drawLine(
            color = color,
            start = Offset(start.x, y),
            end = Offset(start.x - pointWidth, y),
            strokeWidth = strokeWidth,
        )

        // подписи оси Y
        val textLayoutResult = labels[i]
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = start.x - textLayoutResult.size.width - pointWidth * 2,
                y = y - textLayoutResult.size.height / 2
            ),
            color = color
        )
    }
}

private fun DrawScope.drawGrid(
    startAxisX: Offset,
    endAxisX: Offset,
    ySteps: Int,
    yStepSize: Float,
    strokeWidth: Float,
    color: Color,
) {

    for (i in 1 until ySteps) {
        val y = startAxisX.y - i*yStepSize

        drawLine(
            start = Offset(startAxisX.x, y),
            end = Offset(endAxisX.x, y),
            color = color,
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawTarget(
    target: Float,
    startAxisX: Offset,
    endAxisX: Offset,
    plotHeight: Float,
    strokeWidth: Float,
    maxY: Float,
    color: Color,
) {
    val normalized = (target / maxY).coerceIn(0f, 1f)
    val targetY = startAxisX.y - normalized * plotHeight

    drawLine(
        start = Offset(startAxisX.x, targetY),
        end = Offset(endAxisX.x, targetY),
        color = color,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(10f, 10f)
        ),
        strokeWidth = strokeWidth,
    )
}

private fun DrawScope.drawBars(
    data: List<BarChartEntry>,
    startXAxis: Offset,
    barWidth: Float,
    stepSize: Float,
    plotHeight: Float,
    maxY: Float,
    color: Color,
    valueColor: Color,
    cornerRadius: Float,
    pressedBarIndex: Int?,
    labels: List<TextLayoutResult>,
    onDraw: (List<RoundRect>) -> Unit,
) {
    val bars: MutableList<RoundRect> = mutableListOf()

    data.forEachIndexed { i, entry ->
        val normalized = (entry.y / maxY).coerceIn(0f, 1f)
        val barTopY = startXAxis.y - normalized * plotHeight
        val barHeightPx = (startXAxis.y - barTopY)

        drawRoundRect(
            color = color,
            topLeft = Offset(stepSize*i + startXAxis.x, barTopY),
            size = Size(barWidth, barHeightPx),
            cornerRadius = CornerRadius(cornerRadius),
            alpha = if (i == pressedBarIndex) 0.8f else 1f
        )

        if (pressedBarIndex == i) {
            drawText(
                textLayoutResult = labels[i],
                topLeft = Offset(
                    stepSize*i + startXAxis.x + barWidth/2 - labels[i].size.width/2,
                    barTopY - labels[i].size.height
                ),
                color = valueColor,
            )
        }

        bars.add(RoundRect(
            rect = Rect(
                topLeft = Offset(stepSize*i + startXAxis.x, barTopY),
                bottomRight = Offset(stepSize*i + startXAxis.x + barWidth, barTopY + barHeightPx)
            ),
            cornerRadius = CornerRadius(cornerRadius)
        ))
    }

    onDraw(bars)
}

@Preview(showBackground = true)
@Composable
private fun BarChartPreview() {
    AquaUpTheme {
        val data = listOf(
            BarChartEntry("1", 500f),
            BarChartEntry("2", 700f),
            BarChartEntry("3", 925f),
            BarChartEntry("4", 500f),
            BarChartEntry("5", 420f),
            BarChartEntry("6", 126f),
            BarChartEntry("7", 462f),
        )
        BarChart(
            data = data,
            style = BarChartStyle.default.copy(
                barCornerRadius = 16.dp,
                textStyle = MaterialTheme.typography.labelSmall
            ),
            config = BarChartConfig().copy(
                target = 1800f
            ),
            modifier = Modifier.height(300.dp)
        )
    }
}