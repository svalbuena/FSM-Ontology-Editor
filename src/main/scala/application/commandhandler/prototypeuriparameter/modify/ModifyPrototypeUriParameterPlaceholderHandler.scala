package application.commandhandler.prototypeuriparameter.modify

import application.command.prototypeuriparameter.modify.ModifyPrototypeUriParameterPlaceholderCommand
import domain.environment.Environment

class ModifyPrototypeUriParameterPlaceholderHandler(environment: Environment) {

  /**
    *
    * @param modifyPrototypeUriParameterPlaceholderCommand command
    * @return an exception or the placeholder
    */
  def execute(modifyPrototypeUriParameterPlaceholderCommand: ModifyPrototypeUriParameterPlaceholderCommand): Either[Exception, String] = {
    environment.getPrototypeUriParameter(modifyPrototypeUriParameterPlaceholderCommand.parameterName) match {
      case Left(error) => Left(error)
      case Right(parameter) => parameter.placeholder = modifyPrototypeUriParameterPlaceholderCommand.placeholder
    }
  }
}
