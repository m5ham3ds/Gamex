package com.example.game.render

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.game.player.PlayerState
import com.example.game.player.Direction

object SpriteRenderer {
    
    fun drawPlayerSprite(
        drawScope: DrawScope,
        player: PlayerState,
        isSlashing: Boolean
    ) {
        val px = player.x
        val py = player.y
        val pr = player.radius
        val faceDir = if (player.direction == Direction.RIGHT) 1f else -1f
        
        // This will eventually use bitmaps/spritesheets
        // For now, it provides a structured way to handle animations and layers
        
        drawScope.apply {
            // Placeholder: Simple body
            drawCircle(
                color = Color(0xFF1B2631),
                radius = pr,
                center = Offset(px, py)
            )
            
            // Mask
            drawCircle(
                color = Color.White,
                radius = pr * 0.7f,
                center = Offset(px + faceDir * pr * 0.3f, py - pr * 0.2f)
            )
            
            // Eyes
            drawCircle(
                color = Color.Black,
                radius = pr * 0.15f,
                center = Offset(px + faceDir * pr * 0.5f, py - pr * 0.2f)
            )
        }
    }
}
