package me.edvin.othello.game.grid.square

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Square(val x: Int, val y: Int) {
    var color by mutableStateOf(SquareColor.UNSET)
}
