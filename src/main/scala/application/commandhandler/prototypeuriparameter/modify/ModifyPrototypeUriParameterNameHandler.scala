package application.commandhandler.prototypeuriparameter.modify

import application.command.prototypeuriparameter.modify.ModifyPrototypeUriParameterNameCommand
import domain.Environment

class ModifyPrototypeUriParameterNameHandler {

  /**
    *
    * @param modifyPrototypeUriParameterNameCommand command
    * @return an exception or the parameter name
    */
  def execute(modifyPrototypeUriParameterNameCommand: ModifyPrototypeUriParameterNameCommand): Either[Exception, String] = {
    Environment.getPrototypeUriParameter(modifyPrototypeUriParameterNameCommand.parameterName) match {
      case Left(error) => Left(error)
      case Right(parameter) => parameter.name = modifyPrototypeUriParameterNameCommand.newParameterName
    }
  }
}
