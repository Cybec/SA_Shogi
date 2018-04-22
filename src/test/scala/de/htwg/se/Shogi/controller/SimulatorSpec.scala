package de.htwg.se.Shogi.controller

import java.util.concurrent.TimeUnit

import com.google.inject.{ Guice, Injector }
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import de.htwg.se.Shogi.controller.controllerComponent.simulationBaseImpl.Simulator
import de.htwg.se.Shogi.controller.controllerComponent.simulationBaseImpl.Simulator.Simulate

import scala.concurrent.duration.Duration

//noinspection ScalaStyle
@RunWith(classOf[JUnitRunner])
class SimulatorSpec extends TestKit(ActorSystem("MySystem")) with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {
  "An Simulation actor" must {
    val injector: Injector = Guice.createInjector(new ShogiModule)
    val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
    "send back messages unchanged" in {
      controller.createNewBoard()
      val simu = system.actorOf(TestActors.echoActorProps)
      simu ! Simulator.Simulate(controller)

      expectMsg("some")

      /*       receiveOne(Duration.apply(5, TimeUnit.SECONDS)) match{
         case Simulate(controllerNew) => print(controller.boardToString())
         case _ => System.exit(1)
      }
*/
    }

  }
}
