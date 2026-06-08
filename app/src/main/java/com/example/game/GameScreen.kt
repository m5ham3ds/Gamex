package com.example.game

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.enemy.EnemyType
import com.example.game.hud.GameHudOverlay
import com.example.game.menu.*
import com.example.game.player.Direction
import com.example.ui.theme.*

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.gameState.collectAsState()
    val player by viewModel.player.collectAsState()
    val currentRegion by viewModel.currentRegion.collectAsState()
    val enemies by viewModel.enemies.collectAsState()
    val projectiles by viewModel.projectiles.collectAsState()
    val particles by viewModel.particles.collectAsState()
    val relic by viewModel.relic.collectAsState()
    val isSlashing by viewModel.isSlashing.collectAsState()

    var showSettingsInGame by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            GameState.MENU -> {
                GameMenuScreen(
                    onStart = { viewModel.startNewGame() },
                    onOpenSettings = { showSettingsInGame = true },
                    onOpenChronicles = { viewModel.openChronicles() }
                )
            }
            GameState.CHRONICLES -> {
                ChroniclesScreen(onClose = { viewModel.closeChronicles() })
            }
            else -> {
                // Main playing interface (Canvas background & overlays)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(currentRegion.bgHex))
                ) {
                    // Game Canvas (The actual 2D game render)
                    GameViewCanvas(
                        player = player,
                        region = currentRegion,
                        enemies = enemies,
                        projectiles = projectiles,
                        particles = particles,
                        relic = relic,
                        isSlashing = isSlashing,
                        modifier = Modifier.fillMaxSize()
                    )

                    // HUD controls overlay (D-pad & stats)
                    GameHudOverlay(
                        viewModel = viewModel,
                        player = player,
                        onPauseClick = { viewModel.pauseGame() },
                        onRecallMemoriesClick = { viewModel.upgradeVitality() }
                    )

                    // Popups modals
                    when (state) {
                        GameState.PAUSED -> {
                            GamePauseScreen(
                                onResume = { viewModel.resumeGame() },
                                onMainMenu = { viewModel.backToMainMenu() }
                            )
                        }
                        GameState.ORACLE_CONVERSATION -> {
                            OracleConversationScreen(
                                viewModel = viewModel,
                                onClose = { viewModel.resumeGame() }
                            )
                        }
                        GameState.GAME_OVER -> {
                            GameOverScreen(
                                score = player.score,
                                onRestart = { viewModel.startNewGame() }
                            )
                        }
                        GameState.VICTORY -> {
                            VictoryScreen(
                                score = player.score,
                                onRestart = { viewModel.startNewGame() }
                            )
                        }
                        else -> {
                            // Run playing state normally
                        }
                    }

                    // Floating interact key indicator (if near the Oracle core portal)
                    relic?.let { r ->
                        val dx = player.x - r.x
                        val dy = player.y - r.y
                        if (dx*dx + dy*dy < 110f * 110f && state == GameState.PLAYING) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 90.dp)
                                    .background(SurfaceDark.copy(alpha = 0.85f), RoundedCornerShape(24.dp))
                                    .border(1.dp, BlightGold, RoundedCornerShape(24.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    "اضغط على زر العين [Interact] للحديث مع العراف",
                                    color = BlightGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
        
        if (showSettingsInGame) {
            GameSettingsScreen(
                viewModel = viewModel,
                onClose = { showSettingsInGame = false }
            )
        }
    }
}

@Composable
fun GameViewCanvas(
    player: com.example.game.player.PlayerState,
    region: com.example.game.world.GameRegion,
    enemies: List<com.example.game.enemy.Enemy>,
    projectiles: List<com.example.game.Projectile>,
    particles: List<com.example.game.Particle>,
    relic: com.example.game.OracleRelic?,
    isSlashing: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val scaleX = size.width / 1600f
        val scaleY = size.height / 800f

        clipRect {
            // Draw background ambient grid lines
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(region.bgHex), Color(0xFF020406))
                )
            )

            // --- DRAW RELIC / TEMPLE PORTAL PILLAR ---
            relic?.let { r ->
                val rx = r.x * scaleX
                val ry = r.y * scaleY
                val radius = r.radius * scaleX

                // Base pillar stone
                drawRect(
                    color = Color(0xFF3A424A),
                    topLeft = Offset(rx - 12 * scaleX, ry - 60 * scaleY),
                    size = Size(24 * scaleX, 100 * scaleY)
                )

                // Glowing mystical orb inside relic
                drawCircle(
                    color = if (r.isUsed) EchoesBlue.copy(alpha = 0.4f) else BlightGold,
                    radius = radius,
                    center = Offset(rx, ry - 70 * scaleY),
                    style = Stroke(width = 3.dp.toPx())
                )
                drawCircle(
                    color = if (r.isUsed) EchoesBlue else BlightGold,
                    radius = radius * 0.4f,
                    center = Offset(rx, ry - 70 * scaleY)
                )
            }

            // --- DRAW PLATFORMS ---
            region.platforms.forEach { platform ->
                val px = platform.x * scaleX
                val py = platform.y * scaleY
                val pWidth = platform.width * scaleX
                val pHeight = platform.height * scaleY

                val platColor = if (platform.isBouncy) EchoesBlue else Color(0xFF2C323A)
                val strokeColor = if (platform.isBouncy) Color.White else OutlineGray

                // Base platform slab
                drawRoundRect(
                    color = platColor,
                    topLeft = Offset(px, py),
                    size = Size(pWidth, pHeight),
                    cornerRadius = CornerRadius(4 * scaleX, 4 * scaleY)
                )
                // Highlight stone edge
                drawRoundRect(
                    color = strokeColor,
                    topLeft = Offset(px, py),
                    size = Size(pWidth, pHeight),
                    cornerRadius = CornerRadius(4 * scaleX, 4 * scaleY),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }

            // --- DRAW HAZARDS (Crimson spikes or magma) ---
            region.hazards.forEach { hazard ->
                val hx = hazard.x * scaleX
                val hy = hazard.y * scaleY
                val hWidth = hazard.width * scaleX
                val hHeight = hazard.height * scaleY

                // Draw spike spikes visually
                val spikesCount = (hazard.width / 15f).toInt().coerceAtLeast(3)
                val spikeWidth = hWidth / spikesCount

                for (i in 0 until spikesCount) {
                    val sx = hx + i * spikeWidth
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(sx, hy + hHeight)
                        lineTo(sx + spikeWidth / 2f, hy)
                        lineTo(sx + spikeWidth, hy + hHeight)
                        close()
                    }
                    drawPath(path, color = VitalityRed)
                }
            }

            // --- DRAW ENEMIES ---
            enemies.forEach { enemy ->
                val ex = enemy.x * scaleX
                val ey = enemy.y * scaleY
                val r = enemy.radius * scaleX

                // Draw specific models based on enemy types
                when (enemy.type) {
                    EnemyType.SCRAB_SCAVENGER, EnemyType.ASHWARDEN, EnemyType.ROPE_CROAKER, EnemyType.GEARFOLK -> {
                        // Void shadow blob with red eyes
                        drawCircle(color = SurfaceDark, radius = r, center = Offset(ex, ey))
                        drawCircle(color = VitalityRed, radius = r * 0.3f, center = Offset(ex + (if (enemy.direction == Direction.LEFT) -5f else 5f) * scaleX, ey - 3f * scaleY))
                    }
                    EnemyType.PAGE_SCRAPER, EnemyType.SHARDLING, EnemyType.ROOTCRAWLER -> {
                        // Cyan spider shape with crawling legs
                        drawCircle(color = Color(0xFF006064), radius = r, center = Offset(ex, ey))
                        drawCircle(color = EchoesBlue, radius = r * 0.3f, center = Offset(ex, ey))
                        // Spider legs
                        drawLine(EchoesBlue, Offset(ex - r, ey), Offset(ex - r - 10f * scaleX, ey + 10f * scaleY), strokeWidth = 2.dp.toPx())
                        drawLine(EchoesBlue, Offset(ex + r, ey), Offset(ex + r + 10f * scaleX, ey + 10f * scaleY), strokeWidth = 2.dp.toPx())
                    }
                    EnemyType.ECHO_SHADE, EnemyType.GLOW_WISP -> {
                        // Pulsating void sphere
                        val time = System.currentTimeMillis()
                        val pulse = (Math.sin(time / 150.0) + 1.0) / 2.0
                        val pr = r * (0.8f + pulse * 0.25f).toFloat()

                        drawCircle(color = Color(0xFF4A148C), radius = pr, center = Offset(ex, ey), style = Stroke(width = 2.dp.toPx()))
                        drawCircle(color = Color(0xFF9C27B0), radius = pr * 0.5f, center = Offset(ex, ey))
                    }
                    EnemyType.DRIFT_KNIGHT -> {
                        // Hovering sentinel core with geometric diamonds
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(ex, ey - r)
                            lineTo(ex + r, ey)
                            lineTo(ex, ey + r)
                            lineTo(ex - r, ey)
                            close()
                        }
                        drawPath(path, color = BlightGold)
                        drawCircle(color = RadianceWhite, radius = r * 0.25f, center = Offset(ex, ey))
                    }
                }

                // Enemy Health bar
                val barW = enemy.radius * 2 * scaleX
                val barH = 4 * scaleY
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(ex - enemy.radius * scaleX, ey - enemy.radius * scaleX - 12 * scaleY),
                    size = Size(barW, barH)
                )
                drawRect(
                    color = VitalityRed,
                    topLeft = Offset(ex - enemy.radius * scaleX, ey - enemy.radius * scaleX - 12 * scaleY),
                    size = Size(barW * (enemy.hp / enemy.maxHp), barH)
                )
            }

            // --- DRAW PROJECTILES ---
            projectiles.forEach { proj ->
                val px = proj.x * scaleX
                val py = proj.y * scaleY
                val pr = proj.radius * scaleX

                val color = if (proj.isPlayerOwned) EchoesBlue else VitalityRed
                drawCircle(color = color, radius = pr, center = Offset(px, py))
            }

            // --- DRAW PARTICLES ---
            particles.forEach { part ->
                val px = part.x * scaleX
                val py = part.y * scaleY
                val size = part.size * scaleX

                drawRect(
                    color = part.color.copy(alpha = part.alpha),
                    topLeft = Offset(px, py),
                    size = Size(size, size)
                )
            }

            // --- DRAW PLAYER VESSEL (The Silent Wanderer) ---
            val px = player.x * scaleX
            val py = player.y * scaleY
            val pr = player.radius * scaleX

            // Black Coat / Body
            drawRoundRect(
                color = Color(0xFF141A22), // SurfaceDark
                topLeft = Offset(px - pr, py - pr),
                size = Size(pr * 2, pr * 2.5f),
                cornerRadius = CornerRadius(4 * scaleX, 4 * scaleY)
            )
            // Coat white trims
            drawLine(
                color = RadianceWhite,
                start = Offset(px - pr, py - pr),
                end = Offset(px - pr, py + pr * 1.5f),
                strokeWidth = 2.dp.toPx()
            )

            // Satchels (swinging based on velocity)
            val swayX = -(player.vx * 0.5f).coerceIn(-5f, 5f) * scaleX
            drawRoundRect(
                color = Color(0xFF4A3A2C),
                topLeft = Offset(px + pr * 0.4f + swayX, py + pr * 0.5f),
                size = Size(8 * scaleX, 10 * scaleY),
                cornerRadius = CornerRadius(2 * scaleX, 2 * scaleY)
            )

            // Porcelain White Mask
            drawCircle(
                color = Color(0xFFF6F6F6),
                radius = pr * 0.7f,
                center = Offset(px, py - pr * 0.5f)
            )

            // Expressive bright white eyes looking in the facing direction (slits)
            val eyeLookOffset = if (player.direction == Direction.LEFT) -3f * scaleX else 3f * scaleX
            val eyeX1 = px + eyeLookOffset - 4f * scaleX
            val eyeX2 = px + eyeLookOffset + 4f * scaleX
            val eyeY = py - pr * 0.5f - 1f * scaleY

            drawLine(color = Color.Black, start = Offset(eyeX1 - 2*scaleX, eyeY), end = Offset(eyeX1 + 2*scaleX, eyeY), strokeWidth = 2.dp.toPx())
            drawLine(color = Color.Black, start = Offset(eyeX2 - 2*scaleX, eyeY), end = Offset(eyeX2 + 2*scaleX, eyeY), strokeWidth = 2.dp.toPx())
            drawCircle(color = RadianceWhite, radius = 1.5f * scaleX, center = Offset(eyeX1, eyeY))
            drawCircle(color = RadianceWhite, radius = 1.5f * scaleX, center = Offset(eyeX2, eyeY))

            // Black Wide Hat
            val pathHat = androidx.compose.ui.graphics.Path().apply {
                moveTo(px - pr * 1.5f, py - pr * 1.1f)
                lineTo(px + pr * 1.5f, py - pr * 1.1f)
                lineTo(px + pr * 0.8f, py - pr * 1.6f)
                lineTo(px - pr * 0.8f, py - pr * 1.6f)
                close()
            }
            drawPath(pathHat, color = VoidPrimary)

            // --- MEMORY SHIELD AURA ---
            if (player.soulShieldActive) {
                drawCircle(
                    color = RadianceWhite.copy(alpha = 0.6f),
                    radius = pr * 1.8f,
                    center = Offset(px, py),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // --- SWORD SLASH ARC ANIMATION ---
            if (isSlashing) {
                val slashDir = if (player.direction == Direction.LEFT) -1f else 1f
                val slashPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(px, py - 20 * scaleY)
                    quadraticBezierTo(
                        px + 50 * slashDir * scaleX, py,
                        px, py + 20 * scaleY
                    )
                }
                drawPath(
                    path = slashPath,
                    color = RadianceWhite,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }
    }
}

