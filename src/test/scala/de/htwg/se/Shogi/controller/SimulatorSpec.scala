package de.htwg.se.Shogi.controller

import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.Shogi.controller.controllerComponent.simulationBaseImpl.Simulator
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl.{PieceFactory, PiecesEnum}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

//noinspection ScalaStyle
@RunWith(classOf[JUnitRunner])
class SimulatorSpec extends WordSpec with Matchers {
  "The Sumulator" when {
    "Called Start" should {
      val controller = new Controller()
      controller.createNewBoard()

      controller.startSimulation

      "Change Board" in {

        controller.boardToString() should be(
        "Captured: \n" +
          "    0     1     2     3     4     5     6     7     8 \n \n" +
          "---------------------------------------------------------\n " +
          "| L°  | KN° | SG° | GG° | K°  |     |     | KN° | L°  | \ta\n" +
          "---------------------------------------------------------\n " +
          "|     | R°  |     |     |     |     | GG° | SG° |     | \tb\n" +
          "---------------------------------------------------------\n " +
          "| P°  |     | P°  | P°  | P°  | P°  | PB  | P°  | P°  | \tc\n" +
          "---------------------------------------------------------\n " +
          "|     | P°  |     |     |     |     | P°  |     |     | \td\n" +
          "---------------------------------------------------------\n " +
          "|     |     |     |     |     |     |     | P   |     | \te\n" +
          "---------------------------------------------------------\n " +
          "|     |     | P   |     |     |     |     |     |     | \tf\n" +
          "---------------------------------------------------------\n " +
          "| P   | P   |     | P   | P   | P   | P   |     | P   | \tg\n" +
          "---------------------------------------------------------\n " +
          "|     |     | GG  |     |     |     |     | R   |     | \th\n" +
          "---------------------------------------------------------\n " +
          "| L   | KN  | SG  |     | K   | GG  | SG  | KN  | L   | \ti\n" +
          "---------------------------------------------------------\n" +
          "Captured: B     \n"
      )

      }
    }
  }
}
