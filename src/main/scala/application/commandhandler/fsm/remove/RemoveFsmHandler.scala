package application.commandhandler.fsm.remove

import application.command.fsm.remove.RemoveFsmCommand
import domain.Environment

class RemoveFsmHandler {
  def execute(removeFsmCommand: RemoveFsmCommand): Either[Exception, _] = {
    Environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => Environment.removeFsm(fsm)
    }
  }
}
