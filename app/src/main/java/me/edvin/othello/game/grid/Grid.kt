package me.edvin.othello.game.grid

import me.edvin.othello.game.grid.square.Square
import me.edvin.othello.game.grid.square.SquareColor

class Grid (val dimension: Int, val squares: ArrayList<Square> = ArrayList()) {

	init {
		val center = dimension / 2

		for (x in 0 until dimension) {
			for (y in 0 until dimension) {
				val square = Square(x, y)

				when {
					(x == center && y == center - 1) || (x == center - 1 && y == center) ->
						square.color = SquareColor.BLACK
					(x == center - 1 && y == center - 1) || (x == center && y == center) ->
						square.color = SquareColor.WHITE
				}
				squares.add(square)
			}
		}
	}

	fun getSquareCount(color: SquareColor): Int {
		return squares.count { it.color == color }
	}

	fun getSquare(x: Int, y: Int): Square? {
		return squares.firstOrNull { it.x == x && it.y == y }
	}
}
