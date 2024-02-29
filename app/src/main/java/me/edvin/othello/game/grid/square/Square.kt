package me.edvin.othello.game.grid.square

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class Square(val x: Int, val y: Int) {
	var value by mutableIntStateOf(-1)
}
