package me.edvin.othello.game.grid

import me.edvin.othello.game.grid.square.Square

class Grid {
	val dimension = 4
	val squares = ArrayList<Square>()

	init {
		val c = dimension / 2

		for (x in 0 until dimension) {
			for (y in 0 until dimension) {
				val square = Square(x, y)

				if ((x == c && y == c - 1) || (x == c - 1 && y == c)) {
					square.value = 0
				} else if ((x == c - 1 && y == c - 1) || (x == c && y == c)) {
					square.value = 1
				}
				squares.add(square)
			}
		}
	}

	fun getSquare(x: Int, y: Int): Square? {
		return squares.firstOrNull { it.x == x && it.y == y }
	}

	fun getSquareCount(player: Int): Int {
		var count = 0

		for (square in squares) {
			if (square.value == player) {
				count++
			}
		}
		return count
	}
}
