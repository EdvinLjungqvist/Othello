package me.edvin.othello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import me.edvin.othello.game.GameScreen
import me.edvin.othello.ui.theme.OthelloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OthelloTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0, 158, 88))
                ) {
                    GameScreen()
                }
            }
        }
    }
}
