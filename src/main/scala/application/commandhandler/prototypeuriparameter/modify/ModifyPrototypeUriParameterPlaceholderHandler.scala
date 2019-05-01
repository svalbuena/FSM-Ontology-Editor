package application.commandhandler.prototypeuriparameter.modify

import application.command.prototypeuriparameter.modify.ModifyPrototypeUriParameterPlaceholderCommand
import domain.Environment

class ModifyPrototypeUriParameterPlaceholderHandler {

  /**
    *
    * @param modifyPrototypeUriParameterPlaceholderCommand command
    * @return an exception or the placeholder
    */
  def execute(modifyPrototypeUriParameterPlaceholderCommand: ModifyPrototypeUriParameterPlaceholderCommand): Either[Exception, String] = {
    Environment.getPrototypeUriParameter(modifyPrototypeUriParameterPlaceholderCommand.parameterName) match {
      case Left(error) => Left(error)
      case Right(parameter) => parameter.placeholder = modifyPrototypeUriParameterPlaceholderCommand.placeholder
    }
  }
}
