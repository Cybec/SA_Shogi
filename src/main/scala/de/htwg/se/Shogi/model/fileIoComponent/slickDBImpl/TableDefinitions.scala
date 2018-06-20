package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import slick.lifted.{ ForeignKeyQuery, ProvenShape, Rep, Tag }
import slick.jdbc.MySQLProfile.api._

/*
*   Profiles
* */
case class GameSessionProfile(id: Int, firstPlayerID: Int, secondPlayerID: Int, state: Boolean, firstPlayerContainerID: Int, secondPlayerContainerID: Int)

case class PlayerProfile(id: Int, name: String, first: Boolean)

case class PlayerContainerProfile(id: Int)

case class PieceOnBoardProfile(id: Int, name: String, positionRow: Int, positionColumn: Int, hasPromotion: Boolean, isFirstOwner: Boolean, playerID: Int)

case class PieceContainerProfile(id: Int, name: String, hasPromotion: Boolean, isFirstOwner: Boolean, containerID: Int)

/*
*   Sessions
* */
class PieceOnBoardSession(tag: Tag) extends Table[PieceOnBoardProfile](tag, "PIECE_ON_BOARD_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def positionRow: Rep[Int] = column[Int]("positionRow")

  def positionColumn: Rep[Int] = column[Int]("positionColumn")

  def hasPromotion: Rep[Boolean] = column[Boolean]("hasPromotion")

  def isFirstOwner: Rep[Boolean] = column[Boolean]("isFirstOwner")

  def playerID: Rep[Int] = column[Int]("playerID")

  def * : ProvenShape[PieceOnBoardProfile] = (id, name, positionRow, positionColumn, hasPromotion, isFirstOwner, playerID) <> (PieceOnBoardProfile.tupled, PieceOnBoardProfile.unapply) // scalastyle:ignore

  def player: ForeignKeyQuery[PlayerSession, PlayerProfile] = foreignKey("player_fk", playerID, TableQuery[PlayerSession])(_.id)

}

class PieceContainerSession(tag: Tag) extends Table[PieceContainerProfile](tag, "PIECE_CONTAINER_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def hasPromotion: Rep[Boolean] = column[Boolean]("hasPromotion")

  def isFirstOwner: Rep[Boolean] = column[Boolean]("isFirstOwner")

  def containerID: Rep[Int] = column[Int]("containerID")

  def * : ProvenShape[PieceContainerProfile] = (id, name, hasPromotion, isFirstOwner, containerID) <> (PieceContainerProfile.tupled, PieceContainerProfile.unapply) // scalastyle:ignore

  def container: ForeignKeyQuery[PlayerContainerSession, PlayerContainerProfile] = foreignKey("container_fk", containerID, TableQuery[PlayerContainerSession])(_.id) // scalastyle:ignore

}

class PlayerContainerSession(tag: Tag) extends Table[PlayerContainerProfile](tag, "PLAYER_CONTAINER_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def * : ProvenShape[PlayerContainerProfile] = (id) <> (PlayerContainerProfile, PlayerContainerProfile.unapply) // scalastyle:ignore

}

class PlayerSession(tag: Tag) extends Table[PlayerProfile](tag, "PLAYER_SESSION") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def first: Rep[Boolean] = column[Boolean]("first")

  def * : ProvenShape[PlayerProfile] = (id, name, first) <> (PlayerProfile.tupled, PlayerProfile.unapply) // scalastyle:ignore
}

class GameSession(tag: Tag) extends Table[GameSessionProfile](tag, "GAME_SESSION") {

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstPlayerID: Rep[Int] = column[Int]("firstPlayerID")

  def secondPlayerID: Rep[Int] = column[Int]("secondPlayerID")

  def firstPlayerContainerID: Rep[Int] = column[Int]("firstPlayerContainerID")

  def secondPlayerContainerID: Rep[Int] = column[Int]("secondPlayerContainerID")

  def state: Rep[Boolean] = column[Boolean]("state")

  def * : ProvenShape[GameSessionProfile] = (id, firstPlayerID, secondPlayerID, state, firstPlayerContainerID, secondPlayerContainerID) <> (GameSessionProfile.tupled, GameSessionProfile.unapply) // scalastyle:ignore

  def firstPlayer: ForeignKeyQuery[PlayerSession, PlayerProfile] = foreignKey("firstPlayer_fk", firstPlayerID, TableQuery[PlayerSession])(_.id)

  def secondPlayer: ForeignKeyQuery[PlayerSession, PlayerProfile] = foreignKey("secondPlayer_fk", secondPlayerID, TableQuery[PlayerSession])(_.id)

  def firstPlayerContainer: ForeignKeyQuery[PlayerContainerSession, PlayerContainerProfile] = {
    foreignKey("firstPlayerContainer_fk", firstPlayerContainerID, TableQuery[PlayerContainerSession])(_.id)
  }

  def secondPlayerContainer: ForeignKeyQuery[PlayerContainerSession, PlayerContainerProfile] = {
    foreignKey("secondPlayerContainer_fk", secondPlayerContainerID, TableQuery[PlayerContainerSession])(_.id)
  }
}

