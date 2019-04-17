package application.commandhandler.start.remove

import application.command.start.remove.RemoveStartFromFsmCommand
import domain.Environment

class RemoveStartFromFsmHandler {
  def execute(removeStartCommand: RemoveStartFromFsmCommand): Either[Exception, Boolean] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isStartDefined = false
    }
  }
}
