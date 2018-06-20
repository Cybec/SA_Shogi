package de.htwg.se.Shogi.model.boardComponent.boardBaseImpl

import com.google.inject.name.Named
import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.Shogi.ShogiModule
import de.htwg.se.Shogi.model.boardComponent.BoardInterface
import de.htwg.se.Shogi.model.pieceComponent.PieceInterface
import de.htwg.se.Shogi.model.pieceComponent.pieceBaseImpl.{PieceFactory, PiecesEnum}
import de.htwg.se.Shogi.model.playerComponent.Player
import net.codingwell.scalaguice.InjectorExtensions._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

class BoardInj @Inject()(@Named("DefaultSize") boardSize: Int) extends Board(boardSize, PieceFactory.getEmptyPiece)

case class Board(
                  board: Vector[Vector[PieceInterface]],
                  containerPlayer_0: List[PieceInterface],
                  containerPlayer_1: List[PieceInterface]
                ) extends BoardInterface {
  override def createNewBoard(): BoardInterface = new Board(size, PieceFactory.getEmptyPiece)

  def this(size: Int, filling: PieceInterface) =
    this(Vector.tabulate(size, size) { (_, _) => filling }, List.empty[PieceInterface], List.empty[PieceInterface])

  override def getContainer: (List[PieceInterface], List[PieceInterface]) = {
    (containerPlayer_0, containerPlayer_1)
  }

  override def setContainer(container: (List[PieceInterface], List[PieceInterface])): Board = {
    copy(board, container._1, container._2)
  }

  override def addToPlayerContainer(first: Boolean, piece: PieceInterface): Board = {
    if (!PieceFactory.isInstanceOfPiece(PiecesEnum.EmptyPiece, piece)) {
      if (first) {
        val newCon: List[PieceInterface] = containerPlayer_0 :+ piece.cloneToNewPlayer(first)
        copy(board, newCon, containerPlayer_1)
      } else {
        val newCon: List[PieceInterface] = containerPlayer_1 :+ piece.cloneToNewPlayer(first)
        copy(board, containerPlayer_0, newCon)
      }
    } else {
      this
    }
  }

  override def getFromPlayerContainer(player: Player)(pred: (PieceInterface) => Boolean): Option[(Board, PieceInterface)] = {

    def getFromPlayerOne(): Option[(Board, PieceInterface)] = {
      val (before, atAndAfter) = containerPlayer_0 span (x => !pred(x))
      if (atAndAfter.nonEmpty) {
        val getPiece: PieceInterface = atAndAfter.head
        val newCon: List[PieceInterface] = before ::: atAndAfter.drop(1)
        Some((copy(board, newCon, containerPlayer_1), getPiece.cloneToNewPlayer(player.first)))
      } else {
        None
      }
    }

    def getFromPlayerTwo(): Option[(Board, PieceInterface)] = {
      val (before, atAndAfter) = containerPlayer_1 span (x => !pred(x))
      if (atAndAfter.nonEmpty) {
        val getPiece: PieceInterface = atAndAfter.head
        val newCon: List[PieceInterface] = before ::: atAndAfter.drop(1)
        Some((copy(board, containerPlayer_0, newCon), getPiece.cloneToNewPlayer(player.first)))
      } else {
        None
      }
    }

    if (player.first) {
      getFromPlayerOne()
    } else {
      getFromPlayerTwo()
    }
  }

  override def replaceCell(col: Int, row: Int, cell: PieceInterface): Board =
    copy(board.updated(col, board(col).updated(row, cell)), containerPlayer_0, containerPlayer_1)

  override def copyBoard(): Board = copy(board, containerPlayer_0, containerPlayer_1)

  override def getPiecesInColumn(column: Int, stateTurn: Boolean): List[PieceInterface] = {
    var pieces = List[PieceInterface]()

    if (column <= this.size && column >= 0) {
      for (
        i <- 0 until this.size;
        piece <- this.cell(column, i) if !PieceFactory.isInstanceOfPiece(PiecesEnum.EmptyPiece, piece) if stateTurn == piece.isFirstOwner
      ) yield {
        pieces = pieces :+ piece
      }
    }
    pieces
  }

  override def getAllPiecesInColumnOrdered(column: Int): List[PieceInterface] = {
    var pieces = List[PieceInterface]()

    if (column <= this.size && column >= 0) {
      for (
        i <- 0 until this.size;
        piece <- this.cell(column, i)
      ) yield {
        pieces = pieces :+ piece
      }
    }
    pieces
  }

  override def getEmptyCellsInColumn(column: Int, range: (Int, Int)): List[(Int, Int)] = {
    var emptyCells = List[(Int, Int)]()

    if (column <= this.size && column >= 0) {
      for (
        i <- range._1 to range._2;
        piece <- this.cell(column, i) if PieceFactory.isInstanceOfPiece(PiecesEnum.EmptyPiece, piece)
      ) yield {
        emptyCells = emptyCells :+ (column, i)
      }
    }
    emptyCells
  }

  override def toArray: Array[Array[PieceInterface]] = {
    val returnList: Array[Array[PieceInterface]] = Array.ofDim[PieceInterface](size, size)

    for {
      col <- 0 until size
      row <- 0 until size
      piece <- cell(col, row)
    } yield {
      returnList(col)(row) = piece
    }
    returnList
  }

  override def size: Int = board.size

  override def toString: String = {
    var index: Int = 0
    val alphabet = Array[Char]('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')
    val returnValue = new StringBuilder

    returnValue.append("Captured: ")
    containerPlayer_0.foreach(x => returnValue.append(x).append("   "))

    returnValue.append("\n    0     1     2     3     4     5     6     7     8 \n \n")

    for (a <- 1 to 19) {
      if (a % 2 == 1) {
        for (_ <- 1 to 57) returnValue.append("-")
      } else {
        for (c <- 0 to 8) {
          cell(c, index) match {
            case Some(piece) => returnValue.append(" | " + piece)
            case None =>
          }
        }
        returnValue.append(" | \t" + alphabet(index))
        index += 1
      }
      returnValue.append("\n")
    }
    returnValue.append("Captured: ")
    containerPlayer_1.foreach(x => returnValue.append(x).append("   "))
    returnValue.append("\n")

    returnValue.toString()
  }

  override def cell(col: Int, row: Int): Option[PieceInterface] = {
    if (row >= size || col >= size || row < 0 || col < 0) {
      None
    } else {
      Some(board(col)(row))
    }
  }

  override def toHtml: String = "<p  style=\"font-family:'Lucida Console', monospace\"> " + toString.replace("\n", "<br>").replace(" ", "&nbsp") + "</p>"
}

