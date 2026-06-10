package com.example.game.save

import android.content.Context
import androidx.room.Room

object GameDatabaseProvider {
    private var instance: GameDatabase? = null
    
    fun getDatabase(context: Context): GameDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                GameDatabase::class.java, "game_db"
            ).build()
            instance = db
            db
        }
    }
}
