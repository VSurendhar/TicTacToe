package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Coin
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.PlayerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith


@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UnusedFlow")
class SimpleGameControllerTest {


    private lateinit var gameController: MultiPlayerGame
    private val player1 = Player(PlayerType.HUMAN, Coin.X)
    private val player2 = Player(PlayerType.HUMAN, Coin.O)
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        gameController = MultiPlayerGame(
            PlayerDetails(listOf(player1, player2))
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `Able to move coin to board at given coordinate`() =runTest{
        gameController.addMove(Coordinate(0, 0))

        assertEquals(player1, gameController.getGameBoard()[0][0].player)
    }


    @Test
    fun `Not able to add coin in occupied coordinate`() =runTest{
        gameController.addMove(Coordinate(0, 0))

        val exception = assertFailsWith<RuntimeException> {
            gameController.addMove(Coordinate(0, 0))
        }

        assertEquals("(0,0) Cell already occupied", exception.message)
        assertEquals("(0,0) Cell already occupied", exception.message)
    }


    @Test
    fun `Not able to add coin in invalid coordinate`() = runTest {

        val exception = assertFailsWith<RuntimeException> {
            gameController.addMove(Coordinate(5, 5))
        }

        assertEquals("Invalid Coordinate", exception.message)
    }

    @Test
    fun `Able to add the Coin to the Board and Coin is placed in the Board`() = runTest {

        gameController.addMove(Coordinate(1, 1))

        val board = gameController.getGameBoard()
        assertEquals(player1, board[1][1].player)

    }

    @Test
    fun `Board state updates after move`() =runTest{

        gameController.addMove(Coordinate(0, 0))

        val board = gameController.getGameBoard()

        assertEquals(player1, board[0][0].player)
    }

    @Test
    fun `Detect win - Horizontal`() = runTest(StandardTestDispatcher()) {
        var status: GameStatus? = null

        val emissions = mutableListOf<GameStatus>()

        val job = launch {
            gameController.gameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        gameController.addMove(Coordinate(0, 0)) // X
        gameController.addMove(Coordinate(1, 0)) // O
        gameController.addMove(Coordinate(0, 1)) // X
        gameController.addMove(Coordinate(1, 1)) // O
        gameController.addMove(Coordinate(0, 2)) // X wins

        advanceUntilIdle()

        status = emissions.last()
        assertTrue(status is GameStatus.Won)
        assertEquals(player1, (status as GameStatus.Won).winner)

        job.cancel()
    }


    @Test
    fun `Detect win - Vertical`() = runTest {

        val emissions = mutableListOf<GameStatus>()

        val job = launch {
            gameController.gameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        var status: GameStatus? = null

        gameController.addMove(Coordinate(0, 0)) // X
        gameController.addMove(Coordinate(0, 1)) // O
        gameController.addMove(Coordinate(1, 0)) // X
        gameController.addMove(Coordinate(1, 1)) // O
        gameController.addMove(Coordinate(2, 0)) // X wins

        status = emissions.last()

        assertTrue(status is GameStatus.Won)
        assertEquals(player1, (status as GameStatus.Won).winner)

        advanceUntilIdle()

        job.cancel()
    }


    @Test
    fun `Detect win - Main Diagonal`() = runTest {


        val emissions = mutableListOf<GameStatus>()

        val job = launch {
            gameController.gameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        var status: GameStatus? = null

        gameController.addMove(Coordinate(0, 0)) // X
        gameController.addMove(Coordinate(0, 1)) // O
        gameController.addMove(Coordinate(1, 1)) // X
        gameController.addMove(Coordinate(0, 2)) // O
        gameController.addMove(Coordinate(2, 2)) // X wins

        status = emissions.last()

        assertTrue(status is GameStatus.Won)
        assertEquals(player1, (status as GameStatus.Won).winner)

        job.cancel()
    }


    @Test
    fun `Detect win - Anti Diagonal`() = runTest {

        val emissions = mutableListOf<GameStatus>()

        val job = launch {
            gameController.gameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        gameController.addMove(Coordinate(0, 2)) // X
        gameController.addMove(Coordinate(0, 0)) // O
        gameController.addMove(Coordinate(1, 1)) // X
        gameController.addMove(Coordinate(0, 1)) // O
        gameController.addMove(Coordinate(2, 0)) // X wins

        val status = emissions.last()

        assertTrue(status is GameStatus.Won)
        assertEquals(player1, (status as GameStatus.Won).winner)

        job.cancel()
    }

    @Test
    fun `Winning event contains correct winning cells`() = runTest {

        val emissions = mutableListOf<GameStatus>()

        val job = launch {
            gameController.gameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        gameController.addMove(Coordinate(0, 0)) // X
        gameController.addMove(Coordinate(1, 0)) // O
        gameController.addMove(Coordinate(0, 1)) // X
        gameController.addMove(Coordinate(1, 1)) // O
        gameController.addMove(Coordinate(0, 2)) // X wins

        val winState = emissions.last() as? GameStatus.Won

        assertNotNull(winState)

        assertEquals(3, winState?.winningCells?.size)
        assertTrue(winState?.winningCells?.all { it.col == 0 } == true)

        job.cancel()
    }


    // ========== Draw Detection Tests ==========

    @Test
    fun `Detect draw`() = runTest {

        val emissions = mutableListOf<GameStatus>()

        val job = launch {
            gameController.gameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        gameController.addMove(Coordinate(0, 0)) // X
        gameController.addMove(Coordinate(0, 1)) // O
        gameController.addMove(Coordinate(0, 2)) // X
        gameController.addMove(Coordinate(1, 2)) // O
        gameController.addMove(Coordinate(1, 0)) // X
        gameController.addMove(Coordinate(1, 1)) // O
        gameController.addMove(Coordinate(2, 1)) // X
        gameController.addMove(Coordinate(2, 0)) // O
        gameController.addMove(Coordinate(2, 2)) // X -> Draw

        assertTrue(emissions.last() is GameStatus.Draw)

        job.cancel()

    }

    @Test
    fun `Player toggles correctly after each move`() = runTest {

        assertEquals(player1, gameController.getCurPlayer())

        gameController.addMove(Coordinate(0, 0))

        assertEquals(player2, gameController.getCurPlayer())

        gameController.addMove(Coordinate(0, 1))

        assertEquals(player1, gameController.getCurPlayer())
    }

    @Test
    fun `Cannot add move after game is over`() = runTest {

        gameController.addMove(Coordinate(0, 0)) // X
        gameController.addMove(Coordinate(1, 0)) // O
        gameController.addMove(Coordinate(0, 1)) // X
        gameController.addMove(Coordinate(1, 1)) // O
        gameController.addMove(Coordinate(0, 2)) // X wins

        val exception = assertFailsWith<IllegalStateException> {
            gameController.addMove(Coordinate(2, 2))
        }

        assertEquals("Game already finished", exception.message)
    }

}
