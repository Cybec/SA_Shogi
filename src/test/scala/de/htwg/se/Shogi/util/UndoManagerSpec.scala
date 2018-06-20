package de.htwg.se.Shogi.util

import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.SolveCommand
import de.htwg.se.Shogi.controller.controllerComponent.{ ControllerInterface, MoveResult }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class UndoManagerSpec extends WordSpec with Matchers {
  "An UndoManager" when {
    val undoManager = new UndoManager
    val injector: Injector = Guice.createInjector(new ShogiModule)
    val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
    "called undoStep" should {
      "return board to state before the last made move" in {

        controller.createNewBoard()
        controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
        undoManager.saveStep(new SolveCommand(controller))

        controller.boardToString() should be(
          "Captured: \n" +
            "    0     1     2     3     4     5     6     7     8 \n \n" +
            "---------------------------------------------------------\n " +
            "| L°  | KN° | SG° | GG° | K°  | GG° | SG° | KN° | L°  | \ta\n" +
            "---------------------------------------------------------\n " +
            "|     | R°  |     |     |     |     |     | B°  |     | \tb\n" +
            "---------------------------------------------------------\n " +
            "|     | P°  | P°  | P°  | P°  | P°  | P°  | P°  | P°  | \tc\n" +
            "---------------------------------------------------------\n " +
            "| P°  |     |     |     |     |     |     |     |     | \td\n" +
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
        controller.undoCommand
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
    }
    "called redoStep" should {
      "return state of board to the state before last undoStep" in {
        controller.createNewBoard()
        controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
        undoManager.saveStep(new SolveCommand(controller))

        controller.undoCommand
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
        controller.redoCommand
        controller.boardToString() should be(
          "Captured: \n" +
            "    0     1     2     3     4     5     6     7     8 \n \n" +
            "---------------------------------------------------------\n " +
            "| L°  | KN° | SG° | GG° | K°  | GG° | SG° | KN° | L°  | \ta\n" +
            "---------------------------------------------------------\n " +
            "|     | R°  |     |     |     |     |     | B°  |     | \tb\n" +
            "---------------------------------------------------------\n " +
            "|     | P°  | P°  | P°  | P°  | P°  | P°  | P°  | P°  | \tc\n" +
            "---------------------------------------------------------\n " +
            "| P°  |     |     |     |     |     |     |     |     | \td\n" +
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
    }
    "called clear" should {
      controller.createNewBoard()
      controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
      undoManager.saveStep(new SolveCommand(controller))
      "be true" in {
        undoManager.clear() should be(true)
      }
    }
  }
}
