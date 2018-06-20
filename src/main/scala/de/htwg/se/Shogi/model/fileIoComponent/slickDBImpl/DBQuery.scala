package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import slick.dbio.Effect
import slick.lifted.{ AbstractTable, TableQuery }

import scala.concurrent.{ Await, Future }
import slick.jdbc.MySQLProfile.api._
import slick.sql.FixedSqlAction

import scala.concurrent.duration.Duration

class DBQuery {
  val db = Database.forURL("jdbc:mysql://192.168.99.100:3306/shogi-db?user=root&password=1234hot5")
  val gameSessionQuery: TableQuery[GameSession] = TableQuery[GameSession]
  val playerSessionQuery: TableQuery[PlayerSession] = TableQuery[PlayerSession]
  val playerContainerQuery: TableQuery[PlayerContainerSession] = TableQuery[PlayerContainerSession]
  val pieceOnBoardSessionQuery: TableQuery[PieceOnBoardSession] = TableQuery[PieceOnBoardSession]
  val pieceInContainerSessionQuery: TableQuery[PieceContainerSession] = TableQuery[PieceContainerSession]

  def run[E <: AbstractTable[_]](query: FixedSqlAction[Int, NoStream, Effect.Write], s: TableQuery[E]): Option[(Int, Int)] = {
    val eventualInsertResult = db.run(query)
    val insertResult = Await.result(eventualInsertResult, Duration.Inf)

    s match {
      case item => item.shaped.value.tableName match {
        case "PIECE_ON_BOARD_SESSION" => {
          val eventualInsertResult_2 = db.run(pieceOnBoardSessionQuery.result)
          Some((insertResult, Await.result(eventualInsertResult_2, Duration.Inf).map(x => x.id).max))
        }
        case "PIECE_CONTAINER_SESSION" => {
          val eventualInsertResult_2 = db.run(pieceInContainerSessionQuery.result)
          Some((insertResult, Await.result(eventualInsertResult_2, Duration.Inf).map(x => x.id).max))
        }
        case "PLAYER_CONTAINER_SESSION" => {
          val eventualInsertResult_2 = db.run(playerContainerQuery.result)
          Some((insertResult, Await.result(eventualInsertResult_2, Duration.Inf).map(x => x.id).max))
        }
        case "PLAYER_SESSION" => {
          val eventualInsertResult_2 = db.run(playerSessionQuery.result)
          Some((insertResult, Await.result(eventualInsertResult_2, Duration.Inf).map(x => x.id).max))
        }
        case "GAME_SESSION" => {
          val eventualInsertResult_2 = db.run(gameSessionQuery.result)
          Some((insertResult, Await.result(eventualInsertResult_2, Duration.Inf).map(x => x.id).max))
        }
        case _ => None
      }
    }
  }

  //INSERT
  def insert(piece: PieceOnBoardProfile): Option[(Int, Int)] = run(pieceOnBoardSessionQuery += piece, pieceOnBoardSessionQuery)

  def insert(container: PieceContainerProfile): Option[(Int, Int)] = run(pieceInContainerSessionQuery += container, pieceInContainerSessionQuery)

  def insert(container: PlayerContainerProfile): Option[(Int, Int)] = run(playerContainerQuery += container, playerContainerQuery)

  def insert(player: PlayerProfile): Option[(Int, Int)] = run(playerSessionQuery += player, playerSessionQuery)

  def insert(game: GameSessionProfile): Option[(Int, Int)] = run(gameSessionQuery += game, gameSessionQuery)

  //GET
  def getPieceOnBoard(playerID: Int): Seq[PieceOnBoardProfile] = {
    val eventualInsertResult = db.run(pieceOnBoardSessionQuery.filter(_.playerID === playerID).result)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def getPieceInContainer(containerID: Int): Seq[PieceContainerProfile] = {
    val eventualInsertResult = db.run(pieceInContainerSessionQuery.filter(_.containerID === containerID).result)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def getPlayerContainer(id: Int): Option[PlayerContainerProfile] = {
    val eventualInsertResult = db.run(playerContainerQuery.filter(_.id === id).take(1).result.headOption)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def getPlayer(id: Int): Option[PlayerProfile] = {
    val eventualInsertResult = db.run(playerSessionQuery.filter(_.id === id).take(1).result.headOption)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def getPlayer(name: String): Option[PlayerProfile] = {
    val eventualInsertResult = db.run(playerSessionQuery.filter(_.name === name).take(1).result.headOption)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def getGame(id: Int): Option[GameSessionProfile] = {
    val eventualInsertResult = db.run(gameSessionQuery.filter(_.id === id).take(1).result.headOption)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def getLastIDGame(): Int = Await.result(db.run(gameSessionQuery.result), Duration.Inf).map(x => x.id).max

  //DELET
  def deletePieceOnBoard(id: Int): Future[Int] = db.run(pieceOnBoardSessionQuery.filter(_.id === id).delete)

  def deletePieceInContainer(id: Int): Future[Int] = db.run(pieceInContainerSessionQuery.filter(_.id === id).delete)

  def deletePlayerContainer(id: Int): Future[Int] = db.run(playerContainerQuery.filter(_.id === id).delete)

  def deletePlayer(id: Int): Future[Int] = db.run(playerSessionQuery.filter(_.id === id).delete)

  def deletePlayer(name: String): Future[Int] = db.run(playerSessionQuery.filter(_.name === name).delete)

  def deleteGame(id: Int): Future[Int] = db.run(gameSessionQuery.filter(_.id === id).delete)
}
