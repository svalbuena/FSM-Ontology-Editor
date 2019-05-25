package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionAbsoluteUriCommand
import domain.environment.Environment

class ModifyActionAbsoluteUriHandler(environment: Environment) {

  /**
    *
    * @param modifyActionAbsoluteUriCommand command
    * @return an exception or the absolute uri
    */
  def execute(modifyActionAbsoluteUriCommand: ModifyActionAbsoluteUriCommand): Either[Exception, String] = {
    environment.getAction(modifyActionAbsoluteUriCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.absoluteUri = modifyActionAbsoluteUriCommand.absoluteUri
    }
  }
}
