package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import slick.lifted.{ ForeignKeyQuery, ProvenShape, Rep, Tag }
import slick.jdbc.SQLServerProfile.api._

case class GameSessionProfile(id: Int, firstPlayerID: Int, secondPlayerID: Int, state: Boolean, boardID: Int)

case class PlayerProfile(id: Int, name: String, first: Boolean)

case class BoardProfile(id: Int, size: Int, firstPlayerContainerID: Int, secondPlayerContainerID: Int)

case class PlayerFirstContainerProfile(id: Int, pieceID: Int)

case class PlayerSecondContainerProfile(id: Int, pieceID: Int)

case class PieceProfile(id: Int, name: String, hasPromotion: Boolean, isFirstOwner: Boolean)

protected class PieceSession(tag: Tag) extends Table[PieceProfile](tag, "PlayerFirstContainerProfile_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def hasPromotion: Rep[Boolean] = column[Boolean]("hasPromotion")

  def isFirstOwner: Rep[Boolean] = column[Boolean]("isFirstOwner")

  def * : ProvenShape[PieceProfile] = (id, name, hasPromotion, isFirstOwner) <> (PieceProfile.tupled, PieceProfile.unapply) // scalastyle:ignore
}

protected class PlayerFirstContainerSession(tag: Tag) extends Table[PlayerFirstContainerProfile](tag, "PlayerFirstContainerProfile_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def pieceID: Rep[Int] = column[Int]("pieceID")

  def * : ProvenShape[PlayerFirstContainerProfile] = (id, pieceID) <> (PlayerFirstContainerProfile.tupled, PlayerFirstContainerProfile.unapply) // scalastyle:ignore

  def piece: ForeignKeyQuery[PieceSession, PieceProfile] = foreignKey("piece_fk", pieceID, TableQuery[PieceSession])(_.id)

}

protected class PlayerSecondContainerSession(tag: Tag) extends Table[PlayerSecondContainerProfile](tag, "PlayerSecondContainerProfile_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def pieceID: Rep[Int] = column[Int]("pieceID")

  def * : ProvenShape[PlayerSecondContainerProfile] = (id, pieceID) <> (PlayerSecondContainerProfile.tupled, PlayerSecondContainerProfile.unapply) // scalastyle:ignore

  def piece: ForeignKeyQuery[PieceSession, PieceProfile] = foreignKey("piece_fk", pieceID, TableQuery[PieceSession])(_.id)

}

protected class BoardSession(tag: Tag) extends Table[BoardProfile](tag, "BOARD_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstPlayerContainerID: Rep[Int] = column[Int]("firstPlayerContainerID")

  def secondPlayerContainerID: Rep[Int] = column[Int]("secondPlayerContainerID")

  def size: Rep[Int] = column[Int]("size")

  def * : ProvenShape[BoardProfile] = (id, firstPlayerContainerID, secondPlayerContainerID, size) <> (BoardProfile.tupled, BoardProfile.unapply) // scalastyle:ignore
  def firstPlayerContainer: ForeignKeyQuery[PlayerFirstContainerSession, PlayerFirstContainerProfile] = {
    foreignKey("firstPlayerContainer_fk", firstPlayerContainerID, TableQuery[PlayerFirstContainerSession])(_.id)
  }

  def secondPlayerContainer: ForeignKeyQuery[PlayerSecondContainerSession, PlayerSecondContainerProfile] = {
    foreignKey("secondPlayerContainer_fk", secondPlayerContainerID, TableQuery[PlayerSecondContainerSession])(_.id)
  }
}

protected class PlayerSession(tag: Tag) extends Table[PlayerProfile](tag, "PLAYER_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def first: Rep[Boolean] = column[Boolean]("first")

  def * : ProvenShape[PlayerProfile] = (id, name, first) <> (PlayerProfile.tupled, PlayerProfile.unapply) // scalastyle:ignore
}

protected class GameSession(tag: Tag) extends Table[GameSessionProfile](tag, "GAME_SESSION") {

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstPlayerID: Rep[Int] = column[Int]("firstPlayerID")

  def secondPlayerID: Rep[Int] = column[Int]("secondPlayerID")

  def boardID: Rep[Int] = column[Int]("boardID")

  def state: Rep[Boolean] = column[Boolean]("state")

  def * : ProvenShape[GameSessionProfile] = (id, firstPlayerID, secondPlayerID, state, boardID) <> (GameSessionProfile.tupled, GameSessionProfile.unapply) // scalastyle:ignore

  def firstPlayer: ForeignKeyQuery[PlayerSession, PlayerProfile] = foreignKey("firstPlayer_fk", firstPlayerID, TableQuery[PlayerSession])(_.id)

  def secondPlayer: ForeignKeyQuery[PlayerSession, PlayerProfile] = foreignKey("secondPlayer_fk", secondPlayerID, TableQuery[PlayerSession])(_.id)

  def board: ForeignKeyQuery[BoardSession, BoardProfile] = foreignKey("board_fk", boardID, TableQuery[BoardSession])(_.id)
}

