package com.example.game.world

import androidx.compose.ui.graphics.Color

data class Projectile(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val radius: Float = 6f,
    val isPlayerOwned: Boolean = false
)

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val alpha: Float = 1.0f,
    val size: Float = 4f,
    val life: Int = 30 // frames
)

data class OracleRelic(
    val x: Float,
    val y: Float,
    val radius: Float = 24f,
    val isUsed: Boolean = false
)
