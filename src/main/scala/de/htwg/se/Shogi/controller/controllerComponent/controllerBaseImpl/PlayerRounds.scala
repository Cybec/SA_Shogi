package de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.Shogi.controller.controllerComponent.MoveResult
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.pieceComponent.PieceInterface
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl._

trait RoundState {

  def changeState()

  def moveConqueredPiece(pieceAbbreviation: String, destination: (Int, Int)): Boolean

  def getPossibleMoves(pos: (Int, Int)): List[(Int, Int)]

  def movePiece(currentPos: (Int, Int), destination: (Int, Int)): MoveResult.Value

  def getPossibleMovesConqueredPiece(piece: String): List[(Int, Int)]
}

case class PlayerOneRound(controller: Controller) extends RoundState {

  override def changeState(): Unit = controller.currentState = controller.playerTwosTurn

  override def movePiece(currentPos: (Int, Int), destination: (Int, Int)): MoveResult.Value = {
    if (!(getPossibleMoves(currentPos).contains(destination) &&
      controller.currentState.isInstanceOf[PlayerOneRound])) {
      MoveResult.invalidMove
    } else {

      controller.saveState()

      val result: Option[MoveResult.Value] = for (tempPieceDestination <- controller.board.cell(destination._1, destination._2);
                                                  tempPieceCurrent <- controller.board.cell(currentPos._1, currentPos._2)) yield {
        val emptyPiece = PieceFactory.apply(PiecesEnum.EmptyPiece, controller.player_1.first)

        controller.board = controller.board.replaceCell(destination._1, destination._2, tempPieceCurrent)
        controller.board = controller.board.replaceCell(currentPos._1, currentPos._2, emptyPiece)
        controller.board = controller.board.addToPlayerContainer(tempPieceCurrent.isFirstOwner, tempPieceDestination)

        val isDestKing = PieceFactory.isInstanceOfPiece(PiecesEnum.King, tempPieceDestination)
        if (isDestKing) MoveResult.kingSlain else MoveResult.validMove
      }
      if (result == None) MoveResult.invalidMove else result.get
    }
  }

  override def getPossibleMoves(pos: (Int, Int)): List[(Int, Int)] = {
    controller.board.cell(pos._1, pos._2) match {
      case Some(piece) =>
        if (piece.isFirstOwner) {
          piece.getMoveSet((pos._1, pos._2), controller.board)
        } else {
          List()
        }
      case None => List()
    }
  }

  override def moveConqueredPiece(pieceAbbreviation: String, destination: (Int, Int)): Boolean = {
    if (getPossibleMovesConqueredPiece(pieceAbbreviation).contains(destination)) {

      val getFromPlayerContainer = controller.board.getFromPlayerContainer(controller.player_1) {
        _.typeEquals(pieceAbbreviation)
      }

      getFromPlayerContainer match {
        case Some(value: (BoardInterface, PieceInterface)) =>
          val newBoard: BoardInterface = value._1
          val piece: PieceInterface = value._2
          controller.saveState()

          controller.board = newBoard
          controller.board = controller.board.replaceCell(destination._1, destination._2, piece)
          true
        case None | Some(_) => false
      }
    } else {
      false
    }
  }

  override def getPossibleMovesConqueredPiece(piece: String): List[(Int, Int)] = {
    def isColContainingOwnPawns(column: Int) = {
      controller.board.getPiecesInColumn(column, stateTurn = true).exists((x: PieceInterface) => x.typeEquals("P°"))
    }

    def isColContainingEnemyKing(column: Int) = {
      controller.board.getPiecesInColumn(column, stateTurn = true).exists((x: PieceInterface) => x.typeEquals("K°"))
    }

    def getCellsInColumnWithKing(column: Int): List[(Int, Int)] = {
      val indexEnemyKing = controller.board.getAllPiecesInColumnOrdered(column).indexWhere(_.typeEquals("K"))
      controller.board.getEmptyCellsInColumn(column, (0, indexEnemyKing - 2)) ::: controller.board.getEmptyCellsInColumn(column, (indexEnemyKing + 1, 7))
    }

    def getCellsWithoutKingInColumn(colWithoutOwnPawns: IndexedSeq[Int]): List[(Int, Int)] = {
      colWithoutOwnPawns.filter(!isColContainingEnemyKing(_)).map(column =>
        controller.board.getEmptyCellsInColumn(column, (0, 7))).toList.flatten
    }

    def getCellsWithKingInColumn(colWithoutOwnPawns: IndexedSeq[Int]): List[(Int, Int)] = {
      colWithoutOwnPawns.filter(isColContainingEnemyKing(_)).map(column =>
        getCellsInColumnWithKing(column)).toList.flatten
    }

    val columns = 0 until controller.board.size
    val colWithoutOwnPawns = columns.filter(!isColContainingOwnPawns(_))
    piece match {
      case "P°" =>
        getCellsWithoutKingInColumn(colWithoutOwnPawns) ::: getCellsWithKingInColumn(colWithoutOwnPawns)
      case "KN°" | "L°" =>
        columns.map(x => controller.board.getEmptyCellsInColumn(x, (0, 7))).toList.flatten
      case x if x.endsWith("°") =>
        columns.map(x => controller.board.getEmptyCellsInColumn(x, (0, 8))).toList.flatten
      case _ => List[(Int, Int)]()
    }
  }
}

