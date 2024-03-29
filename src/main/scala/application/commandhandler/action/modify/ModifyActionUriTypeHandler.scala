package application.commandhandler.action.modify

import application.command.action.modify.ModifyActionUriTypeCommand
import domain.element.action.UriType
import domain.environment.Environment

class ModifyActionUriTypeHandler(environment: Environment) {

  /**
    *
    * @param modifyActionUriTypeCommand command
    * @return an exception or the action uri type
    */
  def execute(modifyActionUriTypeCommand: ModifyActionUriTypeCommand): Either[Exception, domain.element.action.UriType.UriType] = {
    environment.getAction(modifyActionUriTypeCommand.actionName) match {
      case Left(error) => Left(error)
      case Right(action) => action.uriType = modifyActionUriTypeCommand.uriType match {
        case application.command.action.modify.UriType.ABSOLUTE => UriType.ABSOLUTE
        case application.command.action.modify.UriType.PROTOTYPE => UriType.PROTOTYPE
      }
    }
  }
}
