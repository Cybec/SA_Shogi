import de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl.{DBQuery, PieceProfile}
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

      "save" in {
        val eventualInsertResult = dbQuery.insert(pieceProfile)
        val insertResult = Await.result(eventualInsertResult, Duration.Inf)
        insertResult should be(1)
      }
      "load" in {
        val eventualMaybeUserProfile = dbQuery.getPiece(pieceProfile.name)
        val maybeUserProfile = Await.result(eventualMaybeUserProfile, Duration.Inf)
        for (piece <- maybeUserProfile) yield {
          piece.name should be(pieceProfile.name)
          piece.hasPromotion should be(pieceProfile.hasPromotion)
          piece.isFirstOwner should be(pieceProfile.isFirstOwner)

          val eventualMaybeUserProfile_2 = dbQuery.getPiece(piece.id)
          val maybeUserProfile_2 = Await.result(eventualMaybeUserProfile_2, Duration.Inf)
          for (piece_2 <- maybeUserProfile_2) yield {
            piece_2.name should be(pieceProfile.name)
            piece_2.hasPromotion should be(pieceProfile.hasPromotion)
            piece_2.isFirstOwner should be(pieceProfile.isFirstOwner)
          }
        }
      }
      "delete" in {
        val eventualDeleteResult = dbQuery.deletePiece(pieceProfile.name)
        val deleteResult = Await.result(eventualDeleteResult, Duration.Inf)
        deleteResult should be(1)

        val eventualMaybeUserProfile = dbQuery.getPiece(pieceProfile.name)
        val maybeUserProfile = Await.result(eventualMaybeUserProfile, Duration.Inf)
        maybeUserProfile should be(None)


        val eventualInsertResult = dbQuery.insert(pieceProfile)
        val insertResult = Await.result(eventualInsertResult, Duration.Inf)
        insertResult should be(1)

        val eventualMaybeUserProfile_2 = dbQuery.getPiece(pieceProfile.name)
        val maybeUserProfile_2 = Await.result(eventualMaybeUserProfile_2, Duration.Inf)
        for (piece_2 <- maybeUserProfile_2) yield {
          piece_2.name should be(pieceProfile.name)
          piece_2.hasPromotion should be(pieceProfile.hasPromotion)
          piece_2.isFirstOwner should be(pieceProfile.isFirstOwner)
          val eventualDeleteResult_2 = dbQuery.deletePiece(piece_2.id)
          val deleteResult_2 = Await.result(eventualDeleteResult_2, Duration.Inf)
          deleteResult_2 should be(1)

          val eventualMaybeUserProfile_2 = dbQuery.getPiece(piece_2.name)
          val maybeUserProfile_2 = Await.result(eventualMaybeUserProfile_2, Duration.Inf)
          maybeUserProfile_2 should be(None)
        }
      }
    }
  }
}