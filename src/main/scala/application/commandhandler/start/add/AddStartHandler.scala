package application.commandhandler.start.add

import application.command.start.add.AddStartCommand
import domain.Environment

class AddStartHandler {
  def execute(addStartCommand: AddStartCommand): Either[Exception, Boolean] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isStartDefined = true
    }
  }
}
