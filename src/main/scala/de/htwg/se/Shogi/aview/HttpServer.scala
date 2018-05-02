package de.htwg.se.Shogi.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import akka.stream.ActorMaterializer
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface

import scala.concurrent.{ ExecutionContextExecutor, Future }

class HttpServer(controller: ControllerInterface) {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val port = 8080
  val route: Route = get {
    path("hello") {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Shoshogi</h1>"))
    }
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
      }
  }

  def boardToHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Shogi</h1>" + controller.boardToHtml))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", port)

  def unbind: Any = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  //TODO: ProcessInputLine von TUI anschauen
  def processInputLine(input: String): Unit = {

  }
}
