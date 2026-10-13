package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.ui.components.EventOutcomeDialog
import com.example.ui.components.GeopoliticalCrisisDialog
import com.example.ui.components.TacticalCanvasInsignia
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.MilitaryGameViewModel

enum class MilitaryNavDestination(val label: String, val icon: ImageVector, val tag: String) {
    KOMANDO("Komando", Icons.Default.Dashboard, "tab_komando"),
    KARIER("Karier", Icons.Default.MilitaryTech, "tab_karier"),
    WARGAME("Operasi", Icons.Default.Shield, "tab_wargame"),
    ALUTSISTA("Alutsista", Icons.Default.PrecisionManufacturing, "tab_alutsista"),
    DOSSIER("Dossier", Icons.Default.Badge, "tab_dossier")
}

@Composable
fun MilitaryMainScreen(
    viewModel: MilitaryGameViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profile.collectAsState()
    val activeEvent by viewModel.activeGeopoliticalEvent.collectAsState()
    val eventOutcome by viewModel.eventOutcomeMessage.collectAsState()
    val promotionNotice by viewModel.promotionNotice.collectAsState()

    var currentTab by remember { mutableStateOf(MilitaryNavDestination.KOMANDO) }

    // Back handling: if not on KOMANDO, go back to KOMANDO
    BackHandler(enabled = currentTab != MilitaryNavDestination.KOMANDO) {
        currentTab = MilitaryNavDestination.KOMANDO
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MilitaryBlack,
        topBar = {
            // SLIM TACTICAL STATUS BAR (Ultra-compact HUD header for Android 15+)
            Surface(
                color = MilitaryDarkSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .border(width = 0.5.dp, color = MilitaryBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TacticalCanvasInsignia(rank = profile.rank, size = 22.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "KARIER JENDERAL",
                            color = MilitaryGold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = " // ${profile.rank.abbreviation} ${profile.name}",
                            color = MilitaryTextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    // Compact Merit Badge
                    Box(
                        modifier = Modifier
                            .background(MilitaryCardSurface, RoundedCornerShape(4.dp))
                            .border(0.5.dp, MilitaryBorder, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${profile.meritPoints} PTS",
                            color = MilitaryGold,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MilitaryDarkSurface,
                contentColor = MilitaryTextPrimary,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .height(64.dp)
                    .testTag("military_bottom_nav")
            ) {
                MilitaryNavDestination.values().forEach { destination ->
                    val isSelected = currentTab == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = destination },
                        modifier = Modifier.testTag(destination.tag),
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                tint = if (isSelected) MilitaryBlack else MilitaryTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MilitaryTacticalGreen else MilitaryTextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MilitaryTacticalGreen
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MilitaryNavDestination.KOMANDO -> DashboardScreen(
                    viewModel = viewModel,
                    profile = profile,
                    onNavigateToWargame = { currentTab = MilitaryNavDestination.WARGAME }
                )
                MilitaryNavDestination.KARIER -> CareerProgressionScreen(
                    viewModel = viewModel,
                    profile = profile
                )
                MilitaryNavDestination.WARGAME -> TacticalWarRoomScreen(
                    viewModel = viewModel,
                    profile = profile
                )
                MilitaryNavDestination.ALUTSISTA -> DoctrineAndArsenalScreen(
                    viewModel = viewModel,
                    profile = profile
                )
                MilitaryNavDestination.DOSSIER -> OfficerDossierScreen(
                    viewModel = viewModel,
                    profile = profile
                )
            }

            // PROMOTION BANNER FLOATER IF ELIGIBLE
            if (promotionNotice != null && currentTab != MilitaryNavDestination.KARIER) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.TopCenter),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF14301B)),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MilitaryTacticalGreen)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = MilitaryGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = promotionNotice!!,
                                color = MilitaryTextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.dismissPromotionNotice()
                                currentTab = MilitaryNavDestination.KARIER
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MilitaryTacticalGreen),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Text("LIHAT", color = MilitaryBlack, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // GEOPOLITICAL HISTORICAL EVENT DIALOG
        if (activeEvent != null) {
            GeopoliticalCrisisDialog(
                event = activeEvent!!,
                onSelectOption = { opt -> viewModel.selectEventOption(opt) }
            )
        }

        // EVENT OUTCOME NARRATIVE DIALOG
        if (eventOutcome != null) {
            EventOutcomeDialog(
                narrative = eventOutcome!!,
                onDismiss = { viewModel.dismissEventOutcome() }
            )
        }
    }
}
