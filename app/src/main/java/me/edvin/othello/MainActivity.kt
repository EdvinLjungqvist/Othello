package me.edvin.othello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import me.edvin.othello.game.GameScreen
import me.edvin.othello.game.GameViewModel
import me.edvin.othello.ui.theme.OthelloBackground
import me.edvin.othello.ui.theme.OthelloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OthelloTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    color = OthelloBackground
                ) {
                    GameScreen(GameViewModel())
                }
            }
        }
    }
}
