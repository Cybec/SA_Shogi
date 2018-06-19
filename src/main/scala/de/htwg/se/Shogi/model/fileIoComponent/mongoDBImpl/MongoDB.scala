package de.htwg.se.Shogi.model.fileIoComponent.mongoDBImpl

import com.google.inject.name.Names
import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.playerComponent.Player
import com.mongodb.casbah.Imports._
import com.mongodb.{ DBCursor, DBObject }
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.Imports.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.Shogi.model.pieceComponent.PieceInterface
import org.mongodb.scala.{ MongoCredential, ServerAddress }
import org.mongodb.scala.model.Sorts._
import scala.collection.mutable

object Test {
  def main(args: Array[String]): Unit = {
    val controller: Controller = new Controller()
    val player_1: Player = Player("Player1", first = true)
    val player_2: Player = Player("Player2", first = false)
    controller.createNewBoard()
    val currentPlayerIsFirst = true
    (new MongoDB).save(controller.board, currentPlayerIsFirst, player_1, player_2)
  }
}

class MongoDB extends DAOInterface {
  val SERVER = "192.168.99.100"
  val PORT = 27017
  val DATABASE = "GameSession"
  val COLLECTION = "GameSave"
  val server = new ServerAddress(SERVER, PORT)

  val credentials = MongoCredential.createCredential("root", "admin", "1234hot5".toArray)
  val mongoClient = MongoClient(server, List(credentials))
  val db = mongoClient.getDB(DATABASE).getCollection(COLLECTION)

  /**
   * Loads the saved game
   *
   * @return Returning an Option with the loaded Board, playerTurn and the two PLayers
   */
  override def load: Option[(BoardInterface, Boolean, Player, Player)] = {
    None
  }
//  def loadPieces: Array[Array[PieceInterface]] = {
//    var pieces = Array[Array[PieceInterface]]()
//    pieces
//
//    val find = db.getCollection(COLLECTION).find()
//    while (find.hasNext) {
//
//    }
//
//    None
//  }

  /**
   * Saving the current game
   *
   * @param board    current Board
   * @param state    current Player Turn (true=player_1/false=player_2)
   * @param player_1 Player_1
   * @param player_2 Player_2
   */
  override def save(board: BoardInterface, state: Boolean, player_1: Player, player_2: Player): Unit = {
    def pieceBuilder(piece: PieceInterface): MongoDBObject = {
      val piece_result = MongoDBObject.newBuilder
      piece_result += "name" -> piece.name
      piece_result += "has_promotion" -> piece.hasPromotion
      piece_result += "is_first_owner" -> piece.isFirstOwner
      piece_result.result()
    }

    def pieceArrayBuilder(pieces: Array[Array[PieceInterface]]): MongoDBObject = {
      val piece = MongoDBObject.newBuilder
      for (
        column <- 0 until board.size;
        row <- 0 until board.size
      ) {
        piece += "piece_" + column + "_" + row -> pieceBuilder(pieces(column)(row))
      }
      piece.result()
    }

    val container_player_1_Builder = MongoDBObject.newBuilder
    container_player_1_Builder += "conquered_pieces" -> board.getContainer._1.toArray
    val container_player_2_Builder = MongoDBObject.newBuilder
    container_player_2_Builder += "conquered_pieces" -> board.getContainer._2.toArray
    val board_field = MongoDBObject.newBuilder
    board_field += "field" -> pieceArrayBuilder(board.toArray)

    val boardBuilder = MongoDBObject.newBuilder
    boardBuilder += "size" -> board.size
    boardBuilder += "container_player_1" -> container_player_1_Builder.result()
    boardBuilder += "container_player_2" -> container_player_2_Builder.result()
    boardBuilder += "board_field" -> board_field.result()

    val builder = MongoDBObject.newBuilder
    builder += "player_1" -> player_1
    builder += "player_2" -> player_2
    builder += "state" -> state
    builder += "board" -> boardBuilder.result()

    db.insert(builder.result)
  }
}