package application.commandhandler.action.remove

import application.command.action.remove.RemoveActionFromStateCommand
import domain.environment.Environment

class RemoveActionFromStateHandler(environment: Environment) {

  /**
    *
    * @param removeActionFromStateCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeActionFromStateCommand: RemoveActionFromStateCommand): Either[Exception, _] = {
    environment.getAction(removeActionFromStateCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) =>
        environment.getState(removeActionFromStateCommand.stateName) match {
          case Left(error) => Left(error)
          case Right(state) => state.removeAction(action)
        }
    }
  }


}
