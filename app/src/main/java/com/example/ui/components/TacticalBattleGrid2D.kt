package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OperationTheater
import com.example.model.TacticalUnit
import com.example.ui.theme.*

/**
 * 2D Interactive Tactical Battlefield & War Table Canvas
 * Menampilkan posisi koordinat pasukan, garis tembak, dan status tempur secara visual.
 */
@Composable
fun TacticalBattleGrid2D(
    theater: OperationTheater,
    units: List<TacticalUnit>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MilitaryDarkSurface, RoundedCornerShape(10.dp))
            .border(1.dp, MilitaryBorder, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        // Tactical Map Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PETA TAKTIS MEDAN: ${theater.label.uppercase()}",
                    color = MilitaryGold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kondisi: ${theater.environment}",
                    color = MilitaryTextSecondary,
                    fontSize = 10.sp
                )
            }
            Text(
                text = "GRID: MGRS 48M",
                color = MilitaryRadarGreen,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Canvas Battlefield Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MilitaryBlack, RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFF1F2F25), RoundedCornerShape(6.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw Tactical Coordinate Grid (10x10)
                val cols = 8
                val rows = 6
                val stepX = w / cols
                val stepY = h / rows

                for (i in 0..cols) {
                    val x = i * stepX
                    drawLine(
                        color = Color(0x1F39E75F),
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = 1f
                    )
                }
                for (j in 0..rows) {
                    val y = j * stepY
                    drawLine(
                        color = Color(0x1F39E75F),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f
                    )
                }

                // Decorative Contour lines (Simulating topographic terrain)
                val contourPath = Path().apply {
                    moveTo(0f, h * 0.45f)
                    cubicTo(w * 0.25f, h * 0.40f, w * 0.5f, h * 0.55f, w, h * 0.50f)
                }
                drawPath(contourPath, color = Color(0x2E86A36E), style = Stroke(width = 1.5f))

                val contourPath2 = Path().apply {
                    moveTo(0f, h * 0.70f)
                    cubicTo(w * 0.35f, h * 0.60f, w * 0.65f, h * 0.75f, w, h * 0.65f)
                }
                drawPath(contourPath2, color = Color(0x2E86A36E), style = Stroke(width = 1.5f))

                // Draw Tracers / Lines of fire between first alive friendly and first alive enemy
                val friendly = units.firstOrNull { it.isFriendly && it.health > 0 }
                val enemy = units.firstOrNull { !it.isFriendly && it.health > 0 }

                if (friendly != null && enemy != null) {
                    val fx = friendly.x * w
                    val fy = friendly.y * h
                    val ex = enemy.x * w
                    val ey = enemy.y * h

                    // Firing vector
                    drawLine(
                        color = MilitaryAlertAmber.copy(alpha = 0.5f),
                        start = Offset(fx, fy),
                        end = Offset(ex, ey),
                        strokeWidth = 1.8f
                    )
                }

                // Draw Units
                units.forEach { unit ->
                    val ux = unit.x * w
                    val uy = unit.y * h
                    val isAlive = unit.health > 0

                    if (unit.isFriendly) {
                        // Friendly Blue/Green NATO Box
                        val boxSize = 24f
                        val topLeft = Offset(ux - boxSize / 2, uy - boxSize / 2)

                        if (isAlive) {
                            // Fill
                            drawRect(
                                color = Color(0xFF0F3B25),
                                topLeft = topLeft,
                                size = Size(boxSize, boxSize)
                            )
                            // Border
                            drawRect(
                                color = MilitaryTacticalGreen,
                                topLeft = topLeft,
                                size = Size(boxSize, boxSize),
                                style = Stroke(width = 2f)
                            )
                            // NATO Infantry Cross 'X'
                            drawLine(
                                color = MilitaryTacticalGreen,
                                start = topLeft,
                                end = Offset(topLeft.x + boxSize, topLeft.y + boxSize),
                                strokeWidth = 1.5f
                            )
                            drawLine(
                                color = MilitaryTacticalGreen,
                                start = Offset(topLeft.x, topLeft.y + boxSize),
                                end = Offset(topLeft.x + boxSize, topLeft.y),
                                strokeWidth = 1.5f
                            )

                            // Health bar below
                            val barW = boxSize * 1.4f
                            val barH = 4f
                            val healthRatio = unit.health.toFloat() / unit.maxHealth.toFloat()
                            val barX = ux - barW / 2
                            val barY = uy + boxSize / 2 + 3f

                            drawRect(
                                color = Color(0x55000000),
                                topLeft = Offset(barX, barY),
                                size = Size(barW, barH)
                            )
                            drawRect(
                                color = MilitaryTacticalGreen,
                                topLeft = Offset(barX, barY),
                                size = Size(barW * healthRatio, barH)
                            )
                        } else {
                            // Fallen unit marker
                            drawCircle(color = Color(0x44888888), radius = 6f, center = Offset(ux, uy))
                        }
                    } else {
                        // Hostile Red Diamond
                        val diamondRadius = 14f
                        val diamondPath = Path().apply {
                            moveTo(ux, uy - diamondRadius)
                            lineTo(ux + diamondRadius, uy)
                            lineTo(ux, uy + diamondRadius)
                            lineTo(ux - diamondRadius, uy)
                            close()
                        }

                        if (isAlive) {
                            drawPath(diamondPath, color = Color(0xFF4A1010), style = Fill)
                            drawPath(diamondPath, color = MilitaryHostileRed, style = Stroke(width = 2f))

                            // Inner dot
                            drawCircle(color = MilitaryHostileRed, radius = 3f, center = Offset(ux, uy))

                            // Health bar
                            val barW = diamondRadius * 2f
                            val barH = 4f
                            val healthRatio = unit.health.toFloat() / unit.maxHealth.toFloat()
                            val barX = ux - barW / 2
                            val barY = uy + diamondRadius + 4f

                            drawRect(
                                color = Color(0x55000000),
                                topLeft = Offset(barX, barY),
                                size = Size(barW, barH)
                            )
                            drawRect(
                                color = MilitaryHostileRed,
                                topLeft = Offset(barX, barY),
                                size = Size(barW * healthRatio, barH)
                            )
                        } else {
                            // Destroyed hostile marker
                            drawLine(
                                color = Color(0x66EF4444),
                                start = Offset(ux - 8f, uy - 8f),
                                end = Offset(ux + 8f, uy + 8f),
                                strokeWidth = 2f
                            )
                            drawLine(
                                color = Color(0x66EF4444),
                                start = Offset(ux - 8f, uy + 8f),
                                end = Offset(ux + 8f, uy - 8f),
                                strokeWidth = 2f
                            )
                        }
                    }
                }
            }
        }

        // Units Status List Under Battlefield
        Column(modifier = Modifier.padding(top = 8.dp)) {
            units.forEach { unit ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    if (unit.isFriendly) MilitaryTacticalGreen else MilitaryHostileRed,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = unit.name,
                            color = if (unit.health > 0) MilitaryTextPrimary else MilitaryTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = if (unit.health > 0) "${unit.health}/${unit.maxHealth} HP" else "[LUMPUH / KIA]",
                        color = if (unit.health > 0) {
                            if (unit.isFriendly) MilitaryTacticalGreen else MilitaryHostileRed
                        } else MilitaryTextMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
