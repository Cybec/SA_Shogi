package de.htwg.se.Shogi

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.Shogi.controller.controllerComponent.ControllerInterface
import de.htwg.se.Shogi.controller.controllerComponent.controllerBaseImpl
import de.htwg.se.Shogi.model.boardComponent.{ BoardInterface, boardBaseImpl }
import de.htwg.se.Shogi.model.fileIoComponent.{ DAOInterface, fileIoJsonImpl, slickDBImpl, mongoDBImpl }
import net.codingwell.scalaguice.ScalaModule

object ShogiModuleConf {
  val defaultBoardSize: Int = 9
  val smallBoardSize: Int = 6
  val tinyBoardSize: Int = 3

  val defaultBoard: String = "normal"
  val smallBoard: String = "small"
  val tinyBoard: String = "tiny"
}

class ShogiModule extends AbstractModule with ScalaModule {

  def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(ShogiModuleConf.defaultBoardSize)

    bind[BoardInterface].to[boardBaseImpl.BoardInj]
    bind[ControllerInterface].to[controllerBaseImpl.Controller]

    bind[DAOInterface].to[mongoDBImpl.MongoDB]
    //    bind[DAOInterface].to[slickDBImpl.SlickDB]
    //    bind[DAOInterface].to[fileIoJsonImpl.FileIO]
    //    bind[FileIOInterface].to[fileIoXmlImpl.FileIO]

    bind[BoardInterface].annotatedWithName(ShogiModuleConf.defaultBoard).toInstance(
      new boardBaseImpl.BoardInj(ShogiModuleConf.defaultBoardSize)
    )

    bind[BoardInterface].annotatedWithName(ShogiModuleConf.smallBoard).toInstance(
      new boardBaseImpl.BoardInj(ShogiModuleConf.smallBoardSize)
    )

    bind[BoardInterface].annotatedWithName(ShogiModuleConf.tinyBoard).toInstance(
      new boardBaseImpl.BoardInj(ShogiModuleConf.tinyBoardSize)
    )
  }

}
