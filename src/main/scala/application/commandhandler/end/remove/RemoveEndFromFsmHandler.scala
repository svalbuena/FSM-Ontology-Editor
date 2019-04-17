package application.commandhandler.end.remove

import application.command.end.remove.RemoveEndFromFsmCommand
import domain.Environment

class RemoveEndFromFsmHandler {
  def execute(removeEndCommand: RemoveEndFromFsmCommand): Either[Exception, _] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isEndDefined = false
    }
  }
}
