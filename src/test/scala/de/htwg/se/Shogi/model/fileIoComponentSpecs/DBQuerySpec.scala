import de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl.{DBQuery, PieceProfile, PieceSession, SlickDB}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class DBQuerySpec extends WordSpec with Matchers {
  val ranControlNumber = new scala.util.Random(100)
  val dbQuery = new DBQuery

  "DBQuerySpec" when {
    "called insert, get and delete Piece" should {
      val pieceProfile = PieceProfile(1, "TestPiece_" + ranControlNumber, true, true)

      def f(ps: PieceSession, pp: PieceProfile): Boolean = ps.name == pp.name

      "save" in {
        val eventualInsertResult = dbQuery.insert(pieceProfile)
        val insertResult = Await.result(eventualInsertResult, Duration.Inf)
        insertResult should be(1)
      }
      "load" in {
        val eventualMaybeUserProfile = dbQuery.getPiece(pieceProfile, f)
        val maybeUserProfile = Await.result(eventualMaybeUserProfile, Duration.Inf)
        for (piece <- maybeUserProfile) yield {
          piece.name should be(pieceProfile.name)
          piece.hasPromotion should be(pieceProfile.hasPromotion)
          piece.isFirstOwner should be(pieceProfile.isFirstOwner)
        }
      }
      "delete" in {
        val eventualDeleteResult = dbQuery.deletePiece(pieceProfile, f)
        val deleteResult = Await.result(eventualDeleteResult, Duration.Inf)
        deleteResult should be(1)
      }
    }
  }
}