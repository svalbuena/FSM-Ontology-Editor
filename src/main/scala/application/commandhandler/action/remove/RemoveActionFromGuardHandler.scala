package application.commandhandler.action.remove

import application.command.action.remove.RemoveActionFromGuardCommand
import domain.Environment

class RemoveActionFromGuardHandler(environment: Environment) {

  /**
    *
    * @param removeActionFromGuardCommand command
    * @return an exception or nothing if successful
    */
  def execute(removeActionFromGuardCommand: RemoveActionFromGuardCommand): Either[Exception, _] = {
    environment.getAction(removeActionFromGuardCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) =>
        environment.getGuard(removeActionFromGuardCommand.guardName) match {
          case Left(error) => Left(error)
          case Right(guard) => guard.removeAction(action)
        }
    }
  }
}
