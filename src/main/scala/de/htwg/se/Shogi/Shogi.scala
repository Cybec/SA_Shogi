package de.htwg.se.Shogi

import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.aview.Tui
import de.htwg.se.Shogi.aview.gui.SwingGui
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.UpdateAll

import scala.swing.Publisher

// $COVERAGE-OFF
object Shogi extends Publisher {
  val injector: Injector = Guice.createInjector(new ShogiModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  listenTo(gui)
  controller.publish(new UpdateAll)

  def main(args: Array[String]): Unit = {
    var input: String = ""

    do {
      tui.printInputMenu()
      input = scala.io.StdIn.readLine()
      tui.processInputLine(input)
    } while (input != "q")
    System.exit(0)
  }

  reactions += { case _ => if (gui == null) System.exit(0) }
}