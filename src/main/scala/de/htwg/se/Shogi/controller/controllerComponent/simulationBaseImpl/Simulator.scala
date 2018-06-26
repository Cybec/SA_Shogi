package de.htwg.se.Shogi.controller.controllerComponent.simulationBaseImpl

import de.htwg.se.Shogi.controller.controllerComponent.{ ControllerInterface, MoveResult }
import akka.actor.Actor

class Simulator extends Actor {

  import Simulator._

  def receive: Receive = {
    case Simulate(controller) => {
      simulate(controller) match {
        case true => sender ! Done
        case false => sender ! Failed
      }
    }
    case _ => sender ! Failed
  }

  def simulate(controller: ControllerInterface): Boolean = {
    controller.movePiece((6, 2), (6, 3)) == MoveResult.validMove &&
      controller.movePiece((7, 6), (7, 5)) == MoveResult.validMove &&
      controller.movePiece((1, 2), (1, 3)) == MoveResult.validMove &&
      controller.movePiece((3, 8), (2, 7)) == MoveResult.validMove &&
      controller.movePiece((5, 0), (6, 1)) == MoveResult.validMove &&
      controller.movePiece((7, 5), (7, 4)) == MoveResult.validMove &&
      controller.movePiece((7, 1), (6, 2)) == MoveResult.validMove &&
      controller.movePiece((2, 6), (2, 5)) == MoveResult.validMove &&
      controller.movePiece((6, 0), (7, 1)) == MoveResult.validMove &&
      controller.movePiece((1, 7), (6, 2)) == MoveResult.validMove &&
      controller.promotePiece((6, 2))
  }
}

object Simulator {

  case class Simulate(controller: ControllerInterface)

  case object Done

  case object Failed

}

