package de.htwg.se.Shogi.model.fileIoComponentSpecs

import com.google.inject.name.Names
import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.MoveResult
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.fileIoComponent.fileIoXmlImpl
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl.{ PieceFactory, PiecesEnum }
import de.htwg.se.Shogi.model.playerComponent.Player
import net.codingwell.scalaguice.InjectorExtensions._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class XmlFileIOSpec extends WordSpec with Matchers {
  "A XmlFileIO" when {
    val injector: Injector = Guice.createInjector(new ShogiModule)
    val controller: Controller = new Controller()
    val player_1: Player = Player("Player1", first = true)
    val player_2: Player = Player("Player2", first = false)
    val smallBoard: BoardInterface = injector.instance[BoardInterface](Names.named("small")).createNewBoard()
    val tinyBoard: BoardInterface = injector.instance[BoardInterface](Names.named("tiny")).createNewBoard()

    val fileIo: DAOInterface = new fileIoXmlImpl.FileIO

    "called save and load" should {
      "reload an board(normal) with in the state it was saved" in {
        controller.createNewBoard()
        val currentPlayerIsFirst = true
        fileIo.save(controller.board, currentPlayerIsFirst, player_1, player_2)
        controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
        val result = fileIo.load.get
        controller.board = result._1
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
      }
      "reload an board(small) with in the state it was saved" in {
        val currentPlayerIsFirst = true
        fileIo.save(smallBoard, currentPlayerIsFirst, player_1, player_2)
        smallBoard.replaceCell(0, 2, PieceFactory.apply(PiecesEnum.King, player_1.first))
        val (board, state, player1: Player, player2: Player) = fileIo.load.getOrElse(controller.createEmptyBoard())
        state shouldBe currentPlayerIsFirst
        player1.name shouldEqual player_1.name
        player1.first shouldBe player_1.first
        player2.name shouldEqual player_2.name
        player2.first shouldBe player_2.first

        board.toString() should be(
          "Captured: \n" +
            "    0     1     2     3     4     5     6     7     8 \n \n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     | \ta\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     | \tb\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     | \tc\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     | \td\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     | \te\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     |     |     |     | \tf\n" +
            "---------------------------------------------------------\n " +
            "| \tg\n" +
            "---------------------------------------------------------\n " +
            "| \th\n" +
            "---------------------------------------------------------\n " +
            "| \ti\n" +
            "---------------------------------------------------------\n" +
            "Captured: \n"
        )
      }

      "reload an board(tiny) with the state it was saved" in {
        val currentPlayerIsFirst = true
        fileIo.save(tinyBoard, currentPlayerIsFirst, player_1, player_2)
        tinyBoard.replaceCell(0, 2, PieceFactory.apply(PiecesEnum.King, player_1.first))
        val (board, state, player1: Player, player2: Player) = fileIo.load.getOrElse(controller.createEmptyBoard())
        state shouldBe currentPlayerIsFirst
        player1.name shouldEqual player_1.name
        player1.first shouldBe player_1.first
        player2.name shouldEqual player_2.name
        player2.first shouldBe player_2.first

        board.toString() should be(
          "Captured: \n" +
            "    0     1     2     3     4     5     6     7     8 \n \n" +
            "---------------------------------------------------------\n " +
            "|     |     |     | \ta\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     | \tb\n" +
            "---------------------------------------------------------\n " +
            "|     |     |     | \tc\n" +
            "---------------------------------------------------------\n " +
            "| \td\n" +
            "---------------------------------------------------------\n " +
            "| \te\n" +
            "---------------------------------------------------------\n " +
            "| \tf\n" +
            "---------------------------------------------------------\n " +
            "| \tg\n" +
            "---------------------------------------------------------\n " +
            "| \th\n" +
            "---------------------------------------------------------\n " +
            "| \ti\n" +
            "---------------------------------------------------------\n" +
            "Captured: \n"
        )
      }

      "getBoardBySize will return None if no default board size is given" in {
        val unrealisticBoardSize = 60
        val board: BoardInterface = new Board(unrealisticBoardSize, PieceFactory.apply(PiecesEnum.EmptyPiece, player_1.first))
        val currentPlayerIsFirst = true
        fileIo.save(board, currentPlayerIsFirst, player_1, player_2)
        fileIo.load should be(None)
      }
    }
  }
}
