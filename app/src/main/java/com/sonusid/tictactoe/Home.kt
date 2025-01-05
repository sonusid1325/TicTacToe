package com.sonusid.tictactoe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "TicTacToe") },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TicTacToeGame()
                }
            }
        }
    )
}

@Composable
fun TicTacToeGame() {
    // Mutable state for game board and variables
    var board by remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var gameOver by remember { mutableStateOf(false) }
    var playerXScore by remember { mutableStateOf(0) }
    var playerOScore by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display player scores
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Player X", fontWeight = FontWeight.Bold)
            Text("Player O", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("$playerXScore", fontWeight = FontWeight.Bold)
            Text("$playerOScore", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game Grid
        repeat(3) { row ->
            Row {
                repeat(3) { col ->
                    TicTacToeCell(
                        value = board[row][col], // Pass the cell's value
                        enabled = !gameOver && (board[row][col] == null), // Allow click only if game isn't over and cell is empty
                        onClick = {
                            // Update board and check for winner
                            board = board.mapIndexed { r, rowArray ->
                                rowArray.mapIndexed { c, cell ->
                                    if (r == row && c == col) currentPlayer else cell
                                }.toTypedArray()
                            }.toTypedArray()

                            if (checkWinner(board, currentPlayer)) {
                                winner = currentPlayer
                                gameOver = true
                                if (currentPlayer == "X") {
                                    playerXScore++ // Increment Player X score
                                } else {
                                    playerOScore++ // Increment Player O score
                                }
                            } else if (board.flatten().none { it == null }) {
                                winner = "Draw"
                                gameOver = true
                            } else {
                                currentPlayer = if (currentPlayer == "X") "O" else "X"
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Winner Announcement
        if (gameOver) {
            Text(
                text = when (winner) {
                    "Draw" -> "It's a Draw!"
                    else -> "Player $winner Wins!"
                },
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = Color.Green
            )
        }

        // Reset Button
        Button(
            onClick = {
                board = Array(3) { arrayOfNulls<String>(3) }
                currentPlayer = "X"
                winner = null
                gameOver = false
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Reset Game")
        }
    }
}



@Composable
fun TicTacToeCell(value: String?, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = RectangleShape
            )
            .clickable(enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (value != null) {
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

fun checkWinner(board: Array<Array<String?>>, player: String): Boolean {
    // Check rows and columns
    for (i in 0..2) {
        if ((board[i].all { it == player }) || (board.map { it[i] }.all { it == player })) {
            return true
        }
    }
    // Check diagonals
    if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
        (board[0][2] == player && board[1][1] == player && board[2][0] == player)
    ) {
        return true
    }
    return false
}
