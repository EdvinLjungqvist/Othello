package me.edvin.othello.game

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edvin.othello.R
import me.edvin.othello.game.grid.square.SquareColor
import kotlin.math.pow

@Composable
fun GameScreen(viewModel: GameViewModel = remember { GameViewModel() }) {
    val state = viewModel.state

    if (state == GameState.START) {
        Menu(viewModel = viewModel)
    } else {
        Game(viewModel = viewModel)
    }
}

@Composable
fun Menu(viewModel: GameViewModel) {
    var dimension by remember { mutableIntStateOf(viewModel.dimension) }
    var sounds by remember { mutableStateOf(viewModel.sounds) }

    Column(
        modifier = Modifier
            .background(color = Color(0, 158, 88))
            .fillMaxSize()
            .padding(all = 12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        viewModel.dimension = dimension
                        viewModel.sounds = sounds
                        viewModel.start()
                    }
                    .fillMaxWidth()
                    .background(
                        color = Color(0, 179, 98),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Othello Game",
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
                Text(
                    text = "Click To Start!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0, 179, 98),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Board Size",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Slider(
                    value = dimension.toFloat(),
                    onValueChange = { dimension = it.toInt() },
                    steps = 2,
                    valueRange = 4f..10f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(20, 199, 128),
                        inactiveTrackColor = Color(0, 158, 88)
                    ) ,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = "${dimension}x${dimension}",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp) // Added top padding
                )

                Text(
                    text = "Sound Effects",
                    fontSize = 24.sp, // Increased font size
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                Switch(
                    checked = sounds,
                    onCheckedChange = {
                        sounds = it
                    }
                )
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0, 179, 98),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Game Info",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = "Othello is a strategy board game played by two players who take turns placing their discs on a grid. The objective is to have the most discs of your color on the board by flipping your opponent's discs to your color through strategic placement.",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun Game(viewModel: GameViewModel) {
    val dimension = viewModel.grid?.dimension!!

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(color = Color(0, 158, 88))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(count = dimension)

            ) {
                items(count = dimension.toDouble().pow(2).toInt()) { index ->
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
            modifier = Modifier.padding(all = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0, 179, 98),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val state = viewModel.state

                if (state == GameState.PLAY) {
                    Info(viewModel = viewModel)
                } else {
                    Result(viewModel = viewModel)
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(all = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .clickable { viewModel.reset() }
                    .fillMaxWidth()
                    .background(
                        color = Color(0, 179, 98),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Return To Menu!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun Square(index: Int, viewModel: GameViewModel) {
    val grid = viewModel.grid
    val dimension = grid?.dimension!!
    val x = index % dimension
    val y = index / dimension
    val squareColor = grid.getSquare(x, y)?.color
    var color = Color.Transparent

    val context = LocalContext.current

    if (squareColor == SquareColor.UNSET && viewModel.canPlace(x, y)) {
        color = Color(12, 190, 102, 255)
    } else if (squareColor == SquareColor.BLACK) {
        color = Color(5, 5, 5)
    } else if (squareColor == SquareColor.WHITE) {
        color = Color(250, 250, 250)
    }

    IconButton(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        colors = IconButtonDefaults.iconButtonColors(color),
        onClick = {
            if (viewModel.canPlace(x, y)) {
                viewModel.place(x, y)

                val sounds = viewModel.sounds

                if (viewModel.shouldEnd()) {
                    viewModel.end()

                    if (sounds) {
                        viewModel.playSound(context, R.raw.end)
                    }
                } else if (sounds) {
                    viewModel.playSound(context, R.raw.place)
                }
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

    Text(
        text = "Round: ${viewModel.round}",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    Text(
        text = "Player: ${viewModel.player.text}",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    Text(
        text = "Black Count: ${grid?.getSquareCount(SquareColor.BLACK)}",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    Text(
        text = "White Count: ${grid?.getSquareCount(SquareColor.WHITE)}",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun Result(viewModel: GameViewModel) {
    val winner = viewModel.getWinner()
    val grid = viewModel.grid


    if (winner !== null) {
        Text(
            text = "The winner is ${winner.text}! (${grid?.getSquareCount(SquareColor.BLACK)}/${grid?.getSquareCount(SquareColor.WHITE)})",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    } else {
        val count = grid?.getSquareCount(SquareColor.BLACK)

        Text(
            text = "Draw (${count}/${count})",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}
