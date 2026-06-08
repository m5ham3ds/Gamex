package com.example.game.menu

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.GameViewModel
import com.example.game.player.PlayerState
import com.example.game.temple.AshParticles
import com.example.ui.theme.*

@Composable
fun GameMenuScreen(
    onStart: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenChronicles: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(VoidPrimary)) {
        AshParticles()
        
        // Centered Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "قِـنَـاعُ النُّـور",
                fontSize = 38.sp,
                color = RadianceWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "MASK OF LIGHT",
                fontSize = 14.sp,
                color = BlightGold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Enter the void Option
            Button(
                onClick = onStart,
                modifier = Modifier
                    .size(260.dp, 54.dp)
                    .border(2.dp, BlightGold, RoundedCornerShape(4.dp))
                    .testTag("start_game_button"),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = BlightGold)
            ) {
                Text("دخول إريغرا • EXPLORE ERYGRA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Settings Option
            OutlinedButton(
                onClick = onOpenSettings,
                modifier = Modifier
                    .size(260.dp, 50.dp)
                    .border(1.dp, OutlineGray, RoundedCornerShape(4.dp))
                    .testTag("settings_button"),
                shape = RectangleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceLight)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("الإعدادات • SETTINGS", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chronicles Option
            OutlinedButton(
                onClick = onOpenChronicles,
                modifier = Modifier
                    .size(260.dp, 50.dp)
                    .border(1.dp, OutlineGray, RoundedCornerShape(4.dp)),
                shape = RectangleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceLight)
            ) {
                Text("سجلات الذاكرة • CHRONICLES", fontSize = 11.sp, color = OutlineGray)
            }
        }
    }
}

