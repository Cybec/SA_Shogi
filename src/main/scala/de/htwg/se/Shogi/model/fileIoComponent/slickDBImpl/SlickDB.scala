package de.htwg.se.Shogi.model.fileIoComponent.slickDBImpl

import de.htwg.se.Shogi.model.fileIoComponent.DAOInterface
import com.google.inject.Guice
import com.google.inject.name.Names
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl.{ PieceFactory, PiecesEnum }
import de.htwg.se.Shogi.model.playerComponent.Player
import net.codingwell.scalaguice.InjectorExtensions._

class SlickDB extends DAOInterface {
  val dBQuery = new DBQuery

  /**
   * Loads the saved game
   *
   * @return Returning an Option with the loaded Board, playerTurn and the two PLayers
   */
  //noinspection ScalaStyle
  override def load: Option[(BoardInterface, Boolean, Player, Player)] = {
    val lastGameID = dBQuery.getLastIDGame
    val injector = Guice.createInjector(new ShogiModule)
    var board: BoardInterface = injector.instance[BoardInterface](Names.named("normal"))

    val game = dBQuery.getGame(lastGameID).getOrElse(return None)
    val firstPlayerID = dBQuery.getPlayer(game.firstPlayerID).get
    val secondPlayerID = dBQuery.getPlayer(game.secondPlayerID).get
    val firstPlayerContainerID = game.firstPlayerContainerID
    val secondPlayerContainerID = game.secondPlayerContainerID
    val state = game.state

    for (pieceProfile <- dBQuery.getPieceInContainer(firstPlayerContainerID)) {
      val pieceName = if (pieceProfile.name.trim.isEmpty) "EmptyPiece" else pieceProfile.name
      val piece = PieceFactory.apply(PiecesEnum.withNameOpt(pieceName).get, pieceProfile.isFirstOwner)
      board = board.addToPlayerContainer(piece.isFirstOwner, piece)
    }

    for (pieceProfile <- dBQuery.getPieceInContainer(secondPlayerContainerID)) {
      val pieceName = if (pieceProfile.name.trim.isEmpty) "EmptyPiece" else pieceProfile.name
      val piece = PieceFactory.apply(PiecesEnum.withNameOpt(pieceName).get, pieceProfile.isFirstOwner)
      board = board.addToPlayerContainer(piece.isFirstOwner, piece)
    }

    for (pieceProfile <- dBQuery.getPieceOnBoard(game.firstPlayerID)) {
      val piece = if (pieceProfile.name.trim.isEmpty) PiecesEnum.withNameOpt("EmptyPiece").get else PiecesEnum.withNameOpt(pieceProfile.name).get
      board = board.replaceCell(pieceProfile.positionColumn, pieceProfile.positionRow, PieceFactory.apply(piece, pieceProfile.isFirstOwner))
    }

    for (pieceProfile <- dBQuery.getPieceOnBoard(game.secondPlayerID)) {
      val piece = if (pieceProfile.name.trim.isEmpty) PiecesEnum.withNameOpt("EmptyPiece").get else PiecesEnum.withNameOpt(pieceProfile.name).get
      board = board.replaceCell(pieceProfile.positionColumn, pieceProfile.positionRow, PieceFactory.apply(piece, pieceProfile.isFirstOwner))
    }

    Some((board, state, Player(firstPlayerID.name, firstPlayerID.first), Player(secondPlayerID.name, secondPlayerID.first)))
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
    val playerProfile_1 = PlayerProfile(1, player_1.name, player_1.first)
    val playerProfile_2 = PlayerProfile(1, player_2.name, player_2.first)
    val player1Container = PlayerContainerProfile(1)
    val player2Container = PlayerContainerProfile(1)

    val insPlayerRes_1 = dBQuery.insert(playerProfile_1).get
    val insPlayerRes_2 = dBQuery.insert(playerProfile_2).get
    val insPlayerContRes_1 = dBQuery.insert(player1Container).get
    val insPlayerContRes_2 = dBQuery.insert(player2Container).get

    board.getContainer._1.foreach(x => dBQuery.insert(PieceContainerProfile(1, x.name, x.hasPromotion, x.isFirstOwner, insPlayerContRes_1._2)))
    board.getContainer._2.foreach(x => dBQuery.insert(PieceContainerProfile(1, x.name, x.hasPromotion, x.isFirstOwner, insPlayerContRes_2._2)))

    for {
      col <- 0 until board.size
      row <- 0 until board.size
      piece <- board.cell(col, row)
    } yield {
      val currentPlayer = if (piece.isFirstOwner) insPlayerRes_1._2 else insPlayerRes_2._2
      dBQuery.insert(PieceOnBoardProfile(1, piece.name, row, col, piece.hasPromotion, piece.isFirstOwner, currentPlayer))
    }

    dBQuery.insert(GameSessionProfile(1, insPlayerRes_1._2, insPlayerRes_2._2, state, insPlayerContRes_1._2, insPlayerContRes_2._2))
  }
}
