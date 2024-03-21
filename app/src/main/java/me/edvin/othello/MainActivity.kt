package me.edvin.othello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import me.edvin.othello.ui.theme.OthelloTheme

// TODO:
// - Use themes
// - Handle screen rotations

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			OthelloTheme {
				GameScreen()
			}
		}
	}
}