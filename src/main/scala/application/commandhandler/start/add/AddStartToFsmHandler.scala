package application.commandhandler.start.add

import application.command.start.add.AddStartToFsmCommand
import domain.Environment

class AddStartToFsmHandler {
  def execute(addStartCommand: AddStartToFsmCommand): Either[Exception, Boolean] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => fsm.isStartDefined = true
    }
  }
}
