package application.commandhandler.action.remove

import application.command.action.remove.RemoveActionFromStateCommand
import domain.Environment

class RemoveActionFromStateHandler {
  def execute(removeActionFromStateCommand: RemoveActionFromStateCommand): Either[Exception, _] = {
    Environment.getAction(removeActionFromStateCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) =>
        Environment.getState(removeActionFromStateCommand.stateName) match {
          case Left(error) => Left(error)
          case Right(state) => state.removeAction(action)
        }
    }
  }


}
