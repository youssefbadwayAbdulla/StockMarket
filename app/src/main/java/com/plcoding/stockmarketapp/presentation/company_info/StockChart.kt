package com.plcoding.stockmarketapp.presentation.company_info

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.stockmarketapp.domain.model.IntraDayInfo
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours

@Composable
fun StockChart(
    infos: List<IntraDayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = remember(infos) {
        (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }
    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }
    val density = LocalDensity.current
    val textPint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spaceParHour = (size.width - spacing) / infos.size
        (0 until infos.size - 1 step 2).forEach {
            val info = infos[it]
            val hour = info.close.hours
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    /* text = */ hour.toString(),
                    /* x = */ spacing + it * spaceParHour,
                    /* y = */ size.height - 5,
                    /* paint = */ textPint
                )
            }
        }
        val priceStep = (upperValue - lowerValue) / 5f
        (0..5).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    /* text = */ round(lowerValue + priceStep * i).toString(),
                    /* x = */ 30f,
                    /* y = */ size.height - spacing - i * size.height / 5f,
                    /* paint = */ textPint
                )
            }
        }
        var lastX = 0f
        val stockPath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spaceParHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spaceParHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 + x2) / 2f
                quadraticBezierTo(
                    x1 = x1,
                    y1 = y1,
                    x2 = lastX,
                    y2 = (y1 + y2) / 2f
                )
            }
        }
        val filPath = android.graphics.Path(stockPath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(x = lastX, y = size.height - spacing)
                lineTo(x = spacing, y = size.height - spacing)
                close()
            }
        drawPath(
            path = filPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = stockPath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap= StrokeCap.Round
        )
        )
    }
}