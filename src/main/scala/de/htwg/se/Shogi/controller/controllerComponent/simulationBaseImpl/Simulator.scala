package de.htwg.se.Shogi.controller.controllerComponent.simulationBaseImpl

import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import akka.actor.{ Actor }

class Simulator extends Actor {

  import Simulator._

  def receive: Receive = {
    case Simulate(controller) => {
      simulate(controller)
      sender ! Done
    }
    case _ => sender ! Failed
  }

  def simulate(controller: ControllerInterface): Unit = {
    controller.movePiece((6, 2), (6, 3))
    controller.movePiece((7, 6), (7, 5))

    controller.movePiece((1, 2), (1, 3))
    controller.movePiece((3, 8), (2, 7))
    controller.movePiece((5, 0), (6, 1))
    controller.movePiece((7, 5), (7, 4))
    controller.movePiece((7, 1), (6, 2))
    controller.movePiece((2, 6), (2, 5))
    controller.movePiece((6, 0), (7, 1))
    controller.movePiece((1, 7), (6, 2))
    controller.promotePiece((6, 2))
  }
}

object Simulator {

  case class Simulate(controller: ControllerInterface)

  case object Done

  case object Failed

}

