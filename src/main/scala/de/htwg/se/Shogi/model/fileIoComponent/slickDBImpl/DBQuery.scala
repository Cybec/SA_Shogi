package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import slick.lifted.TableQuery

import scala.concurrent.{Await, Future}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.duration.Duration

class DBQuery {
  val db = Database.forURL("jdbc:mysql://192.168.99.100:3306/shogi-db?user=root&password=1234hot5")
  val gameSessionQuery: TableQuery[GameSession] = TableQuery[GameSession]
  val playerSessionQuery: TableQuery[PlayerSession] = TableQuery[PlayerSession]
  val playerContainerQuery: TableQuery[PlayerContainerSession] = TableQuery[PlayerContainerSession]
  val pieceOnBoardSessionQuery: TableQuery[PieceOnBoardSession] = TableQuery[PieceOnBoardSession]
  val pieceInContainerSessionQuery: TableQuery[PieceContainerSession] = TableQuery[PieceContainerSession]

  //INSERT
  def insert(piece: PieceOnBoardProfile): Int = {
    val eventualInsertResult: Future[Int] = db.run(pieceOnBoardSessionQuery += piece)
    Await.result(eventualInsertResult, Duration.Inf)
  }

  def insert(container: PieceContainerProfile): Future[Int] = db.run(pieceInContainerSessionQuery += container)

  def insert(container: PlayerContainerProfile): Future[Int] = db.run(playerContainerQuery += container)

  def insert(player: PlayerProfile): (Int, Int) = {
    val eventualInsertResult = db.run(playerSessionQuery += player)
    val insertResult = Await.result(eventualInsertResult, Duration.Inf)
    val newID = -1
    (insertResult, newID)
  }

  def insert(game: GameSessionProfile): Future[Int] = db.run(gameSessionQuery += game)

  //GET
  def getPieceOnBoard(id: Int): Future[Option[PieceOnBoardProfile]] = db.run(pieceOnBoardSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPieceInContainer(id: Int): Future[Option[PieceContainerProfile]] = db.run(pieceInContainerSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayerContainer(id: Int): Future[Option[PlayerContainerProfile]] = db.run(playerContainerQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayer(id: Int): Future[Option[PlayerProfile]] = db.run(playerSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayer(name: String): Future[Option[PlayerProfile]] = db.run(playerSessionQuery.filter(_.name === name).take(1).result.headOption)

  def getGame(id: Int): Future[Option[GameSessionProfile]] = db.run(gameSessionQuery.filter(_.id === id).take(1).result.headOption)

  //DELET
  def deletePieceOnBoard(id: Int): Future[Int] = db.run(pieceOnBoardSessionQuery.filter(_.id === id).delete)

  def deletePieceInContainer(id: Int): Future[Int] = db.run(pieceInContainerSessionQuery.filter(_.id === id).delete)

  def deletePlayerContainer(id: Int): Future[Int] = db.run(playerContainerQuery.filter(_.id === id).delete)

  def deletePlayer(id: Int): Future[Int] = db.run(playerSessionQuery.filter(_.id === id).delete)

  def deletePlayer(name: String): Future[Int] = db.run(playerSessionQuery.filter(_.name === name).delete)

  def deleteGame(id: Int): Future[Int] = db.run(gameSessionQuery.filter(_.id === id).delete)
}
