package de.htwg.se.Shogi.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, PathMatchers, Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface

class HttpServer(controller: ControllerInterface, tui: Tui) {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val port = 8080
  val route: Route = get {
      path("hello") {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Shoshogi</h1>"))
      } ~
      path("shogi") {
        boardToHtml
      } ~
        path("shogi" / "empty") {
          controller.createEmptyBoard()
          boardToHtml
        } ~
        path("shogi" / "new") {
          controller.createNewBoard()
          boardToHtml
        } ~
        path("shogi" / "undo") {
          controller.undoCommand
          boardToHtml
        } ~
        path("shogi" / "redo") {
          controller.redoCommand
          boardToHtml
        } ~
        path("shogi" / Segment) { command => {
            processInputLine(command)
            boardToHtml
          }
        }
        /* For testing Exception handling in Akka Http
        path("divide" / IntNumber / IntNumber) { (a, b) =>
          handleExceptions(myExceptionHandler) {
            complete(s"The result is ${a / b}")
          }
        }*/
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
