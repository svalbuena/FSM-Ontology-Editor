package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionTypeCommand
import domain.Environment
import domain.action.ActionType

class ModifyActionTypeHandler {
  def execute(modifyActionTypeCommand: ModifyActionTypeCommand): Either[Exception, domain.action.ActionType.ActionType] = {
    Environment.getAction(modifyActionTypeCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.actionType = modifyActionTypeCommand.actionType match {
        case application.command.action.modify.ActionType.ENTRY => ActionType.ENTRY
        case application.command.action.modify.ActionType.EXIT => ActionType.EXIT
        case application.command.action.modify.ActionType.GUARD => ActionType.GUARD
      }
    }
  }
}
