package com.example.game.enemy

import com.example.game.player.PlayerState
import com.example.game.player.Direction
import kotlin.math.abs

enum class EnemyState {
    IDLE, PATROL, CHASE, ATTACK, FLEE, STUNNED
}

object EnemyAI {
    
    fun updateEnemy(
        enemy: Enemy,
        player: PlayerState
    ): Enemy {
        val dist = abs(enemy.x - player.x)
        val nextState = when {
            dist < 100f -> EnemyState.ATTACK
            dist < 300f -> EnemyState.CHASE
            else -> EnemyState.PATROL
        }
        
        var vx = enemy.vx
        var dir = enemy.direction
        
        if (nextState == EnemyState.CHASE) {
            vx = if (player.x > enemy.x) 2.5f else -2.5f
            dir = if (vx > 0) Direction.RIGHT else Direction.LEFT
        } else if (nextState == EnemyState.PATROL) {
            // Keep current patrol logic for now
            if (abs(vx) < 0.1f) vx = 2f
        }
        
        return enemy.copy(
            vx = vx,
            direction = dir
            // In the future, we will add 'state' field to Enemy data class
        )
    }
}
