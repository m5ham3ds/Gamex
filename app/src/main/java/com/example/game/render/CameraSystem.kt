package com.example.game.render

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.game.player.PlayerState

object CameraSystem {
    private val _cameraX = mutableStateOf(0f)
    private val _cameraY = mutableStateOf(0f)
    
    val cameraX: State<Float> = _cameraX
    val cameraY: State<Float> = _cameraY
    
    fun update(player: PlayerState, screenWidth: Float, screenHeight: Float) {
        // Simple follow logic
        _cameraX.value = player.x - screenWidth / 2
        _cameraY.value = player.y - screenHeight / 2
    }
}
