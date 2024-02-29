package me.edvin.othello.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.edvin.othello.game.grid.Grid
import me.edvin.othello.game.grid.square.Square
import me.edvin.othello.game.player.Player

class GameViewModel: ViewModel() {
	val grid = Grid()
	var state by mutableStateOf(GameState.START)
	var round by mutableIntStateOf(0)

	private fun getPlacementChanges(x: Int, y: Int, player: Int): ArrayList<Square> {
		val changes = ArrayList<Square>()

		for (dx in -1 .. 1) {
			for (dy in -1 .. 1) {
				if (dx != 0 || dy != 0) {
					val ray = ArrayList<Square>()

					for (l in 1 .. grid.dimension) {
						val nx = x + dx * l
						val ny = y + dy * l
						val square = grid.getSquare(nx, ny)
						val value = square?.value

						if (value != getOpponent()) {
							if (value == player) {
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

	fun place(x: Int, y: Int) {
		if (canPlace(x, y)) {
			val player = getPlayer()
			val changes = getPlacementChanges(x, y, player)

			for (square in changes) {
				square.value = player
			}
			grid.getSquare(x, y)?.value = player
			round++

			if (getPossiblePlacements().isEmpty()) {
				state = GameState.END
			}
		}
	}

	private fun getPossiblePlacements(): List<Square> {
		val possible = ArrayList<Square>()

		for (square in grid.squares) {
			if (canPlace(square.x, square.y)) {
				possible.add(square)
			}
		}
		return possible
	}

	fun getWinner(): Player? {
		val black = grid.getSquareCount(Player.BLACK.value)
		val white = grid.getSquareCount(Player.WHITE.value)

		if (black > white) {
			return Player.BLACK
		} else if (white > black) {
			return Player.WHITE
		} else {
			return null
		}
	}

	fun canPlace(x: Int, y: Int): Boolean {
		return grid.getSquare(x, y)?.value == -1 && getPlacementChanges(x, y, getPlayer()).isNotEmpty()
	}

	private fun getPlayer(): Int {
		return round % 2
	}

	private fun getOpponent(): Int {
		return (round + 1) % 2
	}
}