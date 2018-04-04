package de.htwg.se.Shogi.util

import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.ShogiModule
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
      "have the last done move on the redo Step" in {

        controller.createNewBoard()
        controller.movePiece((0, 2), (0, 3)) should be(MoveResult.validMove)
        //  undoManager.saveStep()
        //  undoManager.saveStep()
      }
    }
  }
}
