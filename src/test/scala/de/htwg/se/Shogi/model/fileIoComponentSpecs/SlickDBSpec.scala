package de.htwg.se.Shogi.model.fileIoComponentSpecs

import de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl
import net.codingwell.scalaguice.InjectorExtensions._
import com.google.inject.name.Names
import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.MoveResult
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl.{ PieceFactory, PiecesEnum }
import de.htwg.se.Shogi.model.playerComponent.Player
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class SlickDBSpec extends WordSpec with Matchers {
  "A SlickDB" when {
    val injector: Injector = Guice.createInjector(new ShogiModule)
    val controller: Controller = new Controller()
    val player_1: Player = Player("Player1", first = true)
    val player_2: Player = Player("Player2", first = false)
    val smallBoard: BoardInterface = injector.instance[BoardInterface](Names.named("small")).createNewBoard()
    val tinyBoard: BoardInterface = injector.instance[BoardInterface](Names.named("tiny")).createNewBoard()

    val fileIo: DAOInterface = new slickDBImpl.SlickDB
    "called save and load" should {
      "reload an board(normal) with in the state it was saved" in {
        controller.createNewBoard()
        val currentPlayerIsFirst = true
        fileIo.save(controller.board, currentPlayerIsFirst, player_1, player_2)
        controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
        val (board: BoardInterface, state: Boolean, p1: Player, p2: Player) = fileIo.load.getOrElse(controller.createEmptyBoard())
        p1.name shouldEqual player_1.name
        p1.first shouldBe player_1.first
        p2.name shouldEqual player_2.name
        p2.first shouldBe player_2.first

        controller.replaceBoard(board)
        controller.currentState = if (state) {
          controller.playerOnesTurn
        } else {
          controller.playerTwosTurn
        }
        controller.boardToString() should be(
          "Captured: \n" +
            "    0     1     2     3     4     5     6     7     8 \n \n" +
            "---------------------------------------------------------\n " +
            "| L°  | KN° | SG° | GG° | K°  | GG° | SG° | KN° | L°  | \ta\n" +
            "---------------------------------------------------------\n " +
            "|     | R°  |     |     |     |     |     | B°  |     | \tb\n" +
            "---------------------------------------------------------\n " +
            "| P°  | P°  | P°  | P°  | P°  | P°  | P°  | P°  | P°  | \tc\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     |     |     |     | \td\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     |     |     |     | \te\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     |     |     |     | \tf\n" +
            "---------------------------------------------------------\n " +
            "| P   | P   | P   | P   | P   | P   | P   | P   | P   | \tg\n" +
            "---------------------------------------------------------\n " +
            "|     | B   |     |     |     |     |     | R   |     | \th\n" +
            "---------------------------------------------------------\n " +
            "| L   | KN  | SG  | GG  | K   | GG  | SG  | KN  | L   | \ti\n" +
            "---------------------------------------------------------\n" +
            "Captured: \n"
        )
        controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
        controller.movePiece((0, 6), (0, 5)) should be(MoveResult.validMove)
        controller.movePiece((0, 3), (0, 4)) should be(MoveResult.validMove)
        controller.movePiece((0, 5), (0, 4)) should be(MoveResult.validMove)
        controller.movePiece((0, 0), (0, 4)) should be(MoveResult.validMove)
        val currentState: Boolean = if (controller.currentState == controller.playerOnesTurn) {
          true
        } else {
          false
        }
        fileIo.save(controller.board, currentState, player_1, player_2)
        controller.movePiece((8, 6), (8, 5)) should be(MoveResult.validMove)
        val (board2: BoardInterface, state2: Boolean, p12: Player, p22: Player) = fileIo.load.getOrElse(controller.createEmptyBoard())
        p12.name shouldEqual player_1.name
        p12.first shouldBe player_1.first
        p22.name shouldEqual player_2.name
        p22.first shouldBe player_2.first

        controller.replaceBoard(board2)
        controller.currentState = if (state2) {
          controller.playerOnesTurn
        } else {
          controller.playerTwosTurn
        }
        println(controller.boardToString())
        controller.boardToString() should be(
          "Captured: P°    \n" +
            "    0     1     2     3     4     5     6     7     8 \n \n" +
            "---------------------------------------------------------\n " +
            "|     | KN° | SG° | GG° | K°  | GG° | SG° | KN° | L°  | \ta\n" +
            "---------------------------------------------------------\n " +
            "|     | R°  |     |     |     |     |     | B°  |     | \tb\n" +
            "---------------------------------------------------------\n " +
            "|     | P°  | P°  | P°  | P°  | P°  | P°  | P°  | P°  | \tc\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     |     |     |     | \td\n" +
            "---------------------------------------------------------\n " +
            "| L°  |     |     |     |     |     |     |     |     | \te\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     |     |     |     | \tf\n" +
            "---------------------------------------------------------\n " +
            "|     | P   | P   | P   | P   | P   | P   | P   | P   | \tg\n" +
            "---------------------------------------------------------\n " +
            "|     | B   |     |     |     |     |     | R   |     | \th\n" +
            "---------------------------------------------------------\n " +
            "| L   | KN  | SG  | GG  | K   | GG  | SG  | KN  | L   | \ti\n" +
            "---------------------------------------------------------\n" +
            "Captured: P     \n"
        )
      }
    }
  }
}