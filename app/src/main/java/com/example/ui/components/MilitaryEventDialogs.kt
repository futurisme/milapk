package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.EventOption
import com.example.model.GeopoliticalEvent
import com.example.ui.theme.*

@Composable
fun GeopoliticalCrisisDialog(
    event: GeopoliticalEvent,
    onSelectOption: (EventOption) -> Unit
) {
    Dialog(onDismissRequest = { /* Must choose an option */ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MilitaryDarkSurface),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MilitaryGold)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MilitaryAlertAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "KAWAT TELEGRAM RAHASIA // ${event.sender.uppercase()}",
                            color = MilitaryAlertAmber,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tahun ${event.year} - Bulan ${event.month}",
                            color = MilitaryTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = event.headline,
                    color = MilitaryGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = event.backgroundInfo,
                    color = MilitaryTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "KEPUTUSAN STRATEGIS KOMANDAN:",
                    color = MilitaryRadarGreen,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                event.options.forEachIndexed { index, opt ->
                    Button(
                        onClick = { onSelectOption(opt) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("event_option_$index"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (index == 0) MilitaryElevatedSurface else MilitaryCardSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (index == 0) MilitaryTacticalGreen else MilitaryBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = opt.title,
                                color = if (index == 0) MilitaryTacticalGreen else MilitaryTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = opt.description,
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

@Composable
fun EventOutcomeDialog(
    narrative: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "LAPORAN KASUS & HASIL KEPUTUSAN",
                color = MilitaryRadarGreen,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = narrative,
                color = MilitaryTextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MilitaryTacticalGreen),
                modifier = Modifier.testTag("dismiss_event_dialog")
            ) {
                Text("DIPAHAMI, LAKSANAKAN!", color = MilitaryBlack, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = MilitaryDarkSurface
    )
}
