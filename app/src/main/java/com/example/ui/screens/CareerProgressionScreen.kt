package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import com.example.model.MilitaryPostsRegistry
import com.example.model.MilitaryRank
import com.example.model.PlayerProfile
import com.example.ui.components.TacticalCanvasInsignia
import com.example.ui.theme.*
import com.example.viewmodel.MilitaryGameViewModel

@Composable
fun CareerProgressionScreen(
    viewModel: MilitaryGameViewModel,
    profile: PlayerProfile,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allRanks = remember { MilitaryRank.values().toList() }
    val nextRank = profile.rank.nextRank()
    val canPromote = profile.canBePromoted()
    val missingCriteria = profile.missingPromotionCriteria()

    var expandedRankId by remember { mutableStateOf<String?>(profile.rank.name) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MilitaryBlack)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. WANJAKTI STATUS COMPACT CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (canPromote) Color(0xFF132B1B) else MilitaryDarkSurface
                ),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (canPromote) MilitaryTacticalGreen else MilitaryBorder
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (nextRank != null) {
                                TacticalCanvasInsignia(rank = nextRank, size = 32.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Column {
                                Text(
                                    text = "SIDANG WANJAKTI",
                                    color = MilitaryGold,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (nextRank != null) "Promosi: ${nextRank.title}" else "Pangkat Tertinggi",
                                    color = MilitaryTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        if (nextRank != null) {
                            if (canPromote) {
                                Button(
                                    onClick = {
                                        val success = viewModel.promoteOfficer()
                                        if (success) {
                                            Toast.makeText(context, "Resmi dilantik naik pangkat!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .height(34.dp)
                                        .testTag("submit_wanjakti_promotion"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MilitaryTacticalGreen),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = MilitaryBlack, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("LANTIK", color = MilitaryBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .background(MilitaryElevatedSurface, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${missingCriteria.size} Syarat Belum Siap",
                                        color = MilitaryAlertAmber,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (nextRank != null && !canPromote) {
                        Spacer(modifier = Modifier.height(6.dp))
                        missingCriteria.forEach { crit ->
                            Row(
                                modifier = Modifier.padding(vertical = 1.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = MilitaryAlertAmber, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(crit, color = MilitaryAlertAmber, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // 2. COMPACT EDUCATION BAR (SESKOAD & LEMHANNAS)
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // SESKOAD Compact Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SESKOAD", color = MilitaryGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (profile.seskoadPassed) "LULUS ✓" else "Min. Mayor (50jt)",
                                color = if (profile.seskoadPassed) MilitaryRadarGreen else MilitaryTextSecondary,
                                fontSize = 9.sp
                            )
                        }
                        if (!profile.seskoadPassed) {
                            Button(
                                onClick = {
                                    val res = viewModel.executeOfficerDuty("PENDIDIKAN_SESKOAD")
                                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                                },
                                enabled = profile.rank.ordinal >= MilitaryRank.MAYOR.ordinal,
                                colors = ButtonDefaults.buttonColors(containerColor = MilitaryGold),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier
                                    .height(28.dp)
                                    .testTag("enroll_seskoad_button"),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                Text("IKUT", color = MilitaryBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // LEMHANNAS Compact Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("LEMHANNAS", color = MilitaryGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (profile.lemhannasPassed) "LULUS ✓" else "Min. Brigjen (100jt)",
                                color = if (profile.lemhannasPassed) MilitaryRadarGreen else MilitaryTextSecondary,
                                fontSize = 9.sp
                            )
                        }
                        if (!profile.lemhannasPassed) {
                            Button(
                                onClick = {
                                    val res = viewModel.executeOfficerDuty("PENDIDIKAN_LEMHANNAS")
                                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                                },
                                enabled = profile.rank.ordinal >= MilitaryRank.BRIGJEN.ordinal,
                                colors = ButtonDefaults.buttonColors(containerColor = MilitaryGold),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier
                                    .height(28.dp)
                                    .testTag("enroll_lemhannas_button"),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                Text("IKUT", color = MilitaryBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "PETA KEPANGKATAN & JABATAN (KETUK UNTUK DETAIL)",
                color = MilitaryGold,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // 3. COMPACT ACCORDION RANK LADDER
        items(allRanks) { r ->
            val isCurrent = r == profile.rank
            val isAchieved = r.ordinal < profile.rank.ordinal
            val isExpanded = expandedRankId == r.name
            val post = MilitaryPostsRegistry.getPostForRank(r)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedRankId = if (isExpanded) null else r.name
                    },
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isCurrent -> MilitaryElevatedSurface
                        isAchieved -> Color(0xFF111813)
                        else -> MilitaryCardSurface
                    }
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isCurrent) 1.5.dp else 1.dp,
                    color = when {
                        isCurrent -> MilitaryTacticalGreen
                        isAchieved -> MilitaryBorderHighlight
                        else -> MilitaryBorder
                    }
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TacticalCanvasInsignia(rank = r, size = 32.dp)
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${r.title} (${r.abbreviation})",
                                    color = if (isCurrent) MilitaryTacticalGreen else if (isAchieved) MilitaryTextPrimary else MilitaryTextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isCurrent) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(MilitaryTacticalGreen, RoundedCornerShape(3.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("AKTIF", color = MilitaryBlack, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else if (isAchieved) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("✓", color = MilitaryTextSecondary, fontSize = 10.sp)
                                }
                            }
                            Text(
                                text = post.name,
                                color = MilitaryGold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MilitaryTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Collapsible Details
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                                .background(MilitaryDarkSurface, RoundedCornerShape(4.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Komando: ${post.troopsUnderCommand} Personel • Syarat: ${r.minMeritPoints} Pts / ${r.minYearsOfService} Thn",
                                color = MilitaryRadarGreen,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = r.description,
                                color = MilitaryTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
