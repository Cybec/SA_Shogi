package de.htwg.se.Shogi.aview

import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface

class HttpServerSepc extends WordSpec with Matchers with ScalatestRouteTest {
  //  val injector: Injector = Guice.createInjector(new ShogiModule)
  //  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  //  val tui = new Tui(controller)
  //  val webServer = new HttpServer(controller, tui)
  //
  //
  //  val host = "http://localhost:8080/"
  //
  //
  //  "The service" should {
  //    var doc = Jsoup.connect(host + "shogi").get()
  //    "have the correct Header" in {
  //      val header = doc.select("h1")
  //      header.text() should be("HTWG Shogi")
  //    }
  //    "have style" in {
  //      val p = doc.select("p")
  //      p.attr("style") should be("font-family:'Lucida Console', monospace")
  //    }
  //    "have empty Playground at the start of server" in {
  //      val p = doc.select("p")
  //      p.text() should be("Captured:    " +
  //        " &nbsp0    &nbsp1    &nbsp2    &nbsp3    &nbsp4    &nbsp5    &nbsp6    &nbsp7    &nbsp8    " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  a " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  b " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  c " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  d " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  e " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  f " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  g " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  h " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  i " +
  //        "--------------------------------------------------------- " +
  //        "Captured: ")
  //    }
  //    "have empty Playground after shogi/empty was called" in {
  //      doc = Jsoup.connect(host + "shogi/empty").get()
  //      val p = doc.select("p")
  //      p.text() should be("Captured:    " +
  //        " &nbsp0    &nbsp1    &nbsp2    &nbsp3    &nbsp4    &nbsp5    &nbsp6    &nbsp7    &nbsp8    " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  a " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  b " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  c " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  d " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  e " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  f " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  g " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  h " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  i " +
  //        "--------------------------------------------------------- " +
  //        "Captured: ")
  //    }
  //    "have new Playground after shogi/new was called" in {
  //      doc = Jsoup.connect(host + "shogi/new").get()
  //      val p = doc.select("p")
  //      p.text() should be("Captured:     " +
  //        "&nbsp0    &nbsp1    &nbsp2    &nbsp3    &nbsp4    &nbsp5    &nbsp6    &nbsp7    &nbsp8    " +
  //        "---------------------------------------------------------  " +
  //        "|&nbspL°  |&nbspKN° |&nbspSG° |&nbspGG° |&nbspK°  |&nbspGG° |&nbspSG° |&nbspKN° |&nbspL°  |  a " +
  //        "---------------------------------------------------------  " +
  //        "|     |&nbspR°  |     |     |     |     |     |&nbspB°  |     |  b " +
  //        "---------------------------------------------------------  " +
  //        "|&nbspP°  |&nbspP°  |&nbspP°  |&nbspP°  |&nbspP°  |&nbspP°  |&nbspP°  |&nbspP°  |&nbspP°  |  c " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  d " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  e " +
  //        "---------------------------------------------------------  " +
  //        "|     |     |     |     |     |     |     |     |     |  f " +
  //        "---------------------------------------------------------  " +
  //        "|&nbspP   |&nbspP   |&nbspP   |&nbspP   |&nbspP   |&nbspP   |&nbspP   |&nbspP   |&nbspP   |  g " +
  //        "---------------------------------------------------------  " +
  //        "|     |&nbspB   |     |     |     |     |     |&nbspR   |     |  h " +
  //        "---------------------------------------------------------  " +
  //        "|&nbspL   |&nbspKN  |&nbspSG  |&nbspGG  |&nbspK   |&nbspGG  |&nbspSG  |&nbspKN  |&nbspL   |  i " +
  //        "--------------------------------------------------------- " +
  //        "Captured: ")
  //    }
  //  }
}