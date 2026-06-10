package com.example.game.quest

data class DialogueLine(
    val speaker: String,
    val text: String,
    val choices: List<String> = emptyList()
)

object DialogueSystem {
    private val dialogues = mapOf(
        "edda_greeting" to listOf(
            DialogueLine("Edda", "الورق لا يكذب — لكنه يتعب. هل جئت للمساعدة؟", listOf("نعم", "ليس الآن"))
        ),
        "gideon_greeting" to listOf(
            DialogueLine("Gideon", "أهلا بك في ممرات التروس. هل تبحث عن قطع غيار؟", listOf("أرني خردتك", "فقط أمرّ من هنا"))
        )
    )
    
    fun getDialogue(id: String): List<DialogueLine> = dialogues[id] ?: emptyList()
}
