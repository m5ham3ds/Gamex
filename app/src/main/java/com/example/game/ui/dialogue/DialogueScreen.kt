package com.example.game.ui.dialogue

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.quest.DialogueLine
import com.example.ui.theme.*

@Composable
fun DialogueScreen(
    line: DialogueLine,
    onChoice: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { if (line.choices.isEmpty()) onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(SurfaceDark.copy(alpha = 0.95f), RoundedCornerShape(12.dp))
                .border(2.dp, BlightGold, RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Text(
                text = line.speaker,
                color = BlightGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = line.text,
                color = OnSurfaceLight,
                fontSize = 15.sp
            )
            
            if (line.choices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                line.choices.forEachIndexed { index, choice ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFF232C33), RoundedCornerShape(6.dp))
                            .border(1.dp, OutlineGray, RoundedCornerShape(6.dp))
                            .clickable { onChoice(index) }
                            .padding(12.dp)
                    ) {
                        Text(choice, color = OnSurfaceLight, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
