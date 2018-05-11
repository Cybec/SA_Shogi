package de.htwg.se.Shogi.aview

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.google.inject.{Guice, Injector}
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import org.jsoup.Jsoup


class HttpServerSepc extends WordSpec with Matchers with ScalatestRouteTest {
  val injector: Injector = Guice.createInjector(new ShogiModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val webServer = new HttpServer(controller, tui)


  val host = "http://localhost:8080/"
  val doc = Jsoup.connect(host + "shogi").get()


  "The Shogi home page" should {
    "have the correct Header" in {
      val header = doc.select("h1")
      header.text() should be("HTWG Shogi")
    }
    "have style" in {
      val p = doc.select("p")
      p.attr("style") should be("font-family:'Lucida Console', monospace")
    }
    "have new Playground" in {
      val p = doc.select("p")
      p.text() should be("Captured:    " +
        " &nbsp0    &nbsp1    &nbsp2    &nbsp3    &nbsp4    &nbsp5    &nbsp6    &nbsp7    &nbsp8    " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  a " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  b " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  c " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  d " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  e " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  f " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  g " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  h " +
        "---------------------------------------------------------  " +
        "|     |     |     |     |     |     |     |     |     |  i " +
        "--------------------------------------------------------- " +
        "Captured: ")
    }
  }
}