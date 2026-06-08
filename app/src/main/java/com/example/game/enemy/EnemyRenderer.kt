package com.example.game.enemy

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.game.player.Direction
import com.example.ui.theme.*

fun DrawScope.drawEnemy(
    enemy: Enemy,
    scaleX: Float,
    scaleY: Float
) {
    val ex = enemy.x * scaleX
    val ey = enemy.y * scaleY
    val r = enemy.radius * scaleX
    val faceDir = if (enemy.direction == Direction.LEFT) -1f else 1f

    when (enemy.type) {
        EnemyType.SCRAB_SCAVENGER, EnemyType.ASHWARDEN, EnemyType.ROPE_CROAKER, EnemyType.GEARFOLK -> {
            // Humanoid shape with limbs
            
            // Legs
            drawLine(color = Color(0xFF1B1B1B), start = Offset(ex - r*0.3f, ey + r), end = Offset(ex - r*0.3f, ey + r*1.8f), strokeWidth = 3.dp.toPx())
            drawLine(color = Color(0xFF2B2B2B), start = Offset(ex + r*0.3f, ey + r), end = Offset(ex + r*0.3f, ey + r*1.8f), strokeWidth = 3.dp.toPx())
            
            // Body
            drawRoundRect(
                color = SurfaceDark, 
                topLeft = Offset(ex - r, ey - r*0.5f), 
                size = Size(r*2, r*1.8f), 
                cornerRadius = CornerRadius(4 * scaleX)
            )
            
            // Arms
            drawLine(color = Color(0xFF4A4A4A), start = Offset(ex, ey), end = Offset(ex + faceDir * r * 1.5f, ey + r), strokeWidth = 3.dp.toPx())
            
            // Weapon (Spear/Mace)
            if (enemy.type == EnemyType.ASHWARDEN || enemy.type == EnemyType.ROPE_CROAKER) {
                drawLine(color = Color(0xFF8D6E63), start = Offset(ex + faceDir*r, ey + r*1.5f), end = Offset(ex + faceDir*r*2.5f, ey - r*0.5f), strokeWidth = 2.dp.toPx())
                drawCircle(color = Color(0xFF666666), radius = r*0.4f, center = Offset(ex + faceDir*r*2.5f, ey - r*0.5f))
            }
            
            // Head
            drawCircle(color = SurfaceDark, radius = r * 0.8f, center = Offset(ex, ey - r*0.8f))
            
            // Eyes
            drawCircle(color = VitalityRed, radius = r * 0.2f, center = Offset(ex + faceDir * r * 0.4f, ey - r*0.9f))
        }
        EnemyType.PAGE_SCRAPER, EnemyType.SHARDLING, EnemyType.ROOTCRAWLER -> {
            // Creeping spider-like shape
            drawCircle(color = Color(0xFF006064), radius = r, center = Offset(ex, ey))
            drawCircle(color = EchoesBlue, radius = r * 0.3f, center = Offset(ex, ey))
            // Moving Legs
            for (i in -1..1 step 2) {
                drawLine(EchoesBlue, Offset(ex - r, ey), Offset(ex - r - 10f * scaleX, ey + i * 10f * scaleY), strokeWidth = 2.dp.toPx())
                drawLine(EchoesBlue, Offset(ex + r, ey), Offset(ex + r + 10f * scaleX, ey + i * 10f * scaleY), strokeWidth = 2.dp.toPx())
                drawLine(EchoesBlue, Offset(ex, ey - r), Offset(ex + i * 5f * scaleX, ey - r - 10f * scaleY), strokeWidth = 2.dp.toPx())
            }
        }
        EnemyType.ECHO_SHADE, EnemyType.GLOW_WISP -> {
            // Pulsating ethereal sphere
            val time = System.currentTimeMillis()
            val pulse = (Math.sin(time / 150.0) + 1.0) / 2.0
            val pr = r + (pulse * 4f * scaleX).toFloat()
            drawCircle(color = Color(0xFF4A148C), radius = pr, center = Offset(ex, ey), style = Stroke(width = 2.dp.toPx()))
            drawCircle(color = Color(0xFF9C27B0), radius = pr * 0.5f, center = Offset(ex, ey))
            drawCircle(color = Color(0xFFE1BEE7), radius = pr * 0.2f, center = Offset(ex, ey))
        }
        EnemyType.DRIFT_KNIGHT -> {
            // Hovering geometric sentinel
            val path = Path().apply {
                moveTo(ex, ey - r * 1.5f)
                lineTo(ex + r, ey)
                lineTo(ex, ey + r * 1.5f)
                lineTo(ex - r, ey)
                close()
            }
            drawPath(path, color = BlightGold)
            drawCircle(color = RadianceWhite, radius = r * 0.25f, center = Offset(ex, ey))
            
            // Floating shoulders
            drawCircle(color = BlightGold, radius = r * 0.3f, center = Offset(ex - r * 1.2f, ey - r*0.5f))
            drawCircle(color = BlightGold, radius = r * 0.3f, center = Offset(ex + r * 1.2f, ey - r*0.5f))
            
            // Sword
            drawLine(color = RadianceWhite, start = Offset(ex + faceDir * r * 1.2f, ey), end = Offset(ex + faceDir * r * 1.2f, ey + r * 2.5f), strokeWidth = 3.dp.toPx())
        }
    }

    // Health bar
    if (enemy.hp < enemy.maxHp) {
        val barW = 20f * scaleX
        val barH = 3f * scaleY
        val hpPct = enemy.hp / enemy.maxHp
        drawRoundRect(
            color = Color.DarkGray,
            topLeft = Offset(ex - barW / 2, ey - r - 12f * scaleY),
            size = Size(barW, barH)
        )
        drawRoundRect(
            color = VitalityRed,
            topLeft = Offset(ex - barW / 2, ey - r - 12f * scaleY),
            size = Size(barW * hpPct, barH)
        )
    }
}
