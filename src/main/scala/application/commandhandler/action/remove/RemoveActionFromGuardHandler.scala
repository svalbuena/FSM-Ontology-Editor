package application.commandhandler.action.remove

import application.command.action.remove.RemoveActionFromGuardCommand
import domain.Environment

class RemoveActionFromGuardHandler {
  def execute(removeActionFromGuardCommand: RemoveActionFromGuardCommand): Either[Exception, _] = {
    Environment.getAction(removeActionFromGuardCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) =>
        Environment.getGuard(removeActionFromGuardCommand.guardName) match {
          case Left(error) => Left(error)
          case Right(guard) => guard.removeAction(action)
        }
    }
  }
}
