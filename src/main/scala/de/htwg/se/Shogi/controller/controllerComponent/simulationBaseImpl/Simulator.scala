package de.htwg.se.Shogi.controller.controllerComponent.simulationBaseImpl

import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import akka.actor.{Actor}

class Simulator extends Actor{
  import Simulator._

  val waitTime = 1000
  def receive:Receive = {
    case Simulate(controller, waitTime) => simulate(controller, waitTime)
                     sender ! Done
    case _        => sender ! Failed
  }

  def simulate(controller: ControllerInterface, waitTime: Int): Unit = {
    controller.movePiece((6, 2), (6, 3))
    Thread.sleep(waitTime)
    controller.movePiece((7, 6), (7, 5))
    Thread.sleep(waitTime)
    controller.movePiece((1, 2), (1, 3))
    Thread.sleep(waitTime)
    controller.movePiece((3, 8), (2, 7))
    Thread.sleep(waitTime)
    controller.movePiece((5, 0), (6, 1))
    Thread.sleep(waitTime)
    controller.movePiece((7, 5), (7, 4))
    Thread.sleep(waitTime)
    controller.movePiece((7, 1), (6, 2))
    Thread.sleep(waitTime)
    controller.movePiece((2, 6), (2, 5))
    Thread.sleep(waitTime)
    controller.movePiece((6, 0), (7, 1))
    Thread.sleep(waitTime)
    controller.movePiece((1, 7), (6, 2))
    controller.promotePiece((6, 2))
  }
}


object Simulator {
  case class Simulate(controller: ControllerInterface, waitTime: Int)
  case object Done
  case object Failed
}

