package application.commandhandler.end.add

import application.command.end.add.AddEndCommand
import domain.Environment

class AddEndHandler {
  def execute(addEndCommand: AddEndCommand): Either[Exception, Boolean] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isEndDefined = true
    }
  }
}
