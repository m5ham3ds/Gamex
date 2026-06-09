package com.example.game.engine

import com.example.game.core.GameConfig
import com.example.game.player.PlayerState
import com.example.game.player.Direction
import com.example.game.world.GameRegion
import kotlin.math.abs

object PhysicsEngine {
    
    fun updatePlayer(
        player: PlayerState,
        region: GameRegion,
        movingLeft: Boolean,
        movingRight: Boolean
    ): PlayerState {
        var vx = player.vx
        
        // Horizontal Movement
        if (movingLeft) {
            vx = -GameConfig.MOVE_SPEED
        } else if (movingRight) {
            vx = GameConfig.MOVE_SPEED
        } else {
            vx *= GameConfig.FRICTION
            if (abs(vx) < 0.15f) vx = 0f
        }
        
        // Gravity
        var vy = player.vy + GameConfig.GRAVITY
        if (vy > 14f) vy = 14f // Terminal velocity
        
        var nextX = player.x + vx
        var nextY = player.y + vy
        
        // Boundary Collisions (Simplified for now, will expand)
        if (nextX < 40f) nextX = 40f
        if (nextX > region.width - 40f) nextX = region.width - 40f
        
        // Platform Collisions (Basic landing logic)
        var grounded = false
        region.platforms.forEach { platform ->
            val playerLeft = nextX - player.radius
            val playerRight = nextX + player.radius
            val platLeft = platform.x
            val platRight = platform.x + platform.width
            
            if (playerRight > platLeft && playerLeft < platRight) {
                val feetY = player.y + player.radius
                if (feetY <= platform.y && nextY + player.radius >= platform.y) {
                    if (platform.isBouncy) {
                        vy = -18f
                    } else {
                        nextY = platform.y - player.radius
                        vy = 0f
                        grounded = true
                    }
                }
            }
        }
        
        val dir = when {
            vx < 0 -> Direction.LEFT
            vx > 0 -> Direction.RIGHT
            else -> player.direction
        }
        
        return player.copy(
            x = nextX,
            y = nextY,
            vx = vx,
            vy = vy,
            direction = dir,
            isGrounded = grounded
        )
    }
}
