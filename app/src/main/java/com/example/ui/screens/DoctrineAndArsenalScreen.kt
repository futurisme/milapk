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
import com.example.ui.theme.*
import com.example.viewmodel.MilitaryGameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DoctrineAndArsenalScreen(
    viewModel: MilitaryGameViewModel,
    profile: PlayerProfile,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val arsenal by viewModel.arsenal.collectAsState()
    val doctrines by viewModel.doctrines.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Alutsista, 1 = Doktrin
    var expandedItemId by remember { mutableStateOf<String?>(null) }

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply {
            maximumFractionDigits = 0
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MilitaryBlack)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        // SEGMENTED TAB BUTTONS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
                .background(MilitaryCardSurface, RoundedCornerShape(6.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { selectedTab = 0 },
                modifier = Modifier.weight(1f).height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 0) MilitaryTacticalGreen else Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "ALUTSISTA (${arsenal.count { it.isAcquired }}/${arsenal.size})",
                    color = if (selectedTab == 0) MilitaryBlack else MilitaryTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { selectedTab = 1 },
                modifier = Modifier.weight(1f).height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 1) MilitaryTacticalGreen else Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "DOKTRIN (${doctrines.count { it.isAdopted }}/${doctrines.size})",
                    color = if (selectedTab == 1) MilitaryBlack else MilitaryTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (selectedTab == 0) {
                items(arsenal) { item ->
                    val isExpanded = expandedItemId == item.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedItemId = if (isExpanded) null else item.id },
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isAcquired) Color(0xFF132219) else MilitaryCardSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (item.isAcquired) MilitaryTacticalGreen else MilitaryBorder
                        )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        color = if (item.isAcquired) MilitaryTacticalGreen else MilitaryTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "+${item.tacticalBonus} Taktik • Thn ${item.yearIntroduced}",
                                            color = MilitaryGold,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                if (item.isAcquired) {
                                    Box(
                                        modifier = Modifier
                                            .background(MilitaryElevatedSurface, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("TERPASANG ✓", color = MilitaryTacticalGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            val ok = viewModel.acquireArsenal(item)
                                            if (ok) {
                                                Toast.makeText(context, "${item.name} siap!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Dana operasional kurang!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .height(28.dp)
                                            .testTag("buy_arsenal_${item.id}"),
                                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryTacticalGreen),
                                        shape = RoundedCornerShape(4.dp),
                                        enabled = profile.operationalFunds >= item.cost,
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                    ) {
                                        Text(
                                            currencyFormatter.format(item.cost),
                                            color = MilitaryBlack,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Text(
                                    text = item.description,
                                    color = MilitaryTextSecondary,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                items(doctrines) { doc ->
                    val isExpanded = expandedItemId == doc.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedItemId = if (isExpanded) null else doc.id },
                        colors = CardDefaults.cardColors(
                            containerColor = if (doc.isAdopted) Color(0xFF132A1C) else MilitaryCardSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (doc.isAdopted) MilitaryTacticalGreen else MilitaryBorder
                        )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = doc.name,
                                        color = if (doc.isAdopted) MilitaryTacticalGreen else MilitaryTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "+${doc.tacticalBonus} Taktik • +${doc.leadershipBonus} Pimpinan",
                                        color = MilitaryGold,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                if (doc.isAdopted) {
                                    Box(
                                        modifier = Modifier
                                            .background(MilitaryTacticalGreen, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("AKTIF ✓", color = MilitaryBlack, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            val ok = viewModel.adoptDoctrine(doc)
                                            if (ok) {
                                                Toast.makeText(context, "Doktrin disahkan!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Butuh ${doc.meritRequired} Merit Pts!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .height(28.dp)
                                            .testTag("adopt_doc_${doc.id}"),
                                        colors = ButtonDefaults.buttonColors(containerColor = MilitaryGold),
                                        shape = RoundedCornerShape(4.dp),
                                        enabled = profile.meritPoints >= doc.meritRequired,
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                    ) {
                                        Text("${doc.meritRequired} PTS", color = MilitaryBlack, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Text(
                                    text = doc.description,
                                    color = MilitaryTextSecondary,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
