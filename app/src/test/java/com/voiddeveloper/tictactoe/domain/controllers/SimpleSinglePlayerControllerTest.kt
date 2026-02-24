package com.voiddeveloper.tictactoe.domain.controllers


import com.voiddeveloper.tictactoe.domain.ai.GameAI
import com.voiddeveloper.tictactoe.domain.ai.SimpleGameAi
import com.voiddeveloper.tictactoe.model.Board.Companion.emptyBoard
import com.voiddeveloper.tictactoe.model.Coin
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.model.LocalGameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.PlayerType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SimpleSinglePlayerControllerTest {

    private val player1 = Player(coin = Coin.X, type = PlayerType.HUMAN ,  playerName = "daf")
    private val player2 = Player(coin = Coin.O, type = PlayerType.COMPUTER ,  playerName = "adfa")

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var simpleGameAi: GameAI
    private lateinit var simpleSinglePlayerGameController: SinglePlayerLocal

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        simpleGameAi = SimpleGameAi(
            gameCells = emptyBoard,
            difficulty = GamePlayDifficulty.HARD
        )
        simpleSinglePlayerGameController = SinglePlayerLocal(
            gameAI = simpleGameAi,
            playerDetails = PlayerDetails(listOf(player1, player2)),
            coroutineScope = CoroutineScope(testDispatcher)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `human move should emit InProgress, AiThinking, InProgress`() = runTest {

        val emissions: MutableList<LocalGameStatus> = mutableListOf()

        val job = launch {
            simpleSinglePlayerGameController.localGameStatus.collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()

        simpleSinglePlayerGameController.addMove(Coordinate(0, 0))

        advanceUntilIdle()
        println(emissions)

        assertEquals(3, emissions.size)
        assertTrue(emissions[0] is LocalGameStatus.InProgress)
        assertTrue(emissions[1] is SinglePlayerLocal.AiThinking)
        assertTrue(emissions[2] is LocalGameStatus.InProgress)

        job.cancel()
    }

    // AI Wins
    @Test
    fun `ai winning move should emit Won`() = runTest {

        val emissions = mutableListOf<LocalGameStatus>()

        val job = launch {
            simpleSinglePlayerGameController.localGameStatus.collect {
                emissions.add(it)
            }
        }


        println(simpleSinglePlayerGameController.getPrintableBoard())
        simpleSinglePlayerGameController.addMove(Coordinate(0, 1))

        println(simpleSinglePlayerGameController.getPrintableBoard())
        simpleSinglePlayerGameController.addMove(Coordinate(0, 0))

        println(simpleSinglePlayerGameController.getPrintableBoard())
        simpleSinglePlayerGameController.addMove(Coordinate(2, 2))
        println(simpleSinglePlayerGameController.getPrintableBoard())


        val last = emissions.last()
        advanceUntilIdle()

        assertTrue(last is LocalGameStatus.Won)
        assertEquals(player2, (last as LocalGameStatus.Won).winner)

        job.cancel()
    }


    // Game Draws
    @Test
    fun `game should emit Draw`() = runTest {

        val emissions = mutableListOf<LocalGameStatus>()

        val job = launch {
            simpleSinglePlayerGameController.localGameStatus.collect {
                emissions.add(it)
            }
        }

        // Human 1
        simpleSinglePlayerGameController.addMove(Coordinate(0, 0))

        // Human 2
        simpleSinglePlayerGameController.addMove(Coordinate(0, 2))

        // Human 3
        simpleSinglePlayerGameController.addMove(Coordinate(2, 1))

        // Human 4
        simpleSinglePlayerGameController.addMove(Coordinate(1, 2))

        // Human 5 (final move)
        simpleSinglePlayerGameController.addMove(Coordinate(2, 0))

        advanceUntilIdle()

        val last = emissions.last()

        assertTrue(last is LocalGameStatus.Draw)

        job.cancel()

    }


}