@Composable
fun GameSettingsScreen(
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    var bgVolume by remember { mutableStateOf(viewModel.soundVolume.value) }
    var hudScale by remember { mutableStateOf(viewModel.hudScale.value) }
    var buttonScale by remember { mutableStateOf(viewModel.controlButtonScale.value) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE006090D))
            .clickable(enabled = false) {}
    ) {
        AshParticles()

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.85f)
                .border(2.dp, BlightGold, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "مهندس عناصر التحكم والإعدادات",
                        fontSize = 20.sp,
                        color = BlightGold,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, tint = RadianceWhite, contentDescription = "Close Settings")
                    }
                }

                Divider(color = OutlineGray.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Left Column: Control Panel Adjustments
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("تخصيص الواجهة وتحجيم العناصر", color = RadianceWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        // SIZES: Blood (Vitality) and Soul Bar Scale
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("مقياس شريط الدم والروح (HUD Scale)", color = OnSurfaceLight, fontSize = 11.sp)
                                Text("${(hudScale * 100).toInt()}%", color = BlightGold, fontSize = 11.sp)
                            }
                            Slider(
                                value = hudScale,
                                onValueChange = {
                                    hudScale = it
                                    viewModel.setHudScale(it)
                                },
                                valueRange = 0.6f..1.6f,
                                colors = SliderDefaults.colors(
                                    thumbColor = BlightGold,
                                    activeTrackColor = BlightGold,
                                    inactiveTrackColor = OutlineGray
                                )
                            )
                        }

                        // SIZES: Button Layout Sizes
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("حجم أزرار التحكم باللمس (Buttons Scale)", color = OnSurfaceLight, fontSize = 11.sp)
                                Text("${(buttonScale * 100).toInt()}%", color = EchoesBlue, fontSize = 11.sp)
                            }
                            Slider(
                                value = buttonScale,
                                onValueChange = {
                                    buttonScale = it
                                    viewModel.setControlButtonScale(it)
                                },
                                valueRange = 0.6f..1.5f,
                                colors = SliderDefaults.colors(
                                    thumbColor = EchoesBlue,
                                    activeTrackColor = EchoesBlue,
                                    inactiveTrackColor = OutlineGray
                                )
                            )
                        }

                        // AUDIO
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Settings, tint = OnSurfaceLight, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Text("صوت الموسيقى والصدى (Volume)", color = OnSurfaceLight, fontSize = 11.sp)
                                }
                                Text("${(bgVolume * 100).toInt()}%", color = BlightGold, fontSize = 11.sp)
                            }
                            Slider(
                                value = bgVolume,
                                onValueChange = {
                                    bgVolume = it
                                    viewModel.setSoundVolume(it)
                                },
                                valueRange = 0f..1f,
                                colors = SliderDefaults.colors(
                                    thumbColor = BlightGold,
                                    activeTrackColor = BlightGold,
                                    inactiveTrackColor = OutlineGray
                                )
                            )
                        }
                    }

                    // Right Column: Information Summary (Display layout orientation status)
                    Column(
                        modifier = Modifier
                            .weight(0.8f)
                            .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                            .border(1.dp, OutlineGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("مواصفات العرض والجهاز", color = BlightGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        
                        Text("• اتجاه الشاشة المقفل: أفقي (Landscape)", color = RadianceWhite, fontSize = 11.sp)
                        Text("• دقة الحساب للمستويات: 1600 × 800", color = OnSurfaceLight, fontSize = 10.sp)
                        Text("• وضع الأزرار: سفلي جانبي (ثابت)", color = OnSurfaceLight, fontSize = 10.sp)

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {
                                viewModel.resetSettingsToDefault()
                                bgVolume = 0.8f
                                hudScale = 1.0f
                                buttonScale = 1.0f
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainer),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("استعادة الافتراضي", fontSize = 11.sp, color = RadianceWhite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GamePauseScreen(onResume: () -> Unit, onMainMenu: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xD00A0F14))) {
        AshParticles()
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("توقف مؤقت • GAME PAUSED", fontSize = 28.sp, color = RadianceWhite, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onResume,
                shape = RectangleShape,
                modifier = Modifier
                    .border(1.dp, BlightGold)
                    .width(220.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = BlightGold)
            ) {
                Text("استمرار اللعب • RESUME", fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onMainMenu,
                shape = RectangleShape,
                modifier = Modifier
                    .border(1.dp, OutlineGray)
                    .width(220.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoidPrimary, contentColor = RadianceWhite)
            ) {
                Text("القائمة الرئيسية • MAIN MENU", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(VoidPrimary)) {
        AshParticles()
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("تَـهَـشَّـمَـتْ وِعَـاءُ الـرُّوحِ...", fontSize = 32.sp, color = VitalityRed, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("THE VESSEL HAS SHATTERED", fontSize = 14.sp, color = VitalityRed.copy(alpha = 0.7f), letterSpacing = 2.sp, modifier = Modifier.padding(top = 4.dp))
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("الأصداء المتبقية: $score", fontSize = 16.sp, color = RadianceWhite)
            
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onRestart,
                shape = RectangleShape,
                modifier = Modifier.border(1.dp, OutlineGray),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = RadianceWhite)
            ) {
                Text("النهوض المجدد • AWAKEN ANEW", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun VictoryScreen(score: Int, onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(VoidPrimary)) {
        AshParticles()
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("تَمَّ تَطْهِيرُ النَّوَاةِ السَّحِيقَةِ", fontSize = 32.sp, color = BlightGold, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("THE CORE HAS BEEN CLEANSED", fontSize = 13.sp, color = BlightGold.copy(alpha = 0.7f), letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(16.dp))
            Text("لقد عبرت سراديب النسيان المظلمة بنجاح.", fontSize = 15.sp, color = OnSurfaceLight, textAlign = TextAlign.Center)
            Text("الأصداء النهائية المكتسبة: $score", fontSize = 16.sp, color = RadianceWhite, modifier = Modifier.padding(top = 8.dp))

            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onRestart,
                shape = RectangleShape,
                modifier = Modifier.border(1.dp, BlightGold),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = BlightGold)
            ) {
                Text("بداية ملحمة جديدة • BEGIN ANEW", fontSize = 13.sp)
            }
        }
    }
}

// Moved to com.example.game.temple.OracleConversationScreen
