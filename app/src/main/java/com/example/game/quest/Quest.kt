package com.example.game.quest

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val rewardXp: Int = 0,
    val rewardFragments: Int = 0
)

object QuestManager {
    private val _quests = mutableListOf<Quest>()
    
    fun addQuest(quest: Quest) {
        _quests.add(quest)
    }
    
    fun completeQuest(id: String) {
        val index = _quests.indexOfFirst { it.id == id }
        if (index != -1) {
            _quests[index] = _quests[index].copy(isCompleted = true)
        }
    }
}
