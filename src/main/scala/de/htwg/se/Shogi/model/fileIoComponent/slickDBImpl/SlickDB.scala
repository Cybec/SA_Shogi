package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.playerComponent.Player

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SlickDB extends DAOInterface {

  /**
   * Loads the saved game
   *
   * @return Returning an Option with the loaded Board, playerTurn and the two PLayers
   */
  override def load: Option[(BoardInterface, Boolean, Player, Player)] = {

    val dbQuery = new DBQuery

    println("Inserting new user: TestPiece")
    val eventualInsertResult = dbQuery.insert(PieceProfile(1, "TestPiece", true, true))
    val insertResult = Await.result(eventualInsertResult, Duration.Inf)
    println(s"Insert result: $insertResult\n")

    println("Reading user by ID: 1")
    val eventualMaybeUserProfile = dbQuery.getPiece(1)
    val maybeUserProfile = Await.result(eventualMaybeUserProfile, Duration.Inf)
    println(s"User profile by ID 1: $maybeUserProfile\n")

    //    println("Deleting user by ID: 1")
    //    val eventualDeleteResult = dbQuery.deletePiece(1)
    //    val deleteResult = Await.result(eventualDeleteResult, Duration.Inf)
    //    println(s"Delete result: $deleteResult")

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
  override def save(board: BoardInterface, state: Boolean, player_1: Player, player_2: Player): Unit = {}
}
