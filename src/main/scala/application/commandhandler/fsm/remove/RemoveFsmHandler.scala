package application.commandhandler.fsm.remove

import application.command.fsm.remove.RemoveFsmCommand
import domain.environment.Environment

class RemoveFsmHandler(environment: Environment) {

  /**
    *
    * @param removeFsmCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeFsmCommand: RemoveFsmCommand): Either[Exception, _] = {
    environment.getSelectedFsm match {
      case Left(error) => Left(error)
      case Right(fsm) => environment.removeFsm(fsm)
    }
  }
}