@Composable
fun ChroniclesScreen(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidPrimary)
    ) {
        AshParticles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("سِـجِـلاَّتُ الـرُّوحِ وَالـوَاقِـعِ", fontSize = 26.sp, color = BlightGold, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("THE CHRONICLES OF REALITY", fontSize = 11.sp, color = BlightGold.copy(alpha = 0.6f), letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.6f)
                    .background(SurfaceDark, RoundedCornerShape(12.dp))
                    .border(1.dp, OutlineGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("• إيدّا (مُصلحة الورق): نجت في طفولتها من حريق الملجأ. تُصلح اليوم الوثائق القديمة وتؤمن بقيمة الذكريات.", color = OnSurfaceLight, fontSize = 13.sp)
                    Text("• جيديون (الميكانيكي): حليف وملاّح قنوات التروس. يبيع خبرته لمن يملك الجرأة لإصلاح ما أفسده الخراب.", color = OnSurfaceLight, fontSize = 13.sp)
                    Text("• مايرا (خياطة الظل): تعالج جروح الأرواح في مستنقعات الجذور. رابط قديم يجمعها بالبطل قبل النسيان.", color = OnSurfaceLight, fontSize = 13.sp)
                    Text("• أركوفال (الوصي الأرشيفي): يؤمن بوجوب تقديم التضحيات لحفظ النظام والبيانات. الخصم الأيديولوجي الأكبر.", color = OnSurfaceLight, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClose,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                modifier = Modifier
                    .border(1.dp, OutlineGray)
                    .width(180.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    Text("العودة للقائمة الرئيسية", fontSize = 12.sp)
                }
            }
        }
    }
}
