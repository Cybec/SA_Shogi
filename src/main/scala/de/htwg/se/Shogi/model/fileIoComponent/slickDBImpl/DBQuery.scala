package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Future
import slick.jdbc.SQLServerProfile.api._

class DBQuery {
  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("sqlserver")
  val db: JdbcProfile#Backend#Database = dbConfig.db

  val gameSessionQuery: TableQuery[GameSession] = TableQuery[GameSession]
  val playerSessionQuery: TableQuery[PlayerSession] = TableQuery[PlayerSession]
  val boardSessionQuery: TableQuery[BoardSession] = TableQuery[BoardSession]
  val playerFirstContainerQuery: TableQuery[PlayerFirstContainerSession] = TableQuery[PlayerFirstContainerSession]
  val playerSecondContainerQuery: TableQuery[PlayerSecondContainerSession] = TableQuery[PlayerSecondContainerSession]
  val pieceSessionQuery: TableQuery[PieceSession] = TableQuery[PieceSession]

  def insert(piece: PieceProfile): Future[Int] = db.run(pieceSessionQuery += piece)

  def insert(containerFirst: PlayerFirstContainerProfile): Future[Int] = db.run(playerFirstContainerQuery += containerFirst)

  def insert(containerSecond: PlayerSecondContainerProfile): Future[Int] = db.run(playerSecondContainerQuery += containerSecond)

  def insert(board: BoardProfile): Future[Int] = db.run(boardSessionQuery += board)

  def insert(player: PlayerProfile): Future[Int] = db.run(playerSessionQuery += player)

  def insert(game: GameSessionProfile): Future[Int] = db.run(gameSessionQuery += game)

  def getPiece(id: Int): Future[Option[PieceProfile]] =
    db.run(pieceSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayerFirstContainer(id: Int): Future[Option[PlayerFirstContainerProfile]] =
    db.run(playerFirstContainerQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayerSecondContainer(id: Int): Future[Option[PlayerSecondContainerProfile]] =
    db.run(playerSecondContainerQuery.filter(_.id === id).take(1).result.headOption)

  def getBoard(id: Int): Future[Option[BoardProfile]] =
    db.run(boardSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getPlayer(id: Int): Future[Option[PlayerProfile]] =
    db.run(playerSessionQuery.filter(_.id === id).take(1).result.headOption)

  def getGame(id: Int): Future[Option[GameSessionProfile]] =
    db.run(gameSessionQuery.filter(_.id === id).take(1).result.headOption)

  //Delete wird evtl. noch nciht benutzt
  def deletePiece(id: Int): Future[Int] = db.run(pieceSessionQuery.filter(_.id === id).delete)

  def deletePlayerFirstContainer(id: Int): Future[Int] = db.run(playerFirstContainerQuery.filter(_.id === id).delete)

  def deletePlayerSecondContainer(id: Int): Future[Int] = db.run(playerSecondContainerQuery.filter(_.id === id).delete)

  def deleteBoard(id: Int): Future[Int] = db.run(boardSessionQuery.filter(_.id === id).delete)

  def deletePlayer(id: Int): Future[Int] = db.run(playerSessionQuery.filter(_.id === id).delete)

  def deleteGame(id: Int): Future[Int] = db.run(gameSessionQuery.filter(_.id === id).delete)
}
