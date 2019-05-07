package application.commandhandler.prototypeuriparameter.modify

import application.command.prototypeuriparameter.modify.ModifyPrototypeUriParameterQueryCommand
import domain.Environment

class ModifyPrototypeUriParameterQueryHandler(environment: Environment) {

  /**
    *
    * @param modifyPrototypeUriParameterQueryCommand command
    * @return an exception or the query
    */
  def execute(modifyPrototypeUriParameterQueryCommand: ModifyPrototypeUriParameterQueryCommand): Either[Exception, String] = {
    environment.getPrototypeUriParameter(modifyPrototypeUriParameterQueryCommand.parameterName) match {
      case Left(error) => Left(error)
      case Right(parameter) => parameter.query = modifyPrototypeUriParameterQueryCommand.query
    }
  }
}
