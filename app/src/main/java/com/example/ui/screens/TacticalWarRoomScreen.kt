package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.model.TacticalMissionsRegistry
import com.example.ui.components.TacticalBattleGrid2D
import com.example.ui.theme.*
import com.example.viewmodel.MilitaryGameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TacticalWarRoomScreen(
    viewModel: MilitaryGameViewModel,
    profile: PlayerProfile,
    modifier: Modifier = Modifier
) {
    val activeMission by viewModel.activeMission.collectAsState()
    val units by viewModel.tacticalUnits.collectAsState()
    val turnLogs by viewModel.battleTurnLogs.collectAsState()
    val outcome by viewModel.missionOutcome.collectAsState()

    val availableMissions = remember(profile.currentYear) {
        TacticalMissionsRegistry.getAvailableMissions(profile.currentYear)
    }

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply {
            maximumFractionDigits = 0
        }
    }

    var selectedMissionBriefId by remember { mutableStateOf<String?>(availableMissions.lastOrNull()?.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MilitaryBlack)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        if (activeMission == null) {
            // WAR ROOM: DENSE COMPACT MISSION LIST
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PUSAT KOMANDO OPERASI (TOC)",
                    color = MilitaryRadarGreen,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${availableMissions.size} MISI AKTIF",
                    color = MilitaryGold,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(availableMissions) { mission ->
                    val isExpanded = selectedMissionBriefId == mission.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedMissionBriefId = if (isExpanded) null else mission.id
                            },
                        colors = CardDefaults.cardColors(containerColor = MilitaryCardSurface),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isExpanded) MilitaryTacticalGreen else MilitaryBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(mission.theater.icon, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = mission.codeName,
                                            color = MilitaryGold,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = mission.title,
                                            color = MilitaryTextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Button(
                                    onClick = { viewModel.startMission(mission) },
                                    modifier = Modifier
                                        .height(30.dp)
                                        .testTag("launch_mission_${mission.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MilitaryTacticalGreen),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MilitaryBlack, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("GELAR", color = MilitaryBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Collapsible Briefing
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
                                        text = mission.briefing,
                                        color = MilitaryTextSecondary,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Reward: +${mission.meritReward} Pts • ${currencyFormatter.format(mission.budgetReward)}",
                                            color = MilitaryRadarGreen,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Musuh: ${mission.enemyStrengthDesc}",
                                            color = MilitaryHostileRed,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // ACTIVE COMBAT VIEW: DENSE & COMPACT
            val curMission = activeMission!!

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(MilitaryRadarGreen, RoundedCornerShape(3.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = curMission.codeName,
                        color = MilitaryRadarGreen,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { viewModel.closeMission() },
                    modifier = Modifier.size(28.dp).testTag("exit_war_room_button")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup", tint = MilitaryTextSecondary, modifier = Modifier.size(18.dp))
                }
            }

            // Compact 2D Grid
            TacticalBattleGrid2D(
                theater = curMission.theater,
                units = units
            )

            Spacer(modifier = Modifier.height(6.dp))

            // OUTCOME NOTICE IF FINISHED
            if (outcome != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (outcome == "VICTORY") Color(0xFF14301B) else Color(0xFF331414)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (outcome == "VICTORY") MilitaryTacticalGreen else MilitaryHostileRed
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (outcome == "VICTORY") "KEMENANGAN MUTLAK" else "PASUKAN MUNDUR",
                                color = if (outcome == "VICTORY") MilitaryRadarGreen else MilitaryHostileRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (outcome == "VICTORY") "+${curMission.meritReward} Pts & Dana Operasional" else "Evaluasi taktik diperlukan",
                                color = MilitaryTextPrimary,
                                fontSize = 10.sp
                            )
                        }

                        Button(
                            onClick = { viewModel.closeMission() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (outcome == "VICTORY") MilitaryTacticalGreen else MilitaryHostileRed
                            ),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("dismiss_outcome_button"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text("SELESAI", color = MilitaryBlack, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            } else {
                // TACTICAL ORDER COMMAND BUTTONS: COMPACT ROW
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = { viewModel.executeTacticalAction("MANUVER_FLANK") },
                        modifier = Modifier.weight(1f).height(32.dp).testTag("action_flank"),
                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryCardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Sayap", color = MilitaryTextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.executeTacticalAction("TEMBAKAN_TABIR") },
                        modifier = Modifier.weight(1f).height(32.dp).testTag("action_artillery"),
                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryCardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Mortir", color = MilitaryTextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.executeTacticalAction("SERANGAN_MALAM") },
                        modifier = Modifier.weight(1f).height(32.dp).testTag("action_raider_infil"),
                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryCardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Senyap", color = MilitaryTextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.executeTacticalAction("BANTUAN_UDARA_CAS") },
                        modifier = Modifier.weight(1f).height(32.dp).testTag("action_air_support"),
                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryElevatedSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryRadarGreen),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Drone/CAS", color = MilitaryRadarGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.executeTacticalAction("PERTAHANAN_PARIT") },
                        modifier = Modifier.weight(1f).height(32.dp).testTag("action_trench_defend"),
                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryCardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryBorder),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Parit", color = MilitaryTextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // DENSE BATTLE LOG
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MilitaryDarkSurface, RoundedCornerShape(6.dp))
                    .border(1.dp, MilitaryBorder, RoundedCornerShape(6.dp))
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(turnLogs.reversed()) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MilitaryBlack, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "R${log.round}: ${log.actionTaken}",
                                color = MilitaryGold,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = log.narrative,
                                color = MilitaryTextPrimary,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "-${log.enemyDamageDealt} HP",
                            color = MilitaryRadarGreen,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
