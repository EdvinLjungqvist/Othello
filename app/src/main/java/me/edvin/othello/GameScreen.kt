package me.edvin.othello

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edvin.othello.game.GameState
import me.edvin.othello.game.GameViewModel
import me.edvin.othello.game.grid.Grid
import me.edvin.othello.game.player.Player
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
	var dimension by remember { mutableFloatStateOf(8f) } // Default dimension is 8
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "Othello Game",
			style = TextStyle(
				fontWeight = FontWeight.Bold,
				fontSize = 40.sp,
				color = Color.Black
			),
			modifier = Modifier.padding(bottom = 16.dp)
		)
		Button(
			modifier = Modifier
				.fillMaxWidth(0.6f)
				.padding(bottom = 16.dp),
			onClick = {
				viewModel.grid = Grid(dimension.toInt());
				viewModel.state = GameState.PLAY
			}
		) {
			Text(text = "Click to start!")
		}
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Column {
				Slider(
					value = dimension,
					onValueChange = { dimension = it },
					steps = 2,
					valueRange = 4f..10f,
					modifier = Modifier
						.width(200.dp)
						.align(Alignment.CenterHorizontally),
				)
				Text(
					text = "Board Size: ${dimension.toInt()}x${dimension.toInt()}",
					modifier = Modifier.fillMaxWidth(),
					style = TextStyle(
						fontSize = 18.sp,
						textAlign = TextAlign.Center,
						color = Color.Black
					)
				)
			}
		}
	}
}



@Composable
fun Game(viewModel: GameViewModel) {
	val dimension = viewModel.grid?.dimension

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
				columns = GridCells.Fixed(count = dimension!!)

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
			Column(
				modifier = Modifier.fillMaxWidth()
			){
				val state = viewModel.state

				if (state === GameState.PLAY) {
					Info(viewModel = viewModel)
				} else {
					Result(viewModel = viewModel)
				}
			}
		}
	}
}

@Composable
fun Square(index: Int, viewModel: GameViewModel) {
	val grid = viewModel.grid
	val dimension = grid?.dimension
	val x = index % dimension!!
	val y = index / dimension

	val value = grid.getSquare(x, y)?.value

	var color = Color.Transparent

	if (value == -1 && viewModel.canPlace(x, y)) {
		color = Color(12, 190, 102, 255)
	} else if (value == Player.BLACK.value) {
		color = Color(5, 5, 5)
	} else if (value == Player.WHITE.value) {
		color = Color(250, 250, 250)
	}

	val context = LocalContext.current

	IconButton(
		modifier = Modifier
			.fillMaxSize()
			.padding(2.dp),
		colors = IconButtonDefaults.iconButtonColors(color),
		onClick = {
			if (!viewModel.place(x, y)) {
				Toast.makeText(context, "Invalid square, try again!", Toast.LENGTH_SHORT).show()
			}
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
	val grid = viewModel.grid

	Text(text = "Round: ${viewModel.round}")
	Text(text = "Black: ${grid?.getSquareCount(Player.BLACK.value)}")
	Text(text = "White: ${grid?.getSquareCount(Player.WHITE.value)}")

	Button(
		modifier = Modifier
			.fillMaxWidth(0.6f)
			.padding(bottom = 16.dp),
		onClick = {

		}
	) {
		Text(text = "Restart")
	}
}

@Composable
fun Result(viewModel: GameViewModel) {
	val winner = viewModel.getWinner()
	
	if (winner != null) {
		Text(text = "${winner.text} has won!")
	} else {
		Text(text = "Draw!")
	}
}