package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionNameCommand
import domain.Environment

class ModifyActionNameHandler {
  def execute(modifyActionNameCommand: ModifyActionNameCommand): Either[Exception, String] = {
    Environment.getAction(modifyActionNameCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.name = modifyActionNameCommand.newActionName
    }
  }
}
