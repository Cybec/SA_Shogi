package de.htwg.se.Shogi.model.fileIoComponent.mongoDBImpl

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl.Controller
import org.mongodb.scala.{ MongoCredential, ServerAddress }
import play.api.libs.json.{ JsValue, Json }
import com.google.inject.name.Names
import com.google.inject.{ Guice, Injector }
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.pieceComponent.PieceInterface
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl.{ PieceFactory, PiecesEnum }
import de.htwg.se.Shogi.model.playerComponent.Player
import de.htwg.se.Shogi.{ ShogiModule, ShogiModuleConf }
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json._

class MongoDB extends DAOInterface {
  val SERVER = "192.168.99.100"
  //  val SERVER = "0.0.0.0"
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
    var loadReturnOption: Option[(BoardInterface, Boolean, Player, Player)] = None
    val document = db.find().sort(new BasicDBObject("_id", -1)).toArray().get(0)
    val json: JsValue = Json.parse(document.toString)
    val size = (json \ "board" \ "size").get.toString.toInt
    val state = (json \ "state").get.toString.toBoolean

    val player1Data = (json \ "player_1").get.toString.replace("\"", "").replace("[", "").replace("]", "").split(",")
    val player2Data = (json \ "player_2").get.toString.replace("\"", "").replace("[", "").replace("]", "").split(",")
    val player1 = Player(player1Data(0), player1Data(1).toBoolean)
    val player2 = Player(player2Data(0), player2Data(1).toBoolean)

    val injector: Injector = Guice.createInjector(new ShogiModule)

    loadReturnOption = getBoardBySize(size, injector) match {
      case Some(board) =>
        val firstPlayer = true
        val secondPlayer = false
        val newBoard = board.setContainer(
          getConqueredPieces((json \\ "conquered_pieces1").toArray, firstPlayer),
          getConqueredPieces((json \\ "conquered_pieces2").toArray, secondPlayer)
        )
        Some((newBoard, state, player1, player2))
      case _ => None
    }

    loadReturnOption match {
      case Some((board, savedState, player_1, player_2)) =>
        var _board = board
        for (
          column <- 0 until board.size;
          row <- 0 until board.size
        ) {
          val piece = "piece_" + (column.toString) + "_" + (row.toString)
          val pieceName = (json \ "board" \ "board_field" \ "field" \ piece \ "name").as[JsString].value
          val firstPlayer = (json \ "board" \ "board_field" \ "field" \ piece \ "is_first_owner").get.toString.toBoolean
          PiecesEnum.withNameOpt(pieceName) match {
            case Some(pieceEnum) =>
              _board = _board.replaceCell(column, row, PieceFactory.apply(pieceEnum, firstPlayer))
            case None =>
          }
        }
        loadReturnOption = Some(_board, savedState, player_1, player_2)
      case None => None
    }
    loadReturnOption
  }

  def getBoardBySize(size: Int, injector: Injector): Option[BoardInterface] = {
    size match {
      case ShogiModuleConf.defaultBoardSize =>
        Some(injector.instance[BoardInterface](Names.named(ShogiModuleConf.defaultBoard)).createNewBoard())
      case ShogiModuleConf.smallBoardSize =>
        Some(injector.instance[BoardInterface](Names.named(ShogiModuleConf.smallBoard)).createNewBoard())
      case ShogiModuleConf.tinyBoardSize =>
        Some(injector.instance[BoardInterface](Names.named(ShogiModuleConf.tinyBoard)).createNewBoard())
      case _ => None
    }
  }

  def getConqueredPieces(jsArray: Array[JsValue], istFirst: Boolean): List[PieceInterface] = {
    var stringList: List[String] = List[String]()
    var pieceList: List[PieceInterface] = List[PieceInterface]()

    for (x <- jsArray) yield (x \\ "name").foreach(i => stringList = stringList :+ i.as[String])

    for (x: String <- stringList) {
      PiecesEnum.withNameOpt(x) match {
        case Some(pieceEnum) => pieceList = pieceList :+ PieceFactory.apply(pieceEnum, istFirst)
        case None =>
      }
    }
    pieceList
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

    def containerBuilderpieces(pieces: Array[PieceInterface]): MongoDBObject = {
      val containerPieces = MongoDBObject.newBuilder
      pieces.foreach(x => containerPieces += "piece" -> pieceBuilder((x)))
      containerPieces.result()
    }

    val container_player_1_Builder = MongoDBObject.newBuilder
    container_player_1_Builder += "conquered_pieces1" -> containerBuilderpieces(board.getContainer._1.toArray)
    val container_player_2_Builder = MongoDBObject.newBuilder
    container_player_2_Builder += "conquered_pieces2" -> containerBuilderpieces(board.getContainer._2.toArray)
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
