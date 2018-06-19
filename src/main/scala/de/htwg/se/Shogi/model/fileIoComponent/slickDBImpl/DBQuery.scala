package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import slick.lifted.TableQuery

import scala.concurrent.Future
import slick.jdbc.MySQLProfile.api._

class DBQuery {
  //  val db = Database.forConfig("mysql")
  val db = Database.forURL("jdbc:mysql://192.168.99.100:3306/shogi-db?user=root&password=1234hot5")
  val gameSessionQuery: TableQuery[GameSession] = TableQuery[GameSession]
  val playerSessionQuery: TableQuery[PlayerSession] = TableQuery[PlayerSession]
  val boardSessionQuery: TableQuery[BoardSession] = TableQuery[BoardSession]
  val playerFirstContainerQuery: TableQuery[PlayerFirstContainerSession] = TableQuery[PlayerFirstContainerSession]
  val playerSecondContainerQuery: TableQuery[PlayerSecondContainerSession] = TableQuery[PlayerSecondContainerSession]
  val pieceSessionQuery: TableQuery[PieceSession] = TableQuery[PieceSession]

  //INSERT
  def insert(piece: PieceProfile): Future[Int] = db.run(pieceSessionQuery += piece)

  def insert(containerFirst: PlayerFirstContainerProfile): Future[Int] = db.run(playerFirstContainerQuery += containerFirst)

  def insert(containerSecond: PlayerSecondContainerProfile): Future[Int] = db.run(playerSecondContainerQuery += containerSecond)

  def insert(board: BoardProfile): Future[Int] = db.run(boardSessionQuery += board)

  def insert(player: PlayerProfile): Future[Int] = db.run(playerSessionQuery += player)

  def insert(game: GameSessionProfile): Future[Int] = {
    db.run(gameSessionQuery += game)
  }

  //GET
  def getPiece(id: Int): Future[Option[PieceProfile]] = db.run(pieceSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPiece(name: String): Future[Option[PieceProfile]] = db.run(pieceSessionQuery.filter(_.name === name).take(1).result.headOption)

  def getPlayerFirstContainer(id: Int): Future[Option[PlayerFirstContainerProfile]] = db.run(playerFirstContainerQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayerSecondContainer(id: Int): Future[Option[PlayerSecondContainerProfile]] = db.run(playerSecondContainerQuery.filter(_.id === id).take(1).result.headOption)

  def getBoard(id: Int): Future[Option[BoardProfile]] = db.run(boardSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayer(id: Int): Future[Option[PlayerProfile]] = db.run(playerSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayer(name: String): Future[Option[PlayerProfile]] = db.run(playerSessionQuery.filter(_.name === name).take(1).result.headOption)

  def getGame(id: Int): Future[Option[GameSessionProfile]] = db.run(gameSessionQuery.filter(_.id === id).take(1).result.headOption)

  //DELET
  def deletePiece(id: Int): Future[Int] = db.run(pieceSessionQuery.filter(_.id === id).delete)

  def deletePiece(name: String): Future[Int] = db.run(pieceSessionQuery.filter(_.name === name).delete)

  def deletePlayerFirstContainer(id: Int): Future[Int] = db.run(playerFirstContainerQuery.filter(_.id === id).delete)

  def deletePlayerSecondContainer(id: Int): Future[Int] = db.run(playerSecondContainerQuery.filter(_.id === id).delete)

  def deleteBoard(id: Int): Future[Int] = db.run(boardSessionQuery.filter(_.id === id).delete)

  def deletePlayer(id: Int): Future[Int] = db.run(playerSessionQuery.filter(_.id === id).delete)

  def deletePlayer(name: String): Future[Int] = db.run(playerSessionQuery.filter(_.name === name).delete)

  def deleteGame(id: Int): Future[Int] = db.run(gameSessionQuery.filter(_.id === id).delete)
}
