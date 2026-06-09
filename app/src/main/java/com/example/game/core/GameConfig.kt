package com.example.game.core

object GameConfig {
    // Physics
    const val GRAVITY = 0.8f
    const val JUMP_FORCE = -15f
    const val MOVE_SPEED = 6f
    const val FRICTION = 0.9f
    
    // Player Stats
    const val MAX_HP = 100f
    const val MAX_ENERGY = 100f
    const val MAX_FM = 25f // Max Forgetfulness Meter
    
    // Combat
    const val PISTOL_COST = 10f
    const val DASH_COST = 20f
    const val MELEE_DAMAGE = 20f
    const val RANGE_DAMAGE = 15f
    
    // World
    const val TILE_SIZE = 64f
    const val CHUNK_SIZE = 16
}
