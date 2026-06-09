package com.example.game.player

import com.example.game.core.GameConfig
import com.example.game.player.PlayerState

object PlayerController {
    
    fun handleJump(player: PlayerState): PlayerState {
        return if (player.isGrounded) {
            player.copy(vy = GameConfig.JUMP_FORCE, isGrounded = false)
        } else {
            player
        }
    }
    
    fun useEnergy(player: PlayerState, amount: Float): PlayerState? {
        return if (player.energy >= amount) {
            player.copy(energy = player.energy - amount)
        } else {
            null
        }
    }
    
    fun applyDamage(player: PlayerState, damage: Float): PlayerState {
        if (player.soulShieldActive) {
            return player.copy(soulShieldActive = false)
        }
        val nextHp = (player.hp - damage).coerceAtLeast(0f)
        // Gain energy on taking damage (balancing mechanic)
        val nextEnergy = (player.energy + 5f).coerceAtMost(player.maxEnergy)
        return player.copy(hp = nextHp, energy = nextEnergy)
    }
}
