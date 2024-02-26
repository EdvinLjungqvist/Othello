package me.edvin.othello

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
import me.edvin.othello.game.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel = remember { GameViewModel() }) {
	val width = viewModel.game.grid.width
	val height = viewModel.game.grid.height

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
				columns = GridCells.Fixed(count = width)
			) {
				items(count = width * height) { index ->
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
			Text(
				text = "Round: ${viewModel.game.round}",
				color = Color.Black
			)
		}
	}
}


@Composable
fun Square(index: Int, viewModel: GameViewModel) {
	val x = index % viewModel.game.grid.width
	val y = index / viewModel.game.grid.width
	val value = viewModel.game.grid.getSquare(x, y)?.value

	var color = Color.Transparent

	if (value == -1 && viewModel.game.canPlace(x, y)) {
		color = Color(7, 213, 110)
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
			viewModel.game.place(x, y)
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