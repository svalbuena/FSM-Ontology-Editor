package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionNameCommand
import domain.environment.Environment

class ModifyActionNameHandler(environment: Environment) {
  /**
    *
    * @param modifyActionNameCommand command
    * @return an exception or the action name
    */
  def execute(modifyActionNameCommand: ModifyActionNameCommand): Either[Exception, String] = {
    environment.getAction(modifyActionNameCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.name = modifyActionNameCommand.newActionName
    }
  }
}
