package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionTimeoutCommand
import domain.environment.Environment

class ModifyActionTimeoutHandler(environment: Environment) {
  /**
    *
    * @param modifyActionTimeoutCommand command
    * @return an exception or the action timeout
    */
  def execute(modifyActionTimeoutCommand: ModifyActionTimeoutCommand): Either[Exception, Int] = {
    environment.getAction(modifyActionTimeoutCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.timeout = modifyActionTimeoutCommand.timeout
    }
  }
}
