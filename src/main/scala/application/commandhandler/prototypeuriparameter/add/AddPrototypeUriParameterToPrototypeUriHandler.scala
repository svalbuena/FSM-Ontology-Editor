package application.commandhandler.prototypeuriparameter.add

import application.command.prototypeuriparameter.add.AddPrototypeUriParameterToPrototypeUriCommand
import domain.Environment
import domain.action.PrototypeUriParameter

class AddPrototypeUriParameterToPrototypeUriHandler {

  /**
    *
    * @param addActionPrototypeUriParameterCommand command
    * @return an exception or the parameter name
    */
  def execute(addActionPrototypeUriParameterCommand: AddPrototypeUriParameterToPrototypeUriCommand): Either[Exception, String] = {
    Environment.getPrototypeUri(addActionPrototypeUriParameterCommand.prototypeUriName) match {
      case Left(error) => Left(error)
      case Right(prototypeUri) =>
        val parameter = new PrototypeUriParameter
        prototypeUri.addPrototypeUriParameter(parameter) match {
          case Left(error) => Left(error)
          case Right(_) => Right(parameter.name)
        }
    }
  }
}
