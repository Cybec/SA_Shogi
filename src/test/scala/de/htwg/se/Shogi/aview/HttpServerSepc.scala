package de.htwg.se.Shogi.aview

import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._

class HttpServerSepc extends WordSpec with Matchers with ScalatestRouteTest {

  val smallRoute =
    get {
      pathSingleSlash {
        complete {
          "Captain on the bridge!"
        }
      } ~
        path("ping") {
          complete("PONG!")
        }
    }

  "Ther service" should {
    "return a greeting for GET requests to the root path" in {
      // tests:
      Get() ~> smallRoute ~> check {
        responseAs[String] shouldEqual ("Captain on the bridge!")
      }
    }

    "return a 'PONG! response for GET requests to /ping" in {
      // tests:
      Get("/ping") ~> smallRoute ~> check {
        responseAs[String] shouldEqual ("PONG!")
      }
    }

    /*"leave GET requests to other paths unhandled" in {
      // tests:
      GET("/kermit") ~> smallRoute ~> check{
        handled shouldBe false
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      PUT() ~> Route.seal(smallRoute) ~> check {
        status shouldEqual(StatusCodes.MethodNotAllowed)
        responseAs[String] shouldEqual("HTTP method not allowd, supported methods: GET")
      }
    }*/
  }
}
