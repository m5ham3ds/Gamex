package com.example.game.engine

import androidx.compose.ui.graphics.Color
import com.example.game.world.Particle

object ParticleEngine {
    
    fun createSparks(x: Float, y: Float, color: Color, count: Int = 10): List<Particle> {
        val list = mutableListOf<Particle>()
        repeat(count) {
            list.add(
                Particle(
                    x = x,
                    y = y,
                    vx = (Math.random() * 4 - 2).toFloat(),
                    vy = (Math.random() * 4 - 2).toFloat(),
                    color = color,
                    life = 30
                )
            )
        }
        return list
    }
    
    fun updateParticles(particles: List<Particle>): List<Particle> {
        return particles.map { part ->
            part.copy(
                x = part.x + part.vx,
                y = part.y + part.vy,
                alpha = part.alpha * 0.92f,
                life = part.life - 1
            )
        }.filter { it.life > 0 }
    }
}
