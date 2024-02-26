package me.edvin.othello.grid

import me.edvin.othello.square.Square

class Grid {
	val width = 6
	val height = 6
	private val squares = ArrayList<Square>()

	init {
		val cx = width / 2
		val cy = height / 2

		for (x in 0 until width) {
			for (y in 0 until height) {
				val square = Square(x, y)

				if ((x == cx && y == cy - 1) || (x == cx - 1 && y == cy)) {
					square.value = 0
				} else if ((x == cx - 1 && y == cy - 1) || (x == cx && y == cy)) {
					square.value = 1
				}
				squares.add(square)
			}
		}
	}

	fun getSquare(x: Int, y: Int): Square? {
		return squares.firstOrNull { it.x == x && it.y == y }
	}
}