case class PlayerTwoRound(controller: Controller) extends RoundState {

  override def changeState(): Unit = controller.currentState = controller.playerOnesTurn

  override def movePiece(currentPos: (Int, Int), destination: (Int, Int)): MoveResult.Value = {
    if (!(getPossibleMoves(currentPos).contains(destination) &&
      controller.currentState.isInstanceOf[PlayerTwoRound])) {
      MoveResult.invalidMove
    } else {

      controller.saveState()

      val result: Option[MoveResult.Value] = for (tempPieceDestination <- controller.board.cell(destination._1, destination._2);
                                                  tempPieceCurrent <- controller.board.cell(currentPos._1, currentPos._2)) yield {
        val emptyPiece = PieceFactory.apply(PiecesEnum.EmptyPiece, controller.player_1.first)

        controller.board = controller.board.replaceCell(destination._1, destination._2, tempPieceCurrent)
        controller.board = controller.board.replaceCell(currentPos._1, currentPos._2, emptyPiece)
        controller.board = controller.board.addToPlayerContainer(tempPieceCurrent.isFirstOwner, tempPieceDestination)

        val isDestKing = PieceFactory.isInstanceOfPiece(PiecesEnum.King, tempPieceDestination)
        if (isDestKing) MoveResult.kingSlain else MoveResult.validMove
      }
      if (result == None) MoveResult.invalidMove else result.get
    }
  }

  override def getPossibleMoves(pos: (Int, Int)): List[(Int, Int)] = {
    controller.board.cell(pos._1, pos._2) match {
      case Some(piece: Piece) =>
        if (!piece.isFirstOwner) piece.getMoveSet((pos._1, pos._2), controller.board) else List()
      case None | Some(_) => List()
    }
  }

  override def moveConqueredPiece(pieceAbbreviation: String, destination: (Int, Int)): Boolean = {
    if (getPossibleMovesConqueredPiece(pieceAbbreviation).contains(destination)) {
      val getFromPlayerContainer = controller.board.getFromPlayerContainer(controller.player_2) {
        _.typeEquals(pieceAbbreviation)
      }

      getFromPlayerContainer match {
        case Some(value: (BoardInterface, PieceInterface)) =>
          val newBoard: BoardInterface = value._1
          val piece: PieceInterface = value._2

          controller.board = newBoard
          controller.board = controller.board.replaceCell(destination._1, destination._2, piece)
          true
        case None | Some(_) => false
      }
    } else {
      false
    }
  }

  override def getPossibleMovesConqueredPiece(piece: String): List[(Int, Int)] = {
    //TODO: test intern Methods
    def isColContainingOwnPawns(column: Int) = {
      controller.board.getPiecesInColumn(column, stateTurn = false).exists((x: PieceInterface) => x.typeEquals("P"))
    }

    def isColContainingEnemyKing(column: Int) = {
      controller.board.getPiecesInColumn(column, stateTurn = false).exists((x: PieceInterface) => x.typeEquals("K"))
    }

    def getCellsInColumnWithKing(column: Int): List[(Int, Int)] = {
      val indexEnemyKing = controller.board.getAllPiecesInColumnOrdered(column).indexWhere(_.typeEquals("K°"))
      controller.board.getEmptyCellsInColumn(column, (1, indexEnemyKing - 1)) ::: controller.board.getEmptyCellsInColumn(column, (indexEnemyKing + 2, 8))
    }

    def getCellsWithoutKingInColumn(colWithoutOwnPawns: IndexedSeq[Int]): List[(Int, Int)] = {
      colWithoutOwnPawns.filter(!isColContainingEnemyKing(_)).map(column =>
        controller.board.getEmptyCellsInColumn(column, (1, 8))).toList.flatten
    }

    def getCellsWithKingInColumn(colWithoutOwnPawns: IndexedSeq[Int]): List[(Int, Int)] = {
      colWithoutOwnPawns.filter(isColContainingEnemyKing(_)).map(column =>
        getCellsInColumnWithKing(column)).toList.flatten
    }

    val columns = 0 until controller.board.size
    val colWithoutOwnPawns = columns.filter(!isColContainingOwnPawns(_))
    piece match {
      case "P" =>
        getCellsWithoutKingInColumn(colWithoutOwnPawns) ::: getCellsWithKingInColumn(colWithoutOwnPawns)
      case "KN" | "L" =>
        columns.map(x => controller.board.getEmptyCellsInColumn(x, (1, 8))).toList.flatten
      case x if !x.endsWith("°") =>
        columns.map(x => controller.board.getEmptyCellsInColumn(x, (1, 8))).toList.flatten
      case _ => List[(Int, Int)]()
    }
  }
}
