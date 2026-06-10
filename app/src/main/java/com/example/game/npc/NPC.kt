package com.example.game.npc

data class NPC(
    val id: String,
    val name: String,
    val regionId: String,
    val x: Float,
    val y: Float,
    val dialogueId: String
)

object NPCManager {
    val npcs = listOf(
        NPC("edda", "Edda", "ashen", 400f, 550f, "edda_greeting"),
        NPC("gideon", "Gideon", "clockworks", 600f, 400f, "gideon_greeting")
    )
}
