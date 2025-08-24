package ru.lonelywh1te.aquaup.presentation.ui.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme

@Composable
fun WaterBackground(
    modifier: Modifier = Modifier,
    percentage: Float,
    waveHeight: Dp = 25.dp,
    gradientColors: List<Color>
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 100f),
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutCubic),
        label = "percentageAnimation"
    )

    val waveHeightInPx = with(LocalDensity.current) { waveHeight.toPx() }
    val infiniteTransition = rememberInfiniteTransition(label = "waveAnimation")
    val animatedWaveHeight by infiniteTransition.animateFloat(
        initialValue = -waveHeightInPx,
        targetValue = waveHeightInPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveHeightAnimation"
    )

    Canvas(modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // val waveOffset = waveHeightInPx / 2
        val waterHeight = (height / 100) * animatedPercentage.coerceIn(5f, 95f)

        val path = Path().apply {
            moveTo(0f, height - waterHeight)

            quadraticTo(
                width / 4, height - waterHeight - animatedWaveHeight,
                width / 2, height - waterHeight
            )
            quadraticTo(
                width * 0.75f, (height - waterHeight) + animatedWaveHeight,
                width, height - waterHeight
            )

            lineTo(width, height)
            lineTo(0f, height)

            close()
        }

        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = gradientColors,
                startY = height - waterHeight
            )
        )
    }
}

@Preview()
@Composable
private fun SBPreview() {
    AquaUpTheme {
        WaterBackground(
            modifier = Modifier.fillMaxHeight(),
            percentage = 100f,
            gradientColors = listOf(
                MaterialTheme.colorScheme.inversePrimary,
                Color.Transparent
            ),
        )
    }
}