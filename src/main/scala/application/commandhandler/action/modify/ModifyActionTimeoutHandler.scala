package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionTimeoutCommand
import domain.Environment

class ModifyActionTimeoutHandler {
  def execute(modifyActionTimeoutCommand: ModifyActionTimeoutCommand): Either[Exception, Int] = {
    Environment.getAction(modifyActionTimeoutCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.timeout = modifyActionTimeoutCommand.timeout
    }
  }
}