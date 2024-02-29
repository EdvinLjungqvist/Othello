package me.edvin.othello

import android.icu.text.IDNA.Info
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import me.edvin.othello.game.GameState
import me.edvin.othello.game.GameViewModel
import kotlin.math.pow

@Composable
fun GameScreen(viewModel: GameViewModel = remember { GameViewModel() }) {
	val state = viewModel.state

	if (state === GameState.START) {
		Menu(viewModel = viewModel)
	} else {
		Game(viewModel = viewModel)
	}
}

@Composable
fun Menu(viewModel: GameViewModel) {
	Button(
		modifier = Modifier
			.fillMaxWidth(),
		onClick = {viewModel.state = GameState.PLAY}
	) {
		Text(text = "Click to start!")
	}
}

@Composable
fun Game(viewModel: GameViewModel) {
	val dimension = viewModel.grid.dimension

	Column(
		modifier = Modifier.fillMaxSize()
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(1f)
		) {
			LazyVerticalGrid(
				modifier = Modifier
					.fillMaxSize(),
				columns = GridCells.Fixed(count = dimension)

			) {
				items(count = dimension.toDouble().pow(2.0).toInt()) { index ->
					Box(
						modifier = Modifier
							.fillMaxSize()
							.aspectRatio(1f)
							.border(1.dp, Color(0, 179, 98), shape = RectangleShape)
							.background(
								color = Color(0, 158, 88)
							),
						contentAlignment = Alignment.Center
					) {
						Square(index = index, viewModel = viewModel)
					}
				}
			}
		}
		Row(
			modifier = Modifier.fillMaxWidth()
		) {
			val state = viewModel.state

			if (state === GameState.PLAY) {
				Info(viewModel = viewModel)
			} else {
				Result(viewModel = viewModel)
			}
		}
	}
}

@Composable
fun Square(index: Int, viewModel: GameViewModel) {
	val x = index % viewModel.grid.dimension
	val y = index / viewModel.grid.dimension
	val value = viewModel.grid.getSquare(x, y)?.value

	var color = Color.Transparent

	if (value == -1 && viewModel.canPlace(x, y)) {
		color = Color(12, 190, 102, 255)
	} else if (value == 0) {
		color = Color(5, 5, 5)
	} else if (value == 1) {
		color = Color(250, 250, 250)
	}

	IconButton(
		modifier = Modifier
			.fillMaxSize()
			.padding(2.dp),
		colors = IconButtonDefaults.iconButtonColors(color),
		onClick = {
			viewModel.place(x, y)
		}
	) {
		Box(
			modifier = Modifier
				.aspectRatio(1f)
				.background(color = color)
		) {

		}
	}
}

@Composable
fun Info(viewModel: GameViewModel) {
	Text(text = "Game info...")
}

@Composable
fun Result(viewModel: GameViewModel) {
	Text(text = "Game results...")
}