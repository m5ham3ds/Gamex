package com.example.game.temple

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.GameViewModel
import com.example.ui.theme.*

@Composable
fun OracleConversationScreen(
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    val question by viewModel.oracleQuestion.collectAsState()
    val choices by viewModel.oracleChoices.collectAsState()
    val selectedIndex by viewModel.oracleSelectedIndex.collectAsState()
    val feedback by viewModel.oracleFeedback.collectAsState()
    val isLoading by viewModel.isOracleLoading.collectAsState()

    // Used to intercept background taps without eating button clicks
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF5060A0D))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {} // Intercepts clicks falling to the background
            )
    ) {
        AshParticles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🔮", fontSize = 50.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("بوابة معبد النور", fontSize = 22.sp, color = EchoesBlue, fontWeight = FontWeight.Bold)
            Text("THE TEMPLE OF LIGHT ORACLE", fontSize = 11.sp, color = EchoesBlue.copy(alpha = 0.6f), letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // Oracle Question / Riddle Card
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(SurfaceDark, RoundedCornerShape(8.dp))
                    .border(1.dp, BlightGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = BlightGold, modifier = Modifier.align(Alignment.Center).padding(16.dp))
                } else {
                    Text(
                        text = question,
                        color = RadianceWhite,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multiple Choice Options
            if (!isLoading && choices.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    choices.forEachIndexed { idx, option ->
                        if (option.isNotBlank()) {
                            val isSelected = selectedIndex == idx
                            val choiceBorderColor = if (isSelected) BlightGold else OutlineGray.copy(alpha = 0.4f)
                            val choiceBgColor = if (isSelected) Color(0xFF232C33) else SurfaceDark.copy(alpha = 0.8f)
                            val textColor = if (isSelected) BlightGold else OnSurfaceLight

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        if (feedback == null) {
                                            viewModel.selectOracleChoice(idx)
                                        }
                                    }
                                    .background(choiceBgColor)
                                    .border(1.dp, choiceBorderColor, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${idx + 1}.  $option",
                                    color = textColor,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Text("✦", color = BlightGold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Feedback
            feedback?.let { fb ->
                val isCorrect = fb.contains("مبارك") || fb.contains("صحيحة") || fb.contains("أحسنت") || fb.contains("Correct")
                Text(
                    text = fb,
                    color = if (isCorrect) Color(0xFF4CAF50) else VitalityRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (feedback == null) {
                    Button(
                        onClick = onClose,
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VoidPrimary, contentColor = OutlineGray),
                        modifier = Modifier.border(1.dp, OutlineGray)
                    ) {
                        Text("انصراف • DEPART", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { viewModel.submitSelectedOracleAnswer() },
                        shape = RectangleShape,
                        enabled = selectedIndex != -1,
                        border = BorderStroke(1.dp, BlightGold),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = BlightGold)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("تأكيد الإجابة • ANSWER", fontSize = 12.sp)
                            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                } else {
                    Button(
                        onClick = onClose,
                        shape = RectangleShape,
                        border = BorderStroke(1.dp, BlightGold),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = BlightGold),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("موافق واستمرار • CONTINUE", fontSize = 13.sp)
                            Icon(Icons.Filled.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// Sparkly mystical ash effect running in backgrounds
@Composable
fun AshParticles() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val time = System.currentTimeMillis()
        for (i in 0..35) {
            val px = ((Math.sin(time / 1500.0 + i * 200) * 1200) % w).toFloat()
            val py = (h - ((time / 30.0 + i * 50) % h)).toFloat()
            val sx = if (px < 0) px + w else px
            val sy = if (py < 0) py + h else py

            val brightness = ((Math.sin(time / 400.0 + i) + 1.0) / 2.0).toFloat()

            drawRect(
                color = BlightGold.copy(alpha = 0.2f * brightness),
                topLeft = Offset(sx, sy),
                size = Size(3f, 3f)
            )
        }
    }
}
