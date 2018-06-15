package de.htwg.se.Shogi.model

import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl.SlickDB
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class SlickDBSpec extends WordSpec with Matchers {
  "SlickDB" when {
    val test: DAOInterface = new SlickDB
    "called load" should {
      "print" in {
        println(test.load)
        true
      }
    }
  }
}