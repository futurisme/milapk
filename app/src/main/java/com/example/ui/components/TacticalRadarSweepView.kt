package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TacticalUnit
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 2D Tactical Radar Sweep View
 * Menggunakan hardware-accelerated Canvas untuk render radar real-time 60-120 FPS tanpa alokasi objek loop.
 */
@Composable
fun TacticalRadarSweepView(
    units: List<TacticalUnit>,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_sweep")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_angle"
    )

    val pingPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ping_pulse"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MilitaryBlack, RoundedCornerShape(8.dp))
            .border(1.dp, MilitaryBorder, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        // Radar HUD Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MilitaryRadarGreen, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "RADAR INTELIJEN // SCAN AKTIF",
                    color = MilitaryRadarGreen,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "AZ: ${sweepAngle.toInt()}° | RANGE: 25 KM",
                color = MilitaryTextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Radar Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val radius = minOf(cx, cy) * 0.92f

                // Background Radial Tint
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF0D2416), Color(0xFF08120B)),
                        center = Offset(cx, cy),
                        radius = radius
                    ),
                    radius = radius,
                    center = Offset(cx, cy)
                )

                // Outer border & Range Rings
                drawCircle(color = MilitaryBorderHighlight, radius = radius, center = Offset(cx, cy), style = Stroke(width = 2f))
                drawCircle(color = Color(0x3300FF66), radius = radius * 0.66f, center = Offset(cx, cy), style = Stroke(width = 1f))
                drawCircle(color = Color(0x2200FF66), radius = radius * 0.33f, center = Offset(cx, cy), style = Stroke(width = 1f))

                // Crosshairs
                drawLine(
                    color = Color(0x3300FF66),
                    start = Offset(cx - radius, cy),
                    end = Offset(cx + radius, cy),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color(0x3300FF66),
                    start = Offset(cx, cy - radius),
                    end = Offset(cx, cy + radius),
                    strokeWidth = 1f
                )

                // Diagonal tick marks
                val radAngles = listOf(45f, 135f, 225f, 315f)
                radAngles.forEach { deg ->
                    val rad = (deg * PI / 180f).toFloat()
                    val x = cx + cos(rad) * radius
                    val y = cy + sin(rad) * radius
                    drawLine(
                        color = Color(0x1F00FF66),
                        start = Offset(cx, cy),
                        end = Offset(x, y),
                        strokeWidth = 0.8f
                    )
                }

                // Rotating Sweep Line
                val sweepRad = (sweepAngle * PI / 180f).toFloat()
                val sweepX = cx + cos(sweepRad) * radius
                val sweepY = cy + sin(sweepRad) * radius

                drawLine(
                    color = MilitaryRadarGreen.copy(alpha = 0.85f),
                    start = Offset(cx, cy),
                    end = Offset(sweepX, sweepY),
                    strokeWidth = 2f
                )

                // Trail cone (sector sweep)
                val trailAngle = sweepAngle - 35f
                val trailRad = (trailAngle * PI / 180f).toFloat()
                val trailX = cx + cos(trailRad) * radius
                val trailY = cy + sin(trailRad) * radius

                drawLine(
                    color = MilitaryRadarGreen.copy(alpha = 0.25f),
                    start = Offset(cx, cy),
                    end = Offset(trailX, trailY),
                    strokeWidth = 1f
                )

                // Units Blips
                units.forEach { unit ->
                    if (unit.health > 0) {
                        // Map relative x, y (0..1) to radar circle
                        val unitOffsetX = (unit.x - 0.5f) * 2f * (radius * 0.85f)
                        val unitOffsetY = (unit.y - 0.5f) * 2f * (radius * 0.85f)
                        val bx = cx + unitOffsetX
                        val by = cy + unitOffsetY

                        val blipColor = if (unit.isFriendly) MilitaryRadarGreen else MilitaryHostileRed
                        val blipAlpha = if (unit.isFriendly) 0.95f else pingPulse

                        // Outer Glow
                        drawCircle(
                            color = blipColor.copy(alpha = 0.3f * blipAlpha),
                            radius = 9f,
                            center = Offset(bx, by)
                        )
                        // Inner dot
                        drawCircle(
                            color = blipColor.copy(alpha = blipAlpha),
                            radius = 4.5f,
                            center = Offset(bx, by)
                        )
                    }
                }
            }
        }

        // Radar Bottom Legend
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).background(MilitaryRadarGreen, RoundedCornerShape(3.dp)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Kawan (Friendly)", color = MilitaryTextSecondary, fontSize = 10.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).background(MilitaryHostileRed, RoundedCornerShape(3.dp)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sasaran Musuh (Hostile)", color = MilitaryTextSecondary, fontSize = 10.sp)
            }
        }
    }
}
