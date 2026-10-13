package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.MilitaryRank
import com.example.model.RankTier
import com.example.ui.theme.MilitaryBrass
import com.example.ui.theme.MilitaryGold
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 2D Canvas Procedural Insignia Engine
 * Menggambar lencana dan tanda pangkat militer secara matematis presisi tanpa lag/bitmap overhead.
 */
@Composable
fun TacticalCanvasInsignia(
    rank: MilitaryRank,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            when (rank.tier) {
                RankTier.AKADEMI -> {
                    // Chevron Kadet / Taruna
                    drawCadetChevron(w, h)
                }
                RankTier.PAMA -> {
                    // Balok Perunggu/Emas Perwira Pertama (1, 2, atau 3)
                    drawOfficerBars(w, h, rank.starsOrEmblemCount)
                }
                RankTier.PAMEN -> {
                    // Melati Emas Perwira Menengah (1, 2, atau 3)
                    drawMelatiEmblem(w, h, rank.starsOrEmblemCount)
                }
                RankTier.PATI -> {
                    // Bintang Emas Perwira Tinggi (1, 2, 3, 4, 5)
                    drawGeneralStars(w, h, rank.starsOrEmblemCount, rank == MilitaryRank.JENDERAL_BESAR)
                }
            }
        }
    }
}

private fun DrawScope.drawCadetChevron(w: Float, h: Float) {
    val path = Path().apply {
        moveTo(w * 0.15f, h * 0.35f)
        lineTo(w * 0.5f, h * 0.65f)
        lineTo(w * 0.85f, h * 0.35f)
        lineTo(w * 0.85f, h * 0.5f)
        lineTo(w * 0.5f, h * 0.8f)
        lineTo(w * 0.15f, h * 0.5f)
        close()
    }
    drawPath(path, color = MilitaryGold, style = Fill)
    drawPath(path, color = MilitaryBrass, style = Stroke(width = 2f))
}

private fun DrawScope.drawOfficerBars(w: Float, h: Float, barCount: Int) {
    val barWidth = w * 0.16f
    val barHeight = h * 0.65f
    val gap = w * 0.10f
    val totalWidth = (barCount * barWidth) + ((barCount - 1) * gap)
    val startX = (w - totalWidth) / 2f
    val startY = (h - barHeight) / 2f

    for (i in 0 until barCount) {
        val x = startX + (i * (barWidth + gap))
        drawRoundRect(
            color = Color(0xFFC88232), // Bronze/Gold tone
            topLeft = Offset(x, startY),
            size = Size(barWidth, barHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = MilitaryGold,
            topLeft = Offset(x, startY),
            size = Size(barWidth, barHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f),
            style = Stroke(width = 2.5f)
        )
    }
}

private fun DrawScope.drawMelatiEmblem(w: Float, h: Float, count: Int) {
    val emblemRadius = w * 0.18f
    val centerY = h / 2f
    val gap = w * 0.30f
    val totalWidth = (count - 1) * gap
    val startX = (w / 2f) - (totalWidth / 2f)

    for (i in 0 until count) {
        val cx = startX + (i * gap)
        drawMelatiFlower(cx, centerY, emblemRadius)
    }
}

private fun DrawScope.drawMelatiFlower(cx: Float, cy: Float, radius: Float) {
    // 5 Petals Melati
    val petals = 5
    for (i in 0 until petals) {
        val angle = (i * 2 * PI / petals).toFloat() - (PI / 2).toFloat()
        val px = cx + cos(angle) * (radius * 0.6f)
        val py = cy + sin(angle) * (radius * 0.6f)
        drawCircle(
            color = MilitaryGold,
            radius = radius * 0.45f,
            center = Offset(px, py)
        )
    }
    // Center bud
    drawCircle(color = MilitaryBrass, radius = radius * 0.35f, center = Offset(cx, cy))
    drawCircle(color = Color.White, radius = radius * 0.15f, center = Offset(cx, cy))
}

private fun DrawScope.drawGeneralStars(w: Float, h: Float, starCount: Int, isGrandGeneral: Boolean) {
    val starRadius = if (starCount <= 3) w * 0.15f else w * 0.11f
    val centerY = h / 2f

    if (starCount == 1) {
        drawFivePointStar(w / 2f, centerY, starRadius)
    } else if (starCount <= 3) {
        val gap = w * 0.28f
        val startX = (w / 2f) - ((starCount - 1) * gap / 2f)
        for (i in 0 until starCount) {
            val cx = startX + (i * gap)
            drawFivePointStar(cx, centerY, starRadius)
        }
    } else if (starCount == 4) {
        // Formasi 4 bintang sejajar melengkung
        val gap = w * 0.21f
        val startX = (w / 2f) - (1.5f * gap)
        for (i in 0 until 4) {
            val cx = startX + (i * gap)
            val cy = centerY + if (i == 0 || i == 3) 4f else -4f
            drawFivePointStar(cx, cy, starRadius)
        }
    } else {
        // 5 Bintang Melingkar / Jenderal Besar
        val radiusArc = w * 0.32f
        for (i in 0 until 5) {
            val angle = ((i - 2) * 28f) * (PI / 180f).toFloat() - (PI / 2).toFloat()
            val cx = (w / 2f) + cos(angle) * radiusArc
            val cy = (h * 0.6f) + sin(angle) * radiusArc
            drawFivePointStar(cx, cy, starRadius)
        }
        if (isGrandGeneral) {
            // Crossed swords golden accent under stars
            drawLine(
                color = MilitaryGold,
                start = Offset(w * 0.25f, h * 0.75f),
                end = Offset(w * 0.75f, h * 0.85f),
                strokeWidth = 3f
            )
            drawLine(
                color = MilitaryGold,
                start = Offset(w * 0.75f, h * 0.75f),
                end = Offset(w * 0.25f, h * 0.85f),
                strokeWidth = 3f
            )
        }
    }
}

private fun DrawScope.drawFivePointStar(cx: Float, cy: Float, radius: Float) {
    val path = Path()
    val innerRadius = radius * 0.42f
    val points = 5
    var first = true

    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angle = (i * PI / points).toFloat() - (PI / 2).toFloat()
        val x = cx + cos(angle) * r
        val y = cy + sin(angle) * r
        if (first) {
            path.moveTo(x, y)
            first = false
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    drawPath(path, color = MilitaryGold, style = Fill)
    drawPath(path, color = MilitaryBrass, style = Stroke(width = 1.5f))
}
