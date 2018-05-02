package de.htwg.se.Shogi.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.se.Shogi.controller.controllerComponent.{ControllerInterface, MoveResult}

class HttpServer(controller: ControllerInterface, tui: Tui) {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val port = 8080
  val route: Route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Shoshogi</h1>"))
        }
      } ~
      path("shogi") {
        get {
          boardToHtml
        }
      } ~
        path("shogi" / "empty") {
          get {
            controller.createEmptyBoard()
            boardToHtml
          }
        } ~
        path("shogi" / "new") {
          get {
            controller.createNewBoard()
            boardToHtml
          }
        } ~
        path("shogi" / "undo") {
          get {
            controller.undoCommand
            boardToHtml
          }
        } ~
        path("shogi" / "redo") {
          get {
            controller.redoCommand
            boardToHtml
          }
        } ~
    path("shogi" / "pmv" / Segment) { command => {
      get {
        val list = controller.getPossibleMoves(command.charAt(0).asDigit, command.charAt(1).asDigit)
        pvmToHtml(list)
      }
    }
    } ~
      path("shogi" / "mv" / Segment / Segment ) { (current, dest) => {
        get {
        controller.movePiece((current.charAt(0).asDigit, current.charAt(1).asDigit), (dest.charAt(0).asDigit, dest.charAt(1).asDigit)) match {
          //case MoveResult.invalidMove => Exception einbauen
          case MoveResult.validMove => boardToHtml
          case MoveResult.kingSlain => redirect("/winner/", StatusCodes.PermanentRedirect)
        }
        }
      }
      }

        /* For testing Exception handling in Akka Http
        path("divide" / IntNumber / IntNumber) { (a, b) =>
          handleExceptions(myExceptionHandler) {
            complete(s"The result is ${a / b}")
          }
        }*/

    path("winner") {
      get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>YOU WON!!!!!!!!!!</h1>"))
    }
  }

  def pvmToHtml(moveList: List[(Int, Int)]): StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Shogi</h1>" + tui.possibleMovesAsHtml(moveList)))
  }
  def boardToHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Shogi</h1>" + controller.boardToHtml))
  }
  val bindingFuture = Http().bindAndHandle(route, "localhost", port)

  def unbind: Any = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  def processInputLine(input: String): Unit = {
    tui.processInputLine(input)
  }

  /* Exception handler in Akka Http
  val myExceptionHandler = ExceptionHandler {
    case _: ArithmeticException =>
        complete((StatusCodes.BadRequest, "NOPNOPNOPNOP"))
      }*/
}