object BoardServer {
  val PORT = 8081

  implicit val system = ActorSystem("Controller")
  implicit val materializer = ActorMaterializer()
  val injector: Injector = Guice.createInjector(new ShogiModule)
  val board: BoardInterface = injector.instance[BoardInterface](Names.named("normal")).createNewBoard()

  def main(args: Array[String]): Unit = {
    server
  }

  private def server: Unit = {
    val route: Route = {
      get {
        path("getContainer") {
          complete(HttpEntity(ContentTypes.`application/json`, getContainer))
        } ~
          path("getPiecesInColumn" / Segment / Segment) {
            (column, state) => {
              complete(HttpEntity(ContentTypes.`application/json`, getPiecesInColumn(column.toInt, state.toBoolean)))
            }
          } ~
          path("getAllPiecesInColumnOrdered" / Segment) {
            column => {
              complete(HttpEntity(ContentTypes.`application/json`, getAllPiecesInColumnOrdered(column.toInt)))
            }
          } ~
          path("getEmptyCellsInColumn" / Segment / Segment / Segment) {
            (column, rangeA, rangeB) => {
              val range = (rangeA.toInt, rangeB.toInt)
              complete(HttpEntity(ContentTypes.`application/json`, getEmptyCellsInColumn(column.toInt, range)))
            }
          } ~
          path("cell" / Segment / Segment) {
            (column, row) => {
              complete(HttpEntity(ContentTypes.`application/json`, cell(column.toInt, row.toInt)))
            }
          } ~
          path("size") {
            complete(HttpEntity(ContentTypes.`application/json`, size))
          } ~
          path("copyBoard") {
            complete(HttpEntity(ContentTypes.`application/json`, copyBoard))
          }
      }
      post {
        path("createNewBoard") {
          board.createNewBoard()
          complete(HttpEntity(ContentTypes.`application/json`, board.toHtml))
        }
      }
      //TODO: PUT

    }
  }

  //GET
  private def getContainer: String = {
    board.getContainer._1.mkString(",") + board.getContainer._2.mkString(",")
  }

  private def getFromPlayerContainer(player: Player): String = {
    //board.getFromPlayerContainer(player)
    ""
  }

  private def getPiecesInColumn(column: Int, stateTurn: Boolean): String = {
    board.getPiecesInColumn(column, stateTurn).mkString(",")
  }

  private def getAllPiecesInColumnOrdered(column: Int): String = {
    board.getAllPiecesInColumnOrdered(column).mkString(",")
  }

  private def getEmptyCellsInColumn(column: Int, range: (Int, Int)): String = {
    val list = board.getEmptyCellsInColumn(column, range)
    val string = new StringBuilder
    list.foreach(position => string.append(position.toString))
    list.toString()
  }

  private def cell(column: Int, row: Int): String = {
    board.cell(column, row).toString
  }

  private def size(): String = {
    board.size.toString
  }

  private def copyBoard(): String = {
    board.copyBoard.toString
  }

  //POST
  private def createNewBoard(): String = {
    board.createNewBoard().toString
  }
}