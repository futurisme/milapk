package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.ui.components.TacticalCanvasInsignia
import com.example.ui.components.TacticalRadarSweepView
import com.example.ui.theme.*
import com.example.viewmodel.MilitaryGameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: MilitaryGameViewModel,
    profile: PlayerProfile,
    onNavigateToWargame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply {
            maximumFractionDigits = 0
        }
    }

    // Collapsible section states for extreme space efficiency
    var statsExpanded by remember { mutableStateOf(true) }
    var dutiesExpanded by remember { mutableStateOf(true) }
    var radarExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MilitaryBlack)
            .verticalScroll(scrollState)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. COMPACT COMMAND COCKPIT (Status Dinas + Profil + Maju Bulan Terpadu)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorderHighlight)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Baris Atas: Tanggal & Tombol Maju Bulan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(MilitaryRadarGreen, RoundedCornerShape(3.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${profile.monthName.uppercase()} ${profile.currentYear}",
                            color = MilitaryTextGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${profile.currentYear - 2000} Thn Dinas | ${profile.age} Thn)",
                            color = MilitaryTextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    // Compact Advance Button
                    Button(
                        onClick = { viewModel.advanceMonth() },
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("advance_month_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryTacticalGreen),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Maju",
                            tint = MilitaryBlack,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "MAJU BULAN",
                            color = MilitaryBlack,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                }

                HorizontalDivider(
                    color = MilitaryBorder,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Baris Tengah: Insignia + Nama + Jabatan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TacticalCanvasInsignia(
                        rank = profile.rank,
                        size = 38.dp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${profile.rank.abbreviation}. ${profile.name}",
                                color = MilitaryTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "NRP ${profile.nrp}",
                                color = MilitaryTextMuted,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = "${profile.rank.title} • ${profile.post.name}",
                            color = MilitaryGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }

                // Baris Bawah: Metrik Cepat Rapat (3 Kolom)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(MilitaryDarkSurface, RoundedCornerShape(6.dp))
                        .border(1.dp, MilitaryBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("PASUKAN", color = MilitaryTextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Text("${profile.post.troopsUnderCommand} Pers", color = MilitaryTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PRESTASI", color = MilitaryTextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Text("${profile.meritPoints} Pts", color = MilitaryGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("ANGGARAN", color = MilitaryTextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Text(
                            currencyFormatter.format(profile.operationalFunds),
                            color = MilitaryRadarGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 2. COLLAPSIBLE STATS (2-Column Dense Grid)
        CollapsibleSectionHeader(
            title = "KAPASITAS PERWIRA & MORAL",
            isExpanded = statsExpanded,
            badge = "${(profile.leadership + profile.tacticalAcumen + profile.discipline) / 3}% AVG",
            onToggle = { statsExpanded = !statsExpanded }
        )

        AnimatedVisibility(
            visible = statsExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Row 1
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactStatBar("Kepemimpinan", profile.leadership, MilitaryGold, Modifier.weight(1f))
                        CompactStatBar("Taktik Tempur", profile.tacticalAcumen, MilitaryRadarGreen, Modifier.weight(1f))
                    }
                    // Row 2
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactStatBar("Disiplin Pasukan", profile.discipline, MilitaryTacticalGreen, Modifier.weight(1f))
                        CompactStatBar("Kebugaran Fisik", profile.physicalCondition, MilitaryFriendlyCyan, Modifier.weight(1f))
                    }
                    // Row 3
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactStatBar("Pengaruh Mabes", profile.politicalInfluence, MilitaryAlertAmber, Modifier.weight(1f))
                        CompactStatBar("Moral Prajurit", profile.troopsMorale, MilitaryOlive, Modifier.weight(1f))
                    }
                }
            }
        }

        // 3. COLLAPSIBLE QUICK DUTY ACTIONS (Grid Rapat 4 Tombol)
        CollapsibleSectionHeader(
            title = "TINDAKAN KOMANDO & DINAS",
            isExpanded = dutiesExpanded,
            badge = "OPS",
            onToggle = { dutiesExpanded = !dutiesExpanded }
        )

        AnimatedVisibility(
            visible = dutiesExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CompactDutyButton(
                        title = "Latihan Tempur",
                        costText = "-25jt | +Taktik",
                        icon = Icons.Default.GpsFixed,
                        modifier = Modifier.weight(1f).testTag("action_combat_drill"),
                        onClick = {
                            val res = viewModel.executeOfficerDuty("LATIHAN_TEMPUR")
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                        }
                    )
                    CompactDutyButton(
                        title = "Inspeksi Moral",
                        costText = "-10jt | +Moral",
                        icon = Icons.Default.Groups,
                        modifier = Modifier.weight(1f).testTag("action_inspection"),
                        onClick = {
                            val res = viewModel.executeOfficerDuty("INSPEKSI_MORAL")
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CompactDutyButton(
                        title = "Patroli Batas",
                        costText = "-8jt | +Fisik",
                        icon = Icons.Default.Explore,
                        modifier = Modifier.weight(1f).testTag("action_patrol"),
                        onClick = {
                            val res = viewModel.executeOfficerDuty("PATROLI_BATAS")
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                        }
                    )
                    CompactDutyButton(
                        title = "Lobi Mabes",
                        costText = "-15jt | +Pengaruh",
                        icon = Icons.Default.AccountBalance,
                        modifier = Modifier.weight(1f).testTag("action_headquarters"),
                        onClick = {
                            val res = viewModel.executeOfficerDuty("KONSULTASI_MABES")
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // 4. SHORTCUT KE PUSAT OPERASI TEMPUR 2D
        Button(
            onClick = onNavigateToWargame,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .testTag("nav_wargame_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MilitaryElevatedSurface),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryGold),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = MilitaryGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "BUKA PUSAT OPERASI TEMPUR & WARGAME 2D",
                color = MilitaryGold,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }

        // 5. COLLAPSIBLE RADAR INTELIJEN
        CollapsibleSectionHeader(
            title = "RADAR INTELIJEN TAKTIS",
            isExpanded = radarExpanded,
            badge = if (radarExpanded) "AKTIF" else "MINIMIZE",
            onToggle = { radarExpanded = !radarExpanded }
        )

        AnimatedVisibility(
            visible = radarExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            TacticalRadarSweepView(
                units = viewModel.tacticalUnits.collectAsState().value
            )
        }
    }
}

@Composable
private fun CollapsibleSectionHeader(
    title: String,
    isExpanded: Boolean,
    badge: String,
    onToggle: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "arrow_rot")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 2.dp, horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                color = MilitaryGold,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .background(MilitaryElevatedSurface, RoundedCornerShape(3.dp))
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(badge, color = MilitaryTacticalGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            }
        }

        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "Tutup" else "Buka",
            tint = MilitaryGold,
            modifier = Modifier
                .size(18.dp)
                .rotate(rotation)
        )
    }
}

@Composable
private fun CompactStatBar(
    label: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = MilitaryTextSecondary, fontSize = 10.sp)
            Text(
                text = "$value",
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = color,
            trackColor = MilitaryBlack
        )
    }
}

@Composable
private fun CompactDutyButton(
    title: String,
    costText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(46.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MilitaryElevatedSurface),
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MilitaryTacticalGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    color = MilitaryTextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = costText,
                    color = MilitaryTextSecondary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1
                )
            }
        }
    }
}
