package application.commandhandler.end.remove

import application.command.end.remove.RemoveEndCommand
import domain.Environment

class RemoveEndHandler {
  def execute(removeEndCommand: RemoveEndCommand): Either[Exception, _] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isEndDefined = false
    }
  }
}
