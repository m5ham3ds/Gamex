package com.example.game.save

import androidx.room.*

@Entity(tableName = "save_slots")
data class SaveSlot(
    @PrimaryKey val id: Int, // 1 to 10
    val playerName: String,
    val lastPlayed: Long,
    val playTimeMinutes: Int,
    val currentRegion: String,
    val playerPositionX: Float,
    val playerPositionY: Float,
    val hp: Float,
    val energy: Float,
    val forgetfulness: Float,
    val level: Int,
    val xp: Int,
    val memoryFragments: Int,
    val currency: Int,
    val score: Int,
    val jsonData: String // Full game state as JSON for flexibility
)

@Dao
interface SaveDao {
    @Query("SELECT * FROM save_slots ORDER BY lastPlayed DESC")
    suspend fun getAllSaves(): List<SaveSlot>

    @Query("SELECT * FROM save_slots WHERE id = :slotId")
    suspend fun getSaveById(slotId: Int): SaveSlot?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSave(save: SaveSlot)

    @Delete
    suspend fun deleteSave(save: SaveSlot)
}

@Database(entities = [SaveSlot::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract fun saveDao(): SaveDao
}
