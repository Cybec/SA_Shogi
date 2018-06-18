package de.htwg.se.Shogi.model.fileIoComponent.mongoDBImpl

import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.playerComponent.Player
import com.mongodb.casbah.Imports._
import com.mongodb.{ DBCursor, DBObject }
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.mongodb.scala.{ MongoCredential, ServerAddress }

class MongoDB extends DAOInterface {
  val SERVER = "192.168.99.100"
  val PORT = 27017
  val DATABASE = "GameSession"
  val COLLECTION = "GameSave"
  val server = new ServerAddress(SERVER, PORT)

  val credentials = MongoCredential.createCredential("root", "admin", "1234hot5".toArray)
  val mongoClient = MongoClient(server, List(credentials))
  val db = mongoClient.getDB(DATABASE)

  /**
   * Loads the saved game
   *
   * @return Returning an Option with the loaded Board, playerTurn and the two PLayers
   */
  override def load: Option[(BoardInterface, Boolean, Player, Player)] = {
    printDB()
    None
  }

  /**
   * Saving the current game
   *
   * @param board    current Board
   * @param state    current Player Turn (true=player_1/false=player_2)
   * @param player_1 Player_1
   * @param player_2 Player_2
   */
  override def save(board: BoardInterface, state: Boolean, player_1: Player, player_2: Player): Unit = {

    def buildMongoDbObject(): DBObject = {
      val builder = MongoDBObject.newBuilder
      builder += "player_1" -> player_1
      builder += "player_2" -> player_2
      builder += "state" -> state
      builder += "board" -> board
      builder.result
    }

    db.getCollection(COLLECTION).insert(buildMongoDbObject)

    printDB
  }

  private def printDB(): Unit = {
    val find = db.getCollection(COLLECTION).find()
    while (find.hasNext) {
      println(print(find.next()))
    }
  }
}
