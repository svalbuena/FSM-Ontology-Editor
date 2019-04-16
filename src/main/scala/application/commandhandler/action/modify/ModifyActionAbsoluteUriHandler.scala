package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionAbsoluteUriCommand
import domain.Environment

class ModifyActionAbsoluteUriHandler {
  def execute(modifyActionAbsoluteUriCommand: ModifyActionAbsoluteUriCommand): Either[Exception, String] = {
    Environment.getAction(modifyActionAbsoluteUriCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.absoluteUri = modifyActionAbsoluteUriCommand.absoluteUri
    }
  }
}
