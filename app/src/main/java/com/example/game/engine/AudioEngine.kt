package com.example.game.engine

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

object AudioEngine {
    private var musicPlayer: MediaPlayer? = null
    private val _isMusicMuted = MutableStateFlow(false)
    
    fun playMusic(context: Context, resId: Int, loop: Boolean = true) {
        musicPlayer?.stop()
        musicPlayer?.release()
        
        musicPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = loop
            setVolume(0.5f, 0.5f)
            start()
        }
    }
    
    fun stopMusic() {
        musicPlayer?.stop()
    }
    
    fun setVolume(volume: Float) {
        musicPlayer?.setVolume(volume, volume)
    }
}
