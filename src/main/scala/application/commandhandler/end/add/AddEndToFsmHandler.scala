package application.commandhandler.end.add

import application.command.end.add.AddEndToFsmCommand
import domain.Environment

class AddEndToFsmHandler {
  def execute(addEndCommand: AddEndToFsmCommand): Either[Exception, Boolean] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isEndDefined = true
    }
  }
}
