package com.voiddeveloper.tictactoe.domain.ai

import com.voiddeveloper.tictactoe.domain.SimpleGameBlankCell
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.model.PlayerType
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.random.Random
import kotlin.random.nextInt

@Serializable
class SimpleGameAi(
    override var gameCells: List<List<Cell>>,
    var difficulty: GamePlayDifficulty,
) : GameAI {


    private val gameSize = gameCells.first().size

    private val rotatedGameCells = gameCells.rotated()

    private val diagonals = gameCells.diagonals()

    @Transient
    private val random = Random(System.nanoTime())

    private var totalCells = listOf<List<Cell>>()

    init {
        totalCells = buildList {
            addAll(gameCells)
            addAll(rotatedGameCells)
            addAll(diagonals.toList())
        }
    }

    override fun play(): Cell {
        val cell = when (difficulty) {
            GamePlayDifficulty.EASY -> easyPlay()
            GamePlayDifficulty.MEDIUM -> mediumPlay()
            GamePlayDifficulty.HARD -> hardPlay()
        }
        if (cell.player != null) throw IllegalArgumentException("Cell already has a player!")
        else return cell
    }

    private fun Cell?.getReadyToPlayCell(): Cell? {
        return if (this == null || this.player != null) null else this
    }

    private fun mediumPlay(): Cell {
        return when (random.nextInt(0..1)) {
            0 -> easyPlay()
            else -> hardPlay()
        }
    }

    //mitigate possible stack overflow
    private fun easyPlay(): Cell {
        val cells = gameCells.flatten()
        return cells.getOrNull(random.nextInt(cells.indices)).getReadyToPlayCell() ?: easyPlay()
    }

    private fun hardPlay(): Cell {
        return (winOrWinBlockPlay() ?: forkOrForkBlockPlayHandler() ?: centerPlay()
        ?: cornerPlay()).getReadyToPlayCell() ?: easyPlay()
    }

    private fun cornerPlay(): Cell? {
        val corners = playableCorners()
        if (corners.isEmpty()) return null
        val cell = when {
            gameCells.firstOrNull()
                ?.firstOrNull()?.player?.type == PlayerType.HUMAN -> gameCells.lastOrNull()
                ?.lastOrNull()

            gameCells.firstOrNull()
                ?.lastOrNull()?.player?.type == PlayerType.HUMAN -> gameCells.lastOrNull()
                ?.firstOrNull()

            gameCells.lastOrNull()
                ?.lastOrNull()?.player?.type == PlayerType.HUMAN -> gameCells.firstOrNull()
                ?.firstOrNull()

            gameCells.lastOrNull()
                ?.firstOrNull()?.player?.type == PlayerType.HUMAN -> gameCells.firstOrNull()
                ?.lastOrNull()

            else -> corners.getOrNull(random.nextInt(corners.indices))
        } ?: return null
        return if (cell.player == null) cell
        else corners.getOrNull(random.nextInt(corners.indices))
    }

    private fun playableCorners() = buildList {
        add(gameCells.firstOrNull()?.firstOrNull())
        add(gameCells.firstOrNull()?.lastOrNull())
        add(gameCells.lastOrNull()?.lastOrNull())
        add(gameCells.lastOrNull()?.firstOrNull())
    }.filter { it?.player == null }

    private fun centerPlay(): Cell? {
        val center = gameSize / 2
        return if (gameSize % 2 != 0) {
            val cell = gameCells.getOrNull(center)?.getOrNull(center) ?: return null
            if (cell.player == null) cell
            else null
        } else null
    }

    private fun winOrWinBlockPlay(): Cell? {
        for (i in totalCells.indices) {
            val cells = totalCells.getOrNull(i) ?: return null
            val cell = winOrWinBlockSingleLineScanner(cells)
            if (cell != null) return cell
        }
        return null
    }

    private fun forkOrForkBlockPlayHandler(): Cell? {
        var cell = forkOrForkBlockPlay(gameCells, rotatedGameCells)
        if (cell != null) return cell
        cell = forkOrForkBlockPlay(gameCells, diagonals.toList())
        if (cell != null) return cell
        cell = forkOrForkBlockPlay(rotatedGameCells, diagonals.toList())
        if (cell != null) return cell
        return null
    }

    private fun forkOrForkBlockPlay(
        gameCells: List<List<Cell>>,
        compareGameCells: List<List<Cell>>,
    ): Cell? {
        return forkPlay(gameCells, compareGameCells) ?: forkBlockPlay(gameCells, compareGameCells)
    }

    private fun forkPlay(
        gameCells: List<List<Cell>>,
        compareGameCells: List<List<Cell>>,
    ) = forkOrForkBlockPlayScanner(gameCells, compareGameCells, false)

    private fun forkBlockPlay(
        gameCells: List<List<Cell>>,
        compareGameCells: List<List<Cell>>,
    ) = forkOrForkBlockPlayScanner(gameCells, compareGameCells, true)

    private fun forkOrForkBlockPlayScanner(
        gameCells: List<List<Cell>>,
        compareGameCells: List<List<Cell>>,
        isBlocking: Boolean,
    ): Cell? {
        val playerType = if (isBlocking) PlayerType.HUMAN else PlayerType.COMPUTER

        for (i in gameCells.indices) {
            val cells = gameCells.getOrNull(i) ?: continue
            if (!cells.isSuspectToFork(playerType)) continue

            for (j in compareGameCells.indices) {
                val intersectingCells = compareGameCells.getOrNull(j) ?: continue
                if (!intersectingCells.isSuspectToFork(playerType)) continue

                if (cells.find { it.player != null }?.player == intersectingCells.find { it.player != null }?.player) {

                    if (isBlocking) {
                        val forceBlockCell = findCellForOpponentForceBlock()
                        if (forceBlockCell != null) return forceBlockCell
                    }

                    val cell = cells.filter { it.player == null }
                        .intersect(intersectingCells.filter { it.player == null }.toSet())
                        .firstOrNull()

                    if (cell != null && cell.player == null) return cell
                }
            }
        }
        return null
    }

    private fun findCellForOpponentForceBlock(): Cell? {
        val cells = buildList {
            addAll(gameCells)
            addAll(rotatedGameCells)
            addAll(diagonals.toList())
        }
        for (i in cells.indices) {
            val line = gameCells.getOrNull(i) ?: return null
            val cell = findCellForWinOrWinBlock(
                line, PlayerType.COMPUTER, SimpleGameBlankCell.Fork.count
            )
            if (cell != null) return cell
        }
        return null
    }

    private fun List<Cell>.isSuspectToFork(
        relevantOwnerType: PlayerType,
    ): Boolean {
        if (isEmpty()) return false
        val owned = filter { it.player != null }
        return owned.size == 1 && owned.first().player?.type == relevantOwnerType
    }

    private fun winOrWinBlockSingleLineScanner(
        cells: List<Cell>,
    ): Cell? {
        var cell = findCellForWin(cells)
        if (cell != null) return cell
        cell = findCellForWinBlock(cells)
        if (cell != null) return cell
        return null
    }

    private fun findCellForWin(
        cells: List<Cell>,
    ) = findCellForWinOrWinBlock(cells, PlayerType.COMPUTER)

    private fun findCellForWinBlock(
        cells: List<Cell>,
    ) = findCellForWinOrWinBlock(cells, PlayerType.HUMAN)

    private fun findCellForWinOrWinBlock(
        cells: List<Cell>,
        playerType: PlayerType,
        blankCells: Int = SimpleGameBlankCell.Win.count,
    ): Cell? {
        val (vacant, owned) = cells.partition { it.player == null }
        if (vacant.size > blankCells) return null
        if (owned.any { it.player?.type != playerType }) return null
        return vacant.firstOrNull()
    }

    override fun newInstance(newBoard: List<List<Cell>>): SimpleGameAi {
        return SimpleGameAi(
            gameCells = newBoard,
            difficulty = difficulty
        )
    }

}

fun <T> List<List<T>>.rotated(): List<List<T>> {
    val rotated = mutableListOf<List<T>>()

    for (j in this.indices) {
        val newRow = mutableListOf<T>()
        for (i in this.indices) newRow.add(this[i][j])
        rotated.add(newRow.reversed())
    }

    return rotated
}

fun <T> List<List<T>>.diagonals(): Pair<List<T>, List<T>> {
    return this.diagonal() to this.rotated().diagonal()
}

private fun <T> List<List<T>>.diagonal(): List<T> {
    val diagonal = mutableListOf<T>()
    for (i in this.indices) diagonal.add(this[i][i])
    return diagonal
}
