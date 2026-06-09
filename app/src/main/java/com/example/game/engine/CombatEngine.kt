package com.example.game.engine

import com.example.game.core.GameConfig
import com.example.game.player.PlayerState
import com.example.game.player.Direction
import com.example.game.enemy.Enemy
import com.example.game.world.Projectile
import kotlin.math.abs

object CombatEngine {
    
    fun calculateMeleeHit(
        player: PlayerState,
        enemies: List<Enemy>,
        isHeavy: Boolean
    ): List<Pair<Int, Float>> {
        val range = if (isHeavy) 120f else 90f
        val damage = if (isHeavy) GameConfig.MELEE_DAMAGE * 1.5f else GameConfig.MELEE_DAMAGE
        val hits = mutableListOf<Pair<Int, Float>>()
        
        enemies.forEachIndexed { index, enemy ->
            val dist = abs(enemy.x - player.x)
            val isWithinHeightY = abs(enemy.y - player.y) < 60f
            
            if (dist < range && isWithinHeightY) {
                val isLookingAtEnemy = (player.direction == Direction.RIGHT && enemy.x > player.x) ||
                                       (player.direction == Direction.LEFT && enemy.x < player.x)
                if (isLookingAtEnemy) {
                    hits.add(index to damage)
                }
            }
        }
        return hits
    }
    
    fun isProjectileHitting(
        proj: Projectile,
        targetX: Float,
        targetY: Float,
        targetRadius: Float
    ): Boolean {
        val dx = proj.x - targetX
        val dy = proj.y - targetY
        val distSq = dx * dx + dy * dy
        val combinedRadius = proj.radius + targetRadius
        return distSq < combinedRadius * combinedRadius
    }
}
