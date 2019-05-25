package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionTypeCommand
import domain.element.action.ActionType
import domain.environment.Environment

class ModifyActionTypeHandler(environment: Environment) {

  /**
    *
    * @param modifyActionTypeCommand command
    * @return an exception or the action type
    */
  def execute(modifyActionTypeCommand: ModifyActionTypeCommand): Either[Exception, domain.element.action.ActionType.ActionType] = {
    environment.getAction(modifyActionTypeCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.actionType = modifyActionTypeCommand.actionType match {
        case application.command.action.modify.ActionType.ENTRY => ActionType.ENTRY
        case application.command.action.modify.ActionType.EXIT => ActionType.EXIT
        case application.command.action.modify.ActionType.GUARD => ActionType.GUARD
      }
    }
  }
}
