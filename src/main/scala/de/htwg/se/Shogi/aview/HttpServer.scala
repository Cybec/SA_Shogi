package de.htwg.se.Shogi.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import akka.stream.ActorMaterializer
import de.htwg.se.Shogi.controller.controllerComponent._

class HttpServer(controller: ControllerInterface, tui: Tui) {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val port = 8080
  val route: Route =
    pathPrefix("shogi") {
      path("empty") {
        get {
          controller.createEmptyBoard()
          boardToHtml
        }
      } ~
        path("new") {
          get {
            controller.createNewBoard()
            boardToHtml
          }
        } ~
        path("undo") {
          get {
            controller.undoCommand
            boardToHtml
          }
        } ~
        path("redo") {
          get {
            controller.redoCommand
            boardToHtml
          }
        } ~
        path("pmv" / Segment) {
          command =>
            {
              get {
                val list = controller.getPossibleMoves(command.charAt(0).asDigit, command.charAt(1).asDigit)
                movesToHtml(list)
              }
            }
        } ~
        path("mv" / Segment / Segment) {
          (current, dest) =>
            {
              put {
                controller.movePiece((current.charAt(0).asDigit, current.charAt(1).asDigit), (dest.charAt(0).asDigit, dest.charAt(1).asDigit)) match {
                  case MoveResult.invalidMove => invalidMoveToHtml
                  case MoveResult.validMove => {
                    if (controller.promotable((dest.charAt(0).asDigit, dest.charAt(1).asDigit))) {
                      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Do you want to promote your piece at destination " + dest + "? /y for yes!<br>" + controller.boardToHtml))
                    } else {
                      boardToHtml
                    }
                  }
                  case MoveResult.kingSlain => redirect("winner", StatusCodes.PermanentRedirect)
                }
              }
            }
        } ~
        path("mv" / Segment / Segment / "y") {
          (_, dest) =>
            {
              put {
                //TODO: Check if promotable only after move
                controller.promotePiece((dest.charAt(0).asDigit, dest.charAt(1).asDigit))
                boardToHtml
              }
            }
        } ~
        path("mv" / Segment / "shogi" / "winner") { _ =>
          {
            get {
              //TODO: Check if King is actually slain
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>YOU WON!!!!!!!!!!</h1>" + controller.boardToHtml))
            }
          }
        } ~
        path("pmvcp" / Segment) {
          conqueredPiece =>
            {
              get {
                val list = controller.getPossibleMovesConqueredPiece(conqueredPiece)
                movesToHtml(list)
              }
            }
        } ~
        path("mvcp" / Segment / Segment) {
          (conqueredPiece, dest) =>
            {
              put {
                controller.moveConqueredPiece(conqueredPiece, (dest.charAt(0).asDigit, dest.charAt(1).asDigit)) match {
                  case true => boardToHtml
                  case false => {
                    invalidMoveToHtml
                  }
                }
              }
            }
        } ~
        pathEndOrSingleSlash {
          get {
            boardToHtml
          }
        }
    }
  /* For testing Exception handling in Akka Http
path("divide" / IntNumber / IntNumber) { (a, b) =>
  handleExceptions(myExceptionHandler) {
    complete(s"The result is ${a / b}")
  }
}*/

  def movesToHtml(moveList: List[(Int, Int)]): StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Shogi</h1>" + tui.possibleMovesAsHtml(moveList) + controller.boardToHtml))
  }
  def boardToHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Shogi</h1>" + controller.boardToHtml))
  }
  def invalidMoveToHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>This Move is not valid</h1>" + controller.boardToHtml))
  }
  val bindingFuture = Http().bindAndHandle(route, "localhost", port)

  def unbind: Any = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  /* Exception handler in Akka Http
  val myExceptionHandler = ExceptionHandler {
    case _: Exception =>
        redirect("/shogi", StatusCodes.PermanentRedirect)
      }*/
}
