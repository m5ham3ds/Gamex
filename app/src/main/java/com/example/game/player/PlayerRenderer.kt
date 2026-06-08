package com.example.game.player

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

fun DrawScope.drawPlayer(
    player: PlayerState,
    scaleX: Float,
    scaleY: Float,
    isSlashing: Boolean
) {
    val px = player.x * scaleX
    val py = player.y * scaleY
    val pr = player.radius * scaleX

    val faceDir = if (player.direction == Direction.LEFT) -1f else 1f
    val swayX = -(player.vx * 0.5f).coerceIn(-5f, 5f) * scaleX

    // 0. Legs & Feet
    // Back leg
    drawLine(color = Color(0xFF0F141A), start = Offset(px - pr * 0.4f, py + pr * 1.5f), end = Offset(px - pr * 0.4f + swayX*0.3f, py + pr * 2.2f), strokeWidth = 3.dp.toPx())
    drawLine(color = Color(0xFF05080A), start = Offset(px - pr * 0.4f + swayX*0.3f, py + pr * 2.2f), end = Offset(px - pr * 0.4f + swayX*0.3f + faceDir * 4f * scaleX, py + pr * 2.2f), strokeWidth = 3.dp.toPx())
    // Front leg
    drawLine(color = Color(0xFF141A22), start = Offset(px + pr * 0.4f, py + pr * 1.5f), end = Offset(px + pr * 0.4f - swayX*0.3f, py + pr * 2.2f), strokeWidth = 3.dp.toPx())
    drawLine(color = Color(0xFF0A0F14), start = Offset(px + pr * 0.4f - swayX*0.3f, py + pr * 2.2f), end = Offset(px + pr * 0.4f - swayX*0.3f + faceDir * 4f * scaleX, py + pr * 2.2f), strokeWidth = 3.dp.toPx())

    // 1. Black Coat / Body
    drawRoundRect(
        color = Color(0xFF141A22), // SurfaceDark
        topLeft = Offset(px - pr, py - pr),
        size = Size(pr * 2, pr * 2.5f),
        cornerRadius = CornerRadius(4 * scaleX, 4 * scaleY)
    )

    // 2. Coat white trims
    drawLine(
        color = RadianceWhite,
        start = Offset(px - pr, py - pr),
        end = Offset(px - pr, py + pr * 1.5f),
        strokeWidth = 2.dp.toPx()
    )

    // 3. Satchels (swinging based on velocity)
    drawRoundRect(
        color = Color(0xFF4A3A2C),
        topLeft = Offset(px + pr * 0.4f + swayX, py + pr * 0.5f),
        size = Size(8 * scaleX, 10 * scaleY),
        cornerRadius = CornerRadius(2 * scaleX, 2 * scaleY)
    )

    // 3.5 Back Arm & Pistol
    val armPivotY = py + pr * 0.2f
    val backHandX = px - faceDir * pr * 0.6f
    val backHandY = armPivotY + pr * 0.6f
    // Back arm
    drawLine(color = Color(0xFF0F141A), start = Offset(px, armPivotY), end = Offset(backHandX, backHandY), strokeWidth = 3.dp.toPx())
    // Pistol body
    drawRoundRect(color = Color(0xFF7A4E3E), topLeft = Offset(backHandX - 3f * scaleX, backHandY - 2f * scaleY), size = Size(8f * scaleX, 5f * scaleY), cornerRadius = CornerRadius(1f, 1f))
    // Pistol barrel
    drawLine(color = Color(0xFFCFC7B8), start = Offset(backHandX, backHandY), end = Offset(backHandX + 12f * scaleX * faceDir, backHandY - 1f * scaleY), strokeWidth = 1.5f.dp.toPx())
    // Glove back
    drawCircle(color = Color(0xFF1A1A1A), radius = 2.5f * scaleX, center = Offset(backHandX, backHandY))

    // 4. Porcelain White Mask
    drawCircle(
        color = Color(0xFFF6F6F6),
        radius = pr * 0.7f,
        center = Offset(px, py - pr * 0.5f)
    )

    // 5. Expressive bright white eyes looking in the facing direction (slits)
    val eyeLookOffset = if (player.direction == Direction.LEFT) -3f * scaleX else 3f * scaleX
    val eyeX1 = px + eyeLookOffset - 4f * scaleX
    val eyeX2 = px + eyeLookOffset + 4f * scaleX
    val eyeY = py - pr * 0.5f - 1f * scaleY

    drawLine(color = Color.Black, start = Offset(eyeX1 - 2*scaleX, eyeY), end = Offset(eyeX1 + 2*scaleX, eyeY), strokeWidth = 2.dp.toPx())
    drawLine(color = Color.Black, start = Offset(eyeX2 - 2*scaleX, eyeY), end = Offset(eyeX2 + 2*scaleX, eyeY), strokeWidth = 2.dp.toPx())
    drawCircle(color = RadianceWhite, radius = 1.5f * scaleX, center = Offset(eyeX1, eyeY))
    drawCircle(color = RadianceWhite, radius = 1.5f * scaleX, center = Offset(eyeX2, eyeY))

    // 6. Black Wide Hat
    val pathHat = Path().apply {
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

    // 7. Front Arm & Sword
    val frontHandX = px + faceDir * pr * 1.3f
    val frontHandY = armPivotY + pr * 0.5f
    // Front arm
    drawLine(
        color = Color(0xFF141A22),
        start = Offset(px + faceDir * pr * 0.5f, armPivotY),
        end = Offset(frontHandX, frontHandY),
        strokeWidth = 3.dp.toPx()
    )
    // Sword handle
    drawLine(color = Color(0xFF8D6E63), start = Offset(frontHandX, frontHandY), end = Offset(frontHandX + faceDir * pr * 0.5f, frontHandY - pr * 0.5f), strokeWidth = 2.dp.toPx())
    // Sword blade
    drawLine(color = RadianceWhite, start = Offset(frontHandX + faceDir * pr * 0.5f, frontHandY - pr * 0.5f), end = Offset(frontHandX + faceDir * pr * 1.8f, frontHandY - pr * 1.8f), strokeWidth = 2.dp.toPx())
    // Front glove
    drawCircle(color = Color(0xFF1A1A1A), radius = 2.5f * scaleX, center = Offset(frontHandX, frontHandY))

    // --- SWORD SLASH ARC ANIMATION ---
    if (isSlashing) {
        val slashDir = if (player.direction == Direction.LEFT) -1f else 1f
        val slashPath = Path().apply {
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
