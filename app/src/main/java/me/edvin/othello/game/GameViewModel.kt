package me.edvin.othello.game

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.edvin.othello.game.grid.Grid
import me.edvin.othello.game.grid.square.Square
import me.edvin.othello.game.grid.square.SquareColor

class GameViewModel: ViewModel() {
    var grid: Grid? = null
    var round: Int = 0
    var state by mutableStateOf(GameState.START)
        private set
    var player by mutableStateOf(SquareColor.BLACK)
        private set
    var dimension = 8
    var sounds = true

    fun playSound(context: Context, id: Int) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, id)

        mediaPlayer.start()
    }

    fun start() {
        state = GameState.PLAY
        grid = Grid(dimension)
    }

    fun end() {
        state = GameState.END
    }

    fun reset() {
        state = GameState.START
        player = SquareColor.BLACK
        round = 0
    }

    fun place(x: Int, y: Int) {
        val changes = getPlacementChanges(x, y)

        changes.forEach { it.color = player }
        grid?.getSquare(x, y)?.color = player
        player = getOpponent()
        round++
    }

    fun getWinner(): SquareColor? {
        val black = grid?.getSquareCount(SquareColor.BLACK)
        val white = grid?.getSquareCount(SquareColor.WHITE)

        return when {
            white != null && black != null -> {
                if (black > white) SquareColor.BLACK
                else if (white > black) SquareColor.WHITE
                else null
            }
            else -> null
        }
    }

    fun shouldEnd(): Boolean {
        return grid?.squares?.all { !canPlace(it.x, it.y) } ?: false
    }

    fun canPlace(x: Int, y: Int): Boolean {
        return grid?.getSquare(x, y)?.color == SquareColor.UNSET && getPlacementChanges(x, y).isNotEmpty()
    }

    fun getOpponent(): SquareColor {
        return if (player == SquareColor.BLACK) SquareColor.WHITE
        else SquareColor.BLACK
    }

    private fun getPlacementChanges(x: Int, y: Int): ArrayList<Square> {
        val changes = ArrayList<Square>()

        for (dx in -1 .. 1) {
            for (dy in -1 .. 1) {
                if (dx != 0 || dy != 0) {
                    val ray = ArrayList<Square>()

                    for (l in 1 ..grid?.dimension!!) {
                        val nx = x + dx * l
                        val ny = y + dy * l
                        val square = grid?.getSquare(nx, ny)
                        val color = square?.color

                        if (color != getOpponent()) {
                            if (color == player) {
                                changes.addAll(ray)
                            }
                            break
                        }
                        ray.add(square)
                    }
                }
            }
        }
        return changes
    }
}