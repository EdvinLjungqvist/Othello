package me.edvin.othello.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.edvin.othello.R
import me.edvin.othello.game.grid.square.SquareColor
import me.edvin.othello.ui.theme.OthelloBackground
import me.edvin.othello.ui.theme.OthelloContainer
import me.edvin.othello.ui.theme.OthelloGridBackground
import me.edvin.othello.ui.theme.OthelloGridBorder

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state = viewModel.state

    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (state == GameState.START) {
            Menu(viewModel)
        } else {
            Game(viewModel)
        }
    }
}

@Composable
fun Menu(viewModel: GameViewModel) {
    var dimension by remember { mutableIntStateOf((viewModel.dimension)) }
    var sounds by remember { mutableStateOf(viewModel.sounds) }

    Container(
        clickable = {
            viewModel.start(dimension, sounds)
        }
    ) {
        ContainerTitle("Othello Game")
        ContainerText("Click here to start!")
    }
    Container {
        ContainerTitle("Board Size ${dimension}x${dimension}")
        Slider(
            value = dimension.toFloat(),
            onValueChange = { dimension = it.toInt() },
            steps = 2,
            valueRange = 4f..10f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.LightGray,
                inactiveTrackColor = OthelloBackground
            ),
        )
        ContainerTitle("Sound Effects ${if (sounds) "ON" else "OFF"}")
        Switch(
            checked = sounds,
            onCheckedChange = { sounds = it },
            colors = SwitchDefaults.colors(
                checkedTrackColor = Color.LightGray,
                uncheckedTrackColor = OthelloBackground
            )
        )
    }
    Container {
        ContainerTitle("How To Play")
        ContainerText("Othello is a strategy board game played by two players who take turns placing their disks on a grid. The objective is to have the most disks of your color on the board by flipping your opponent's disks to your color through strategic placement.")
    }
}

@Composable
fun Game(viewModel: GameViewModel) {
    Grid(viewModel)
    Container {
        val state = viewModel.state

        if (state == GameState.END){
            Result(viewModel)
        }
        Info(viewModel)
    }
    Container(
        clickable = {
            viewModel.reset()
        }
    ) {
        ContainerTitle("Menu")
        ContainerText("Click here to return to menu!")
    }
}

@Composable
fun Grid(viewModel: GameViewModel) {
    val dimension = viewModel.grid?.dimension!!

    LazyVerticalGrid(
        modifier = Modifier
            .background(OthelloGridBackground)
            .aspectRatio(1f),
        verticalArrangement = Arrangement.Top,
        columns = GridCells.Fixed(dimension)
    ) {
        for (y in 0 until dimension) {
            for (x in 0 until dimension) {
                item { Square(viewModel, x, y) }
            }
        }
    }
}

@Composable
fun Square(viewModel: GameViewModel, x: Int, y: Int) {
    val context = LocalContext.current
    val canPlace = viewModel.canPlace(x, y)
    val squareColor = viewModel.grid?.getSquare(x, y)?.color
    var image: Int = -1
    var description: Int = -1

    if (canPlace && squareColor == SquareColor.UNSET) {
        image = R.drawable.disk_hint
        description = R.string.disk_hint_description
    } else if (squareColor == SquareColor.BLACK) {
        image = R.drawable.disk_black
        description = R.string.disk_black_description
    } else if (squareColor == SquareColor.WHITE) {
        image = R.drawable.disk_white
        description = R.string.disk_white_description
    }

    IconButton(
        modifier = Modifier
            .aspectRatio(1f)
            .border(1.dp, OthelloGridBorder, shape = RectangleShape)
            .padding(3.dp),
        onClick = {
            if (canPlace) {
                val sounds = viewModel.sounds

                viewModel.place(x, y)

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
        if (image != -1 || description != -1) {
            Image(
                painter = painterResource(image),
                contentDescription = stringResource(description)
            )
        }
    }
}


@Composable
fun Info(viewModel: GameViewModel) {
    ContainerText("Round: ${viewModel.round}")
    ContainerText("Player: ${viewModel.player.text}")
    ContainerText("Black Disks: ${viewModel.grid?.getSquareCount(SquareColor.BLACK)}")
    ContainerText("White Disks: ${viewModel.grid?.getSquareCount(SquareColor.WHITE)}")
}

@Composable
fun Result(viewModel: GameViewModel) {
    val winner = viewModel.getWinner()

    if (winner != null) {
        ContainerTitle("The winner is ${winner.text}!")
    } else {
        ContainerTitle("Draw!")
    }
}


@Composable
fun Container(
    clickable: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = OthelloContainer,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(20.dp)
            .let {
                if (clickable != null) {
                    it.clickable(onClick = clickable)
                } else {
                    it
                }
            },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun ContainerTitle(
    text: String
) {
    Text(
        text = text,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun ContainerText(
    text: String
) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = Color.White
    )
}
