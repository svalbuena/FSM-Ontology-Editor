package application.commandhandler.start.remove

import application.command.start.remove.RemoveStartCommand
import domain.Environment

class RemoveStartHandler {
  def execute(removeStartCommand: RemoveStartCommand): Either[Exception, Boolean] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isStartDefined = false
    }
  }
}
