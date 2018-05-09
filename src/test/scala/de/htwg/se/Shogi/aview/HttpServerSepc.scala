package de.htwg.se.Shogi.aview

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.google.inject.{Guice, Injector}
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import org.scalatest.selenium.HtmlUnit

class HttpServerSepc extends WordSpec with Matchers with ScalatestRouteTest with HtmlUnit {
  val injector: Injector = Guice.createInjector(new ShogiModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val webServer = new HttpServer(controller, tui)


  val host = "http://localhost:8080/"

  "The Shogi home page" should {
    "have the correct Header" in {
      go to (host + "shogi")
      //      print(pageSource)
    }
  }
}