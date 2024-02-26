package me.edvin.othello.game

import me.edvin.othello.grid.Grid
import me.edvin.othello.square.Square
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlin.math.max

class Game {
	val grid = Grid()
	var round by mutableIntStateOf(0) // Update composable on change

	private fun getPlacementChanges(x: Int, y: Int, player: Int): ArrayList<Square> {
		val changes = ArrayList<Square>()

		for (dx in -1 .. 1) {
			for (dy in -1 .. 1) {
				if (dx != 0 || dy != 0) {
					val ray = ArrayList<Square>()

					for (l in 1 .. max(grid.width, grid.height)) {
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
		}
	}

	fun canPlace(x: Int, y: Int): Boolean {
		return grid.getSquare(x, y)?.value == -1 && getPlacementChanges(x, y, getPlayer()).isNotEmpty()
	}

	fun getPlayer(): Int {
		return round % 2
	}

	fun getOpponent(): Int {
		return (round + 1) % 2
	}
}