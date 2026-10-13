package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.ui.components.TacticalCanvasInsignia
import com.example.ui.theme.*
import com.example.viewmodel.MilitaryGameViewModel

@Composable
fun OfficerDossierScreen(
    viewModel: MilitaryGameViewModel,
    profile: PlayerProfile,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showResetConfirm by remember { mutableStateOf(false) }
    var allLogsExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MilitaryBlack)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. COMPACT DOSSIER CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryGold)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TacticalCanvasInsignia(rank = profile.rank, size = 44.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${profile.rank.abbreviation}. ${profile.name}",
                            color = MilitaryTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "NRP ${profile.nrp} • Korps ${profile.branch.title}",
                            color = MilitaryRadarGreen,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${profile.post.name} (TMT 2000)",
                            color = MilitaryGold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // 2. COMPACT RIBBON RACK (TANDA JASA)
        item {
            Text(
                text = "PAPAN TANDA KEHORMATAN (${profile.medals.size})",
                color = MilitaryGold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MilitaryDarkSurface),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    profile.medals.forEach { medal ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 18.dp, height = 9.dp)
                                    .background(
                                        color = Color(android.graphics.Color.parseColor(medal.ribbonColorHex)),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                                    .border(0.5.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(1.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = medal.name,
                                color = MilitaryTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "+${medal.prestigeBonus} Prestasi",
                                color = MilitaryGold,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // 3. BREVETS INLINE ROW
        item {
            Text(
                text = "KUALIFIKASI & BREVET MILITER",
                color = MilitaryGold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    profile.brevets.forEach { brevet ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = MilitaryGold, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = brevet, color = MilitaryTextPrimary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 4. LOGBOOK (COLLAPSIBLE / SHOW TOP 4 OR ALL)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CATATAN RIWAYAT DINAS",
                    color = MilitaryGold,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (allLogsExpanded) "CIUTKAN" else "LIHAT SEMUA (${profile.careerLogs.size})",
                    color = MilitaryTacticalGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { allLogsExpanded = !allLogsExpanded }
                )
            }
        }

        val displayedLogs = if (allLogsExpanded) profile.careerLogs.reversed() else profile.careerLogs.reversed().take(4)

        items(displayedLogs) { log ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (log.isMilestone) Color(0xFF16251C) else MilitaryDarkSurface
                ),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (log.isMilestone) MilitaryTacticalGreen else MilitaryBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = log.title,
                            color = if (log.isMilestone) MilitaryTacticalGreen else MilitaryTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = log.details,
                            color = MilitaryTextSecondary,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "${log.month}/${log.year}",
                        color = MilitaryGold,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // 5. RESET BUTTON
        item {
            OutlinedButton(
                onClick = { showResetConfirm = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .testTag("reset_career_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MilitaryHostileRed),
                border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryHostileRed),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("RESET KARIER KE KADET 2000", fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Konfirmasi Reset Karier", color = MilitaryGold, fontSize = 14.sp) },
            text = { Text("Mulai ulang dari Kadet tahun 2000?", color = MilitaryTextPrimary, fontSize = 12.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetGame()
                        showResetConfirm = false
                        Toast.makeText(context, "Karier direset ke 2000.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MilitaryHostileRed)
                ) {
                    Text("RESET", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("BATAL", color = MilitaryTextSecondary)
                }
            },
            containerColor = MilitaryDarkSurface
        )
    }
}
