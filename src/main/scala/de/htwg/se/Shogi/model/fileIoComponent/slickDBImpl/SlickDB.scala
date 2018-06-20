package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import de.htwg.se.Shogi.model.playerComponent.Player

object Test {
  def main(args: Array[String]): Unit = {
    val dBQuery = new DBQuery
    val playerProfile = PlayerProfile(1, "TestPlayer_3248079", true)

    val insertResult = dBQuery.insert(playerProfile)
    print("Result: " + insertResult)
  }
}

class SlickDB extends DAOInterface {

  /**
   * Loads the saved game
   *
   * @return Returning an Option with the loaded Board, playerTurn and the two PLayers
   */
  override def load: Option[(BoardInterface, Boolean, Player, Player)] = {

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

  }
}
