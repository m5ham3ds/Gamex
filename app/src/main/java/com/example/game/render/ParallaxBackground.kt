package com.example.game.render

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

object ParallaxBackground {
    
    fun drawParallax(
        drawScope: DrawScope,
        cameraX: Float,
        cameraY: Float,
        screenWidth: Float,
        screenHeight: Float,
        regionColor: Color
    ) {
        drawScope.apply {
            // Background Layer 1 (Slowest)
            drawRect(
                color = regionColor.copy(alpha = 0.3f),
                size = Size(screenWidth, screenHeight)
            )
            
            // Midground Layer 2
            for (i in 0..5) {
                val x = (i * 400f - cameraX * 0.3f) % (screenWidth + 400f)
                drawRect(
                    color = regionColor.copy(alpha = 0.5f),
                    topLeft = Offset(x, screenHeight - 200f),
                    size = Size(200f, 200f)
                )
            }
        }
    }
}